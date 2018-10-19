package com.fuzamei.bonuspoint.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: bonus-point-cloud
 * @description: 国际手机号校验
 * @author: WangJie
 * @create: 2018-05-11 15:31
 **/
@Slf4j
public class MobileUtil {
    /**
     * 国际手机号校验
     *
     * @param country 国家或地区 代码  如中国大陆 CN
     * @param mobile  手机号
     * @return
     * @author wangjie
     */
    public static boolean isRightMobile(String country, String mobile) throws NumberParseException {
        boolean isValid;

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber;
        phoneNumber = phoneNumberUtil.parse(mobile, country.toUpperCase());
        if (phoneNumberUtil.isValidNumber(phoneNumber)){
            return true;
        }
        return false;
    }
}
