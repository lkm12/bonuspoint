package com.fuzamei.bonuspoint.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: bonus-point-cloud
 * @description: 备付金，积分兑换比例校验
 * @author: WangJie
 * @create: 2018-04-26 16:20
 **/
public class NumberCheck {
    /**
     * 数值判断是否为[0,100]之间，最多两位小数
     * @author wangjie
     * @param cashRate Float 备付金比例
     * @return
     */
    public static boolean checkCashRate(Float cashRate){
        if(cashRate>100 || cashRate<0){
            return false;
        }
        String str = cashRate.toString();
        int index = str.indexOf(".");
        if(index>0){
            int l = str.length()-1-index;
            if(l>2){
                return false;
            }
        }
        return true;
    }


    /**
     * 数值判断，值是否大于等于0，且最多两位小数
     * @author wangjie
     * @param pointRate
     * @return
     */
    public static boolean checkPointRate(Float pointRate){
        if(pointRate<=0){
            return false;
        }
        String str = pointRate.toString();
        int index = str.indexOf(".");
        if(index>0){
            int l = str.length()-1-index;
            if(l>2){
                return false;
            }
        }
        return true;
    }

    /**
     * 6位数支付密码校验
     * @param payword
     * @return
     */
    public static boolean checkPayword(String payword){
        if (payword == null || payword.length()!=6){
            return false;
        }
        try{
            Integer number = Integer.parseInt(payword);

        }catch (Exception e){
            return false;
        }
        return true;
    /*    String reg ="/^\\d{6}$/";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(payword);
        return m.matches();*/
    }
}
