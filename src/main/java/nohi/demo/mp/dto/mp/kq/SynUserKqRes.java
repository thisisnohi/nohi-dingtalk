package nohi.demo.mp.dto.mp.kq;

import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiAttendanceListResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-16 16:50
 **/
@Data
@Builder
public class SynUserKqRes {
    private boolean suc;
    private String msg;
    private CountDownLatch countDownLatch;
    List<String> userIdlist;
    private List<OapiAttendanceListResponse.Recordresult> kqList;
    private List<OapiAttendanceListRecordResponse.Recordresult> kqDetailList;
}
