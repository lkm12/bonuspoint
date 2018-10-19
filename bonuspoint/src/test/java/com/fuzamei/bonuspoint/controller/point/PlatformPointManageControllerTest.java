package com.fuzamei.bonuspoint.controller.point;

import com.alibaba.fastjson.JSON;
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
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-05-08 19:15
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
//@Transactional
public class PlatformPointManageControllerTest {


    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final static String authorizationName = "Authorization";
    private static String authorizationValue = "token&1";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    @Test
    public void sendGeneralPointTest() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("toId", "399");
        map.put("num", "20");
        map.put("memo", "王杰 7/4 19:48");
        map.put("payword", "123456");
        this.mockMvc.perform(post("/bonus-point/point/platform/sendPoint")
                .header(authorizationName, authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    public  void  reviewPoint () throws Exception  {
        this.mockMvc.perform(get("/bonus-point/point/platform/applyrecord/58/review")
                .header(authorizationName, authorizationValue))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }



    @Test
    public void refusePoint() throws Exception {
        this.mockMvc.perform(get("/bonus-point/point/platform/applyrecord/59/refuse?reason=test")
                .header(authorizationName, authorizationValue))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }


}
