package nohi.demo.common.tx;

import lombok.Data;

/**
 * <h3>nohi-dd-miniprogram-server</h3>
 *
 * @author NOHI
 * @description <p>page信息，不包含业务数据</p>
 * @date 2023/09/03 22:03
 **/
@Data
public class BasePage {
    /**
     * 每页记录数
     */
    private long pageSize;
    /**
     * 当前页
     */
    private long pageIndex;

    /**
     * 总页数
     */
    private long totalPages;

    /**
     * 总记录数
     */
    private long totalRecords;
}
