import com.dingtalk.api.response.OapiAttendanceListResponse;
import nohi.demo.mp.dt.entity.jpa.DtKqInfo;
import nohi.demo.mp.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-16 17:26
 **/
public class TestBeanCopy {

    @Test
    public void testBeanCopy() {
        OapiAttendanceListResponse.Recordresult item = new OapiAttendanceListResponse.Recordresult();
        item.setBaseCheckTime(new Date());
        item.setCheckType("CT");
        item.setGroupId(11111L);
        item.setId(1L);
        item.setPlanId(2L);

        DtKqInfo info = new DtKqInfo();
        JsonUtils.copyProperties(item, info);

        System.out.println("info:" + JsonUtils.toJson(info));
    }
}
