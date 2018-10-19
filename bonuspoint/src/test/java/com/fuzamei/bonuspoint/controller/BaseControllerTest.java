package com.fuzamei.bonuspoint.controller;


import com.fuzamei.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author lmm 测试基本类
 * @description
 * @create 2018/7/17 10:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BaseControllerTest {
    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    protected final String authorizationName = "Authorization";

    protected String authorizationValue = "token&1";

    protected String username = "lceshi";

    protected String password = "a123456";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        login();
    }
    /**
     * 登陆平台
     * @return
     */

    private void login() throws RuntimeException {
        Map<String,String> map = new HashMap<>(4);
        map.put("username",username);
        map.put("password",password);
        map.put("mark","123456");
        map.put("role","1");
        try {
            log.info(JsonUtil.toJsonString(map));
            String result =this.mockMvc.perform(post("/bonus-point/browser-login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJsonString(map))).andReturn().getResponse().getContentAsString();
            log.info(result);
            reslove(result);
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    /**
     * 解析参数
     * @param result
     */
    public void reslove(String result) throws  RuntimeException {

        boolean success= result.contains("\"success\":\"true\"");
        if (!success){
            throw  new RuntimeException();
        }
        int last = result.lastIndexOf("\"");
        this.authorizationValue = result.substring((result.substring(0,last).lastIndexOf("\"")+1),result.lastIndexOf("\""));
        log.info("token ： " + this.authorizationValue + "\n");
    }

}
