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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-04 18:54
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class PlatformPointQueryControllerTest {


    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final static String authorizationName = "Authorization";
    private  static String authorizationValue = "token&1";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    /**
     *  集团查看发行记录
     * @throws Exception
     */
    @Test
    public void companyGetIssuePointListTest() throws Exception {
        Map<String, Object> map = new HashMap<>();
        authorizationValue="token&343";
        map.put("name", "test");
      //  map.put("status", 0);
        this.mockMvc.perform(post("/bonus-point/point/company/point-issue-list")
                .header(authorizationName, authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 平台查看积分审批记录
     * @throws Exception
     */
    @Test
    public void platformGetIssuePointListTest() throws Exception {
        Map<String, Object> map = new HashMap<>();
       // map.put("fuzzyMatch", "test");
        this.mockMvc.perform(post("/bonus-point/point/platform/point-issue-list")
                .header(authorizationName, authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();

    }

    /**
     * 平台查看发放积分记录
     * @throws Exception
     */
    @Test
    public void grantGeneralPointListTest()throws  Exception{
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", "178");
        map.put("startTime","1530705282190");
        map.put("endTime","1530705494308");
        this.mockMvc.perform(post("/bonus-point/point/platform/point-grant-list")
                .header(authorizationName, authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }


}
