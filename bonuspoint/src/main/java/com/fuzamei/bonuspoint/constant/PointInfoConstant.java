/**
 * FileName: PointInfoConstant
 * Author: 王涛
 * Date: 2018/4/25 18:01
 * Description:
 */
package com.fuzamei.bonuspoint.constant;

import java.util.regex.Pattern;

/**
 *
 *
 * @author wangtao
 * @create 2018/4/25
 *
 */

public class PointInfoConstant {

    private PointInfoConstant() {
        throw new AssertionError("不能实例化 PointInfoConstant");
    }

    public final static int ONE = 1;
    public final static int TWO = 2;
    public final static int THREE = 3;
    /** 积分入账*/
    public final static int POINT_IN = 1;
    /** 积分入账 string*/
    public final static String  POINT_IN_STR = "积分入账";

    /** 积分出账*/
    public final static int POINT_OUT = 2;
    /** 积分出账 string*/
    public final static String POINT_OUT_STR = "积分出账";

    /** 平台积分*/
    public final static int GENERAL_POINT = 2;
    /** 平台积分 string*/
    public final static String GENERAL_POINT_STR = "通用积分";

    /** 集团积分*/
    public final static int COMPANY_POINT = 1;
    /** 集团积分 string*/
    public final static String COMPANY_POINT_STR = "商户积分";

    /** 大平台积分*/
    public final static int SUPER_POINT = 3;
    /** 大平台积分 string*/
    public final static String SUPER_POINT_STR = "大平台积分";

    public static Pattern NUMBER_PATTERN = Pattern.compile("^[1-9]\\d*$");

    /** 待审核 */
    public final static int CHECK_PENDING = 0;
    /** 审核通过 */
    public final static int CHECK_PASS = 1;
    /** 审核没通过 */
    public final static int CHECK_NOT_PASS = 2;
    /** 积分已过期*/
    public final static int point_out_time = 3;

    /** 有有效期*/
    public static final int LIFE_HAVING =1;
    /** 无有效期*/
    public static final int LIFE_NOT_HAVING=0;
    /** 待审核*/
    public static final int STATUS_TODO_CHECK =0;
    /** 已审核*/
    public static final int STATUS_CHECKED =1;
    /** 已经拒绝*/
    public static final int STATUS_REFUSE=2;
    /** 已经过期*/
    public static final int STATUS_EXPIRED=3;

}
