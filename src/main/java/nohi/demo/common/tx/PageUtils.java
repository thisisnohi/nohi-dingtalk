package nohi.demo.common.tx;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.formula.functions.T;

/**
 * <h3>nohi-dd-miniprogram-server</h3>
 *
 * @author NOHI
 * @description <p>分页工具类</p>
 * @date 2023/09/06 15:33
 **/
public class PageUtils {
    /**
     * 默认页索引
     */
    private static final int PAGE_INDEX = 1;
    /**
     * 默认每页记录数
     */
    private static final int PAGE_SIZE= 10;
    /**
     * 根据 basePage,转换为Mybatis分页
     * @param basePage 分页数据
     * @return Mybatis分页信息
     */
    public static <T> Page<T> buildPage(BasePage basePage, Class<T> returnClass) {
        if (null != basePage && basePage.getPageIndex() >= 0 && basePage.getPageSize() > 0) {
            return Page.of(basePage.getPageIndex(), basePage.getPageSize());
        } else {
            return null;
        }
    }


    /**
     * 根据返回对象
     * @param basePage 原始请求对象
     * @param page  分页结果
     * @return 返回分页数据
     */
    public static BasePage buildPage(BasePage basePage, Page page) {
        if (null == page) {
            page = Page.of(PAGE_INDEX, PAGE_SIZE);
        }

        if (null == basePage) {
            basePage = new BasePage();
        }
        basePage.setPageSize(page.getSize());
        basePage.setPageIndex(page.getCurrent());
        basePage.setTotalPages(page.getPages());
        basePage.setTotalRecords(page.getTotal());
        return basePage;
    }

}
