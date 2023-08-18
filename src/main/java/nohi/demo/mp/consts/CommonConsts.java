package nohi.demo.mp.consts;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-06 17:16
 **/
public class CommonConsts {

    public enum YesOrNo implements DictEnum{
        // Y-是
        YES("Y","是"),
        // N-否
        NO("N","否");

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
}
