package com.fuzamei.bonuspoint.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-05-17 14:52
 **/
@Slf4j
public class NumberCheckUtilTest {
    @Test
    public void testPaywordCheck(){
        String payword ="12345.6";
        boolean result = NumberCheck.checkPayword(payword);
        System.out.println("\n result= "+result);

    }
}
