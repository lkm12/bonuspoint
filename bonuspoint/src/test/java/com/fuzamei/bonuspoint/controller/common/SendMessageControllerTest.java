package com.fuzamei.bonuspoint.controller.common;

import com.fuzamei.bonuspoint.constant.CodeType;
import com.fuzamei.bonuspoint.util.SendEmailUtil;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-05-11 14:34
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class SendMessageControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private final static String authorizationName = "Authorization";
    private  static String authorizationValue = "Browser_token&1";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }


    /**
     * 重置密码
     * @throws Exception
     */
    @Test
    public void testResetPassword()throws Exception{
        authorizationValue="token&343";
        Map<String,Object>map = new HashMap<>(4);
        map.put("mobile","1782687317");
        map.put("type",CodeType.RESET_PASSWORD);
        this.mockMvc.perform(post("/bonus-point/message/reset/send-SMS-verification-code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }


    /**
     * 设置交易密码 发送验证码
     * @throws Exception
     */
    @Test
    public void testSetPywordSendSMS()throws Exception{
        authorizationValue="token&343";
        Map<String,Object>map = new HashMap<>(4);
        map.put("mobile","17826873177");
        map.put("type",CodeType.SET_PAYWORD);
        this.mockMvc.perform(post("/bonus-point/message/send-SMS-verification-code")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }
    /**
     * 修改手机号 第一步验证原手机号 发送验证码
     * @throws Exception
     */
    @Test
    public void testSendMobileOrEmailMessageOne()throws Exception{
        authorizationValue="token&343";
        Map<String,Object>map = new HashMap<>(4);
        map.put("mobile","17826873177");
        map.put("type",CodeType.EDIT_MOBILE_STEP_ONE);
        this.mockMvc.perform(post("/bonus-point/message/send-SMS-verification-code")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();

    }
    /**
     * 修改手机号 第二步更新手机号 发送验证码
     * @throws Exception
     */
    @Test
    public void testSendMobileOrEmailMessageTwo()throws Exception{
        Map<String,Object>map = new HashMap<>(4);
        authorizationValue="token&343";
        map.put("mobile","15957181403");
        map.put("type",CodeType.EDIT_MOBILE_STEP_TWO);
        this.mockMvc.perform(post("/bonus-point/message/send-SMS-verification-code")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();

    }


    /**
     * 绑定邮箱
     * @throws Exception
     */
    @Test
    public void testSendEmailMessage()throws Exception{
        Map<String,Object>map = new HashMap<>(4);
        authorizationValue="token&343";
        map.put("email","1358726405@qq.com");
        map.put("type",CodeType.BOUND_EMAIL);
        this.mockMvc.perform(post("/bonus-point/message/send-email-verification-code")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();

    }



    /**
     * 修改邮箱 第一步验证原邮箱 发送验证码
     * @throws Exception
     */
    @Test
    public void testSendEmailMessageOne()throws Exception{
        authorizationValue="token&343";
        Map<String,Object>map = new HashMap<>(4);
        map.put("email","paperpushsystem@163.com");
        map.put("type",CodeType.EDIT_EMAIL_STEP_ONE);
        this.mockMvc.perform(post("/bonus-point/message/send-email-verification-code")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();

    }
    /**
     * 修改手机号 第二步更新邮箱 发送验证码
     * @throws Exception
     */
    @Test
    public void testSendEmailMessageTwo()throws Exception{
        Map<String,Object>map = new HashMap<>(4);
        authorizationValue="token&343";
        map.put("email","paperpushsystem@163.com");
        map.put("type",CodeType.EDIT_EMAIL_STEP_TWO);
        this.mockMvc.perform(post("/bonus-point/message/send-email-verification-code")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();

    }

    /**
     * 用户注册发送验证码
     * @throws Exception
     */
    @Test
    public void testSendMobileCodeForRegister()throws Exception{
        Map<String,Object>map = new HashMap<>(4);
        map.put("mobile","17826873177");
        map.put("country","CN");
        map.put("type",CodeType.REGISETER);
        this.mockMvc.perform(post("/bonus-point/message/register/send-SMS-verification-code")

                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();

    }
}
