import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.mp.dto.work.WorkDayMeta;
import nohi.demo.mp.dto.work.WorkSheetDTO;
import nohi.doc.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.*;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
public class TestExcelTemplate {

    private List<WorkSheetDTO> data() {
        List<WorkSheetDTO> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            WorkSheetDTO data = new WorkSheetDTO();
            data.setProjectName("全链通");
            data.setUserNo("000" + i);
            data.setUserName("NAME" + i);
            data.setWorkDayList(this.createWorkDayList(data.getUserNo(), data.getUserName(), data.getProjectName()));
            list.add(data);
        }
        return list;
    }

    public List<WorkDayMeta> createWorkDayList(String userNo, String userName, String prjName) {
        List<WorkDayMeta> list = Lists.newArrayList();

        for (int i = 0; i < 10; i++) {
            WorkDayMeta data = new WorkDayMeta();
            data.setProjectName(prjName);
            data.setUserNo(userNo);
            data.setUserName(userName);
            data.setWorkDays(new BigDecimal("1." + i));
            list.add(data);
        }
        return list;
    }
    @Test
    public void projectSheet() {
        String template = "template/project_emp_worksheet.xlsx";
        String outputFile = "/Users/nohi/work/workspaces-nohi/demo/nohi-dd-miniprogram-server/1.xlsx";
        log.info( "template:{}", template);

        //2,读取Excel模板
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(template);
             FileOutputStream fos = new FileOutputStream(outputFile);
        ) {
            // 读取Excel模板
            Workbook templatebook = WorkbookFactory.create(is);
//            //备份模板
//            int templateSheetSize = templatebook.getNumberOfSheets();
//            for (int i = 1 ; i <= templateSheetSize ;i++) {
//                templatebook.setSheetName(i-1, templatebook.getSheetName(i-1) + "_bak");
//            }
            Sheet templateSheet = templatebook.getSheetAt(0);
            // 导出数据
            List<WorkSheetDTO> data = data(); // 模拟数据
            for (int i = 0; i < data.size(); i++) {
                WorkSheetDTO item = data.get(i);
                Row row = templateSheet.getRow(i + 1); // excel第二行，start with 0

                // 大于第一行时拷贝样式
                if (i > 0) {
                    if (row == null) {
                        row = templateSheet.createRow(i + 1);
                    }
                    ExcelUtils.setRowStyle(ExcelUtils.getRow(templateSheet, 1), row);
                }

                // 设置数据
                setValue(row.getCell(0), "");
                setValue(row.getCell(1), item.getProjectName());
                setValue(row.getCell(2), item.getOffice());
                setValue(row.getCell(3), item.getUserName());

                // every day
                List<WorkDayMeta> days = item.getWorkDayList();
                for (int day_index = 0; day_index < days.size(); day_index++) {
                    setValue(row.getCell(4 + day_index), days.get(day_index).getWorkDays());
                }

                templateSheet.setForceFormulaRecalculation(true);
            }

//            //删除备份的模板
//            for (int i = 1 ; i <= templateSheetSize ;i++) {
//                templatebook.removeSheetAt(0);
//            }
            // 生成文件
            templatebook.write(fos);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
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
