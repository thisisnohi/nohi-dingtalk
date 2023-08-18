package nohi.demo.mp.utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-06 21:07
 **/
public class IdUtils {
    public static String uuid32(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 随机数
     * @param length
     * @return
     */
    public static String radomLength(int length) {
        SecureRandom random = new SecureRandom();
        String msg = "";
        for (int i = 0; i < length; i++) {
            msg += random.nextInt(10);
        }
        return msg;
    }

}
