package com.fuzamei.bonuspoint.util;

import java.util.Random;

/**
 * @program: bonus-point-cloud
 * @description: 验证码生成工具
 * @author: WangJie
 * @create: 2018-05-02 14:53
 **/
public class CaptchaUtil {

    /**
     * 生成6位数字验证码
     */
    public static String  getCode (){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int j;
        for (int i = 0 ; i < 6 ; i++){
            j = random.nextInt(1000);
            sb.append(j%10);
        }
        return sb.toString();
    }
}
