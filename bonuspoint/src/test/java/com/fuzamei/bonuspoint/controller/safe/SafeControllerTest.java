package com.fuzamei.bonuspoint.controller.safe;

import com.fuzamei.bonuspoint.constant.CodeType;
import com.fuzamei.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @program: bonuspoint
 * @description: 测试安全模块
 * @author: WangJie
 * @create: 2018-04-18 11:09
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
@Slf4j
public class SafeControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private final static String authorizationName = "Authorization";
    private  static String authorizationValue = "Browser_17b335190903491a845913a387908e7a&1";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testGetSafeStatusInfo() throws Exception {
        this.mockMvc.perform(get("/bonus-point/safe/index")
                .header(authorizationName, authorizationValue))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }
    @Test
    public void testUpdatePassword()throws Exception{
        authorizationValue="token&343";
        Map<String,String> map = new HashMap<String,String>(8);
        map.put("oldPassword","123456");
        map.put("password","a123456");
        map.put("passwordRepeat","a123456");
        this.mockMvc.perform(put("/bonus-point/safe/password-edit")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testUpdatePayWord()throws Exception{
        authorizationValue="token&343";
        Map<String,String> map = new HashMap<String,String>(8);
        map.put("country","CN");
        map.put("mobile","17826873177");
        map.put("code","123456");
        map.put("payword","a123456");
        map.put("paywordRepeat","a123456");

        this.mockMvc.perform(put("/bonus-point/safe/payword-set")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testResetPassword()throws Exception{
        authorizationValue="token&343";
        Map<String,String> map = new HashMap<String,String>(8);
        map.put("mobile","17826873177");
        map.put("code","123456");
        map.put("password","a123456");
        map.put("passwordRepeat","a123456");
        map.put("username","WangJieTest");

        this.mockMvc.perform(put("/bonus-point/safe/password-reset")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }


    @Test
    public void testSetMobile() throws Exception{
        Map<String,String> map = new HashMap<String,String>(8);
        map.put("country","CN");
        map.put("mobile","17833333333");
        map.put("code","123456");


        this.mockMvc.perform(put("/bonus-point/safe/mobile-set")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testUpdateMobileStepOne() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>(8);
        authorizationValue="token&343";
        map.put("country","CN");
        map.put("mobile","17826873177");
        map.put("code","123456");

        map.put("type",CodeType.EDIT_MOBILE_STEP_ONE);

        this.mockMvc.perform(put("/bonus-point/safe/mobile-edit/verification-original-mobile-number")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testUpdateMobileStepTwo() throws  Exception{
        Map<String,String> map = new HashMap<String,String>(8);
        authorizationValue="token&343";
        map.put("country","CN");
        map.put("mobile","15957181403");
        map.put("code","123456");
        map.put("codeToken","daab590f-6a60-43b7-b592-621664332474");

        this.mockMvc.perform(put("/bonus-point/safe/mobile-edit/update-mobile-number")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }


    @Test
    public void testSetEmail() throws Exception{
        authorizationValue="token&343";
        Map<String,String> map = new HashMap<String,String>(8);
        map.put("email","1358726405@qq.com");
        map.put("code","123456");
        this.mockMvc.perform(put("/bonus-point/safe/email-set")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }



    @Test
    public void testUpdateEmailStepOne() throws Exception{
        authorizationValue="token&343";
        Map<String,String> map = new HashMap<String,String>(8);
        map.put("email","1358726405@qq.com");
        map.put("code","123456");
        this.mockMvc.perform(put("/bonus-point/safe/email-edit/verification-original-email")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    public void testUpdateEmailStepTwo() throws Exception{
        authorizationValue="token&343";
        Map<String,String> map = new HashMap<String,String>(8);
        map.put("email","paperpushsystem@163.com");
        map.put("code","123456");
        map.put("codeToken","0b8f7381-cbf3-439e-926e-d14f7b5097ee");


        this.mockMvc.perform(put("/bonus-point/safe/email-edit/update-email")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }
}
