import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.mp.dto.work.EmpWorkDayMeta;
import nohi.demo.mp.dto.work.WorkSheetDTO;
import nohi.demo.mp.utils.DateUtils;
import nohi.demo.mp.utils.IdUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-18 13:35
 **/
@Slf4j
public class TestExcel {

    private List<EmpWorkDayMeta> data() {
        List<EmpWorkDayMeta> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            EmpWorkDayMeta data = new EmpWorkDayMeta();
            data.setProjectName("全链通");
            data.setUserNo("0006");
            data.setUserName("丁龙海");
            data.setWorkDay(DateUtils.parseDate("2021-01-01", DateUtils.HYPHEN_DATE));
            data.setWorkStart(DateUtils.parseDate("2021-01-01 08:12:00", DateUtils.HYPHEN_TIME));
            data.setWorkEnd(DateUtils.parseDate("2021-01-01 18:34:00", DateUtils.HYPHEN_TIME));
            data.setWorkDays(new BigDecimal(IdUtils.radomLength(3)));
            list.add(data);
        }
        return list;
    }

    @Test
    public void writer() {
        String path = "/";
        // 写法1
        String fileName = System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
//        EasyExcel.write(fileName, EmpWorkDayMeta.class).sheet("模板").doWrite(data());

        // 写法2
        fileName = System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(fileName, EmpWorkDayMeta.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            excelWriter.write(data(), writeSheet);
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    @Test
    public void projectSheet() throws Exception {
        WorkSheetDTO dto = new WorkSheetDTO();
        LocalDate start = DateUtils.stringToLocalDate("2021-01-01");
        LocalDate end = DateUtils.stringToLocalDate("2021-01-10");
        List<Object> title = this.title(start, end);
        System.out.println("title:" + title);

        String template = "";


        String outputFile = "/Users/nohi/work/workspaces-nohi/demo/nohi-dd-miniprogram-server/1.xlsx";
        try (OutputStream os = new FileOutputStream(outputFile);) {
            Workbook templatebook = null;
            log.info("template.getTemplate():" + template);
            templatebook = new SXSSFWorkbook();
            Sheet sheet = templatebook.createSheet("项目工时");
            Row row = sheet.createRow(0);
            this.writerRow(row, title);

            templatebook.write(os);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void writerRow(Row row, List<Object> data) {
        if (null == data) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            Cell cell = row.createCell(i);
            setValue(cell, data.get(i));
        }
    }

    public List<Object> title(LocalDate start, LocalDate end) {
        // 类别	项目	科室	人员
        List<Object> titleList = Lists.newArrayList("类别", "项目", "科室", "人员");
        LocalDate tmp = start;
        // [start to end]
        while (!end.isBefore(tmp)) {
            titleList.add(DateUtils.LocalDateToDate(tmp));
            tmp = tmp.plusDays(1);
        }
        titleList.add("汇总");
        return titleList;
    }

    /**
     * 取得值
     * @return
     */
    public static void setValue(Cell cell, Object rs){
        if (rs == null) {
            return;
        }

        if (rs instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) rs).doubleValue());
        } else if (rs instanceof Date) {
            cell.setCellValue((Date)rs);
        } else if (rs instanceof Timestamp) {
            cell.setCellValue((Timestamp)rs);
        } else if (rs instanceof Integer) {
            cell.setCellValue((Integer)rs);
        } else {
            cell.setCellValue(rs.toString());
        }
    }
}
