import nohi.demo.mp.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-15 13:25
 **/
public class TestDate {

    @Test
    public void test() {
        Date s1 = DateUtils.parseDate("2021-01-01", DateUtils.HYPHEN_DATE);
        System.out.println(s1.getTime());

        s1 = DateUtils.parseDate("2021-01-30", DateUtils.HYPHEN_DATE);
        System.out.println(s1.getTime());
    }

    @Test
    public void test1() {
        Date s1 = DateUtils.parseDate("2021-01-01 08:01:01", DateUtils.HYPHEN_TIME);
        Date s2 = DateUtils.parseDate("2021-01-11 23:59:59", DateUtils.HYPHEN_TIME);
        // 转换日期
        LocalDate start = DateUtils.date2LocalDate(s1);
        LocalDate end = DateUtils.date2LocalDate(s2);
        // 2021-01-03 08:00:00 YYYYMMddHHmmssSSS
        DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("YYYY-MM-dd");
        while(!end.isBefore(start)) {
            String workDateFromStr = start.format(dateformat) + " 00:00:00";
            String workDateToStr = null;
            start = start.plusDays(6);
            if (start.isBefore(end)) {
                workDateToStr = start.format(dateformat);
            } else {
                workDateToStr = end.format(dateformat);
            }
            start = start.plusDays(1);
            workDateToStr += " 23:59:59";

            System.out.println(String.format("[%s]-[%s]", workDateFromStr, workDateToStr));
        }
    }

    @Test
    public void testNewDate(){
        Date date = new Date(1692158400000L);
        System.out.println(date);
    }
}
