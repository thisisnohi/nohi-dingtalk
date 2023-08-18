package nohi.demo.mp.consts;

import com.dingtalk.api.DingTalkConstants;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-06 17:16
 **/
public class DingTalkConsts extends DingTalkConstants {
    // 获取TOKEN
    public static final String API_GETTOKEN = "/gettoken";
    // 获取流程实例
    public static final String API_GET_PROCESS_INSTANCE = "/topapi/processinstance/get";
    // 部门列表
    public static final String API_DEPARTMENT_LIST = "/department/list";
    // 用户列表
    public static final String API_USER_LIST = "/topapi/v2/user/list";

    // 获取考勤结果列表
    public static final String API_ATTENDANCE_LIST = "/attendance/list";
    // 获取考勤详情
    public static final String API_ATTENDANCE_LIST_RECORD = "/attendance/listRecord";
    // 获取假期类型
    public static final String API_ATTENDANCE_GET_VOCATION_TYPE_ = "/topapi/attendance/vacation/type/list";

    // 获取考勤组
    public static final String API_ATTENDANCE_GET_SIMPLE_GROUPS = "/topapi/attendance/getsimplegroups";

    public static final String API_ATTENDANCE_GET_LEAVES_STATUS = "/topapi/attendance/getleavestatus";


    public enum RespCode implements DictEnum{
        SUC("0","成功"),
        ;

        private String key;

        private String val;

        RespCode(String key, String val) {
            this.key = key;
            this.val = val;
        }

        @Override
        public String getName() {
            return this.getClass().getName();
        }
        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getVal() {
            return this.val;
        }
    }
}
