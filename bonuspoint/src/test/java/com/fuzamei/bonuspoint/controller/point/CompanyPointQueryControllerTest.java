package com.fuzamei.bonuspoint.controller.point;

import com.alibaba.fastjson.JSON;
import com.fuzamei.bonuspoint.entity.dto.point.CompanyPointDTO;
import com.fuzamei.bonuspoint.entity.dto.point.QueryPointSendDTO;
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
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/5/9 15:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CompanyPointQueryControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private final static String authorizationName = "Authorization";
    private static String authorizationValue = "token&1";

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getBalanceInfoRecord() throws Exception {
        this.mockMvc
                .perform(get("/bonus-point/point/company/balance-info/get").header("Authorization",
                        "Browser_17b335190903491a845913a387908e7a&1"))
                .andExpect(status().isOk()).andDo(print()) //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString(); //将相应的数据转换为字符串;
    }

    @Test
    public void listPointExchangeRecord() throws Exception {
        CompanyPointDTO companyPointDTO = new CompanyPointDTO();
        companyPointDTO.setCurrentPage(2);
        companyPointDTO.setPageSize(2);
        log.info(JSON.toJSONString(companyPointDTO));
        this.mockMvc
                .perform(post("/bonus-point/point/company/point-exchange/list")
                        .header("Authorization", "Browser_17b335190903491a845913a387908e7a&2")
                        .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJsonBytes(companyPointDTO)))
                .andExpect(status().isOk()).andDo(print()) //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString(); //将相应的数据转换为字符串;
    }

    @Test
    public void grantPointList() throws Exception {
        log.info(JSON.toJSONString("获取集团已发放积分"));
        authorizationValue="bb&242";

        Map<String,Object> map = new HashMap<>();
        this.mockMvc
                .perform(post("/bonus-point/point/company/grant-list")
                        .header(authorizationName, authorizationValue)
                        .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJsonString(map)))
                .andExpect(status().isOk()).andDo(print()) //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString(); //将相应的数据转换为字符串;
    }

    @Test
    public void queryCompanyPointAsset() throws Exception {
        log.info(JSON.toJSONString("获取集团资产"));
        this.mockMvc
                .perform(get("/bonus-point/point/company/asset").header("Authorization",
                        "Browser_17b335190903491a845913a387908e7a&1"))
                .andExpect(status().isOk()).andDo(print()) //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString(); //将相应的数据转换为字符串;
    }

    @Test
    public  void listActivity() throws Exception {
        log.info("测试获取集团进行的活动");
        this.mockMvc
                .perform(get("/bonus-point/point/company/activities")
                        .header("Authorization", "Browser_17b335190903491a845913a387908e7a&1"))
                .andExpect(status().isOk()).andDo(print()) //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString(); //将相应的数据转换为字符串;
    }

}
