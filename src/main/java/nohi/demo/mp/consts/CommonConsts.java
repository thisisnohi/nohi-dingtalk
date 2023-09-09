package nohi.demo.mp.consts;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-06 17:16
 **/
public class CommonConsts {

    public enum YesOrNo implements DictEnum {
        // Y-是
        YES("Y", "是"),
        // N-否
        NO("N", "否");

        private String key;

        private String val;

        YesOrNo(String key, String val) {
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

    /**
     * 考勤类型
     */
    public enum CheckType implements DictEnum {
        // 上班
        ONDUTY("OnDuty", "上班"),
        // 下班
        OFFDUTY("OffDuty", "下班");

        private String key;

        private String val;

        CheckType(String key, String val) {
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

    /**
     * 打卡结果
     * when 'Normal' then '正常'
     * when 'Early' then '早退'
     * when 'Late' then '迟到'
     * when 'SeriousLate' then '严重迟到'
     * when 'Absenteeism' then '旷工迟到'
     * when 'NotSigned' then '未打卡'
     */
    public enum TimeResult implements DictEnum {
        // 正常
        NORMAL("Normal", "正常"),
        // 早退
        EARLY("Early", "早退"),
        // 迟到
        LATE("Late", "迟到"),
        // 严重迟到
        SERIOUSLATE("SeriousLate", "严重迟到"),
        // 旷工迟到
        ABSENTEEISM("Absenteeism", "旷工迟到"),
        // 未打卡
        NOTSIGNED("NotSigned", "未打卡"),
        ;

        private String key;

        private String val;

        TimeResult(String key, String val) {
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

    /**
     * 位置结果
     * when 'Normal' then '范围内'
     * when 'Outside' then '范围外'
     * when 'NotSigned' then '未打卡'
     */
    public enum LocationResult implements DictEnum {
        // 范围内
        NORMAL("Normal", "范围内"),
        // 范围外
        OUTSIDE("Outside", "范围外"),
        // 未打卡
        NOTSIGNED("NotSigned", "未打卡"),
        ;

        private String key;

        private String val;

        LocationResult(String key, String val) {
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

    /**
     * 数据来源
     * when 'USER' then '用户打卡'
     * when 'BOSS' then '老板改签'
     * when 'APPROVE' then '审批系统'
     * when 'SYSTEM' then '考勤系统'
     * when 'AUTO_CHECK' then '自动打卡'
     */
    public enum SourceType implements DictEnum {
        // 用户打卡
        USER("USER", "用户打卡"),
        // 老板改签
        BOSS("BOSS", "老板改签"),
        // 审批系统
        APPROVE("APPROVE", "审批系统"),
        // 考勤系统
        SYSTEM("SYSTEM", "考勤系统"),
        // 自动打卡
        AUTO_CHECK("AUTO_CHECK", "自动打卡"),
        ;

        private String key;

        private String val;

        SourceType(String key, String val) {
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

    /**
     * 审批状态
     * when 'NEW'then '新创建'
     * when 'RUNNING'then '审批中'
     * when 'TERMINATED'then '被终止'
     * when 'COMPLETED'then '完成'
     * when 'CANCELED'then '取消'
     */
    public enum AprStatus implements DictEnum {
        // 新创建
        NEW("NEW", "新创建"),
        // 审批中
        RUNNING("RUNNING", "审批中"),
        // 被终止
        TERMINATED("TERMINATED", "被终止"),
        // 完成
        COMPLETED("COMPLETED", "完成"),
        // 取消
        CANCELED("CANCELED", "取消"),
        ;

        private String key;

        private String val;

        AprStatus(String key, String val) {
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

    /**
     * 审批结果
     * when 'agree'then '同意'
     * when 'refuse'then '拒绝'
     */
    public enum AprResult implements DictEnum {
        // 同意
        AGREE("agree", "同意"),
        // 拒绝
        REFUSE("refuse", "拒绝"),
        ;

        private String key;

        private String val;

        AprResult(String key, String val) {
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


    /**
     * dt考勤数据字典
     */
    public enum DtNoonType implements DictEnum {
        // 上午
        AM("AM", "上午"),
        // 下午
        PM("PM", "下午"),
        ;

        private String key;

        private String val;

        DtNoonType(String key, String val) {
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
