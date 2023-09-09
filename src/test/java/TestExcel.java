import lombok.extern.slf4j.Slf4j;
import nohi.demo.mp.utils.FileUtils;
import nohi.demo.mp.utils.IdUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-18 13:35
 **/
@Slf4j
public class TestExcel {

    @Test
    public void test1() {
        String filename = IdUtils.timeFlow() + ".xlsx";
        String template = "template/project_emp_worksheet.xlsx";

        template = "template/project_worksheet.xlsx";

        // template/emps_worksheet.xlsx outputFile:/Users/nohi/tmp/nohi-dingtalk/file/20230909205921904522.xlsx
        String outputFile = "/Users/nohi/tmp/nohi-dingtalk/file/20230909205921904522.xlsx";
        try {
            FileUtils.createDir(outputFile);
        } catch (Exception e) {
            log.error("创建目录[{}]异常:{}", outputFile, e.getMessage(), e);
        }
        log.info("template:{} outputFile:{}", template, outputFile);

        //2,读取Excel模板
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(template);
             FileOutputStream fos = new FileOutputStream(outputFile);
        ) {
            // 读取Excel模板
            Workbook templatebook = WorkbookFactory.create(is);
//            Workbook templatebook = new XSSFWorkbook(is);//此代码运行ok
            Sheet templateSheet = templatebook.getSheetAt(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
