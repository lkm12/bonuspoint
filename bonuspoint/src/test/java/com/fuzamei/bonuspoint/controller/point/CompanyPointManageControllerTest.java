package com.fuzamei.bonuspoint.controller.point;

import com.alibaba.fastjson.JSON;
import com.fuzamei.bonuspoint.entity.dto.point.ApplyPointDTO;
import com.fuzamei.bonuspoint.entity.dto.point.CompanyPointDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/5/7 15:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class CompanyPointManageControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void sendPointToUser() throws Exception {
        CompanyPointDTO companyPointDTO = new CompanyPointDTO();
        companyPointDTO.setPointId(69L);
        companyPointDTO.setMobile("15957180279");
        companyPointDTO.setNum(new BigDecimal(20));
        companyPointDTO.setMemo("测试");
        companyPointDTO.setPayword("123456");
        log.info(JSON.toJSONString(companyPointDTO));
        this.mockMvc.perform(post("/bonus-point/point/company/send?lang=en_US")
                .header("Authorization", "Browser_17b335190903491a845913a387908e7a&1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(companyPointDTO)))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }

    @Test
    public void balanceCommonPoint() throws Exception {
        CompanyPointDTO companyPointDTO = new CompanyPointDTO();
        companyPointDTO.setPayword("123456");
        companyPointDTO.setNum(new BigDecimal(10));
        log.info(JSON.toJSONString(companyPointDTO));
        this.mockMvc.perform(post("/bonus-point/point/company/balance")
                .header("Authorization", "3fa6a66c3799157132de1dbbda6e261d02ff98f4f06db828e3fa9c70c1b85079d9b2694c418409d97d4231b4ccced906516609c7aee15d4374118f20ff654fb79218dc882081e9b1567e7f2ba3f8b74ba774a38d27884de86d3757136cefce0b1934d2a4c07f20edcd0324e53db7973886aa8b429e2a7226fc747d950c5125694e4c1932f9ad947091bb7fca6d3990c430591f1eaa5a0dca4ca09c5a63e3afe1aaa8550471ef7e313b308f4285c2be897a5d77a301021233")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(companyPointDTO)))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }


    @Test
    public void releasePoint() throws Exception {
        ApplyPointDTO applyPointDTO = new ApplyPointDTO();
        applyPointDTO.setPointName("test");
        applyPointDTO.setMemo("测试");
        applyPointDTO.setPayWord("123456");
        applyPointDTO.setNum(new BigDecimal(100));
        log.info(JSON.toJSONString(applyPointDTO));
        this.mockMvc.perform(post("/bonus-point/point/company/apply")
                .header("Authorization", "Browser_bf3a9725-85e6-4236-9e7e-23837218c5f2&242")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(applyPointDTO)))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }
}
