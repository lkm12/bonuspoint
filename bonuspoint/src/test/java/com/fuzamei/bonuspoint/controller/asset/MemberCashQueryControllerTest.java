package com.fuzamei.bonuspoint.controller.asset;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MemberCashQueryControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getMemberCashRecord() throws Exception {
        this.mockMvc.perform(get("/bonus-point/asset/member/member-cash/get")
                .header("Authorization", "d0789ad66a0d10510234f650aff3b1fa5d0c500147a9482d00366ed35287008a23de4f58ec87faf4216b78c041277f883821df4b4c22ec0b731f27c0698a829b3c2a13d9aa6876951fb23afcf3c824393b531b54d3fbbfc49a8904c0ed77eef6d8e3ab5adc80620571c65cf1b1f5a46ce34249a050961551bbd64694d6a62e18bfe0817bc47f957297cf876afd41bda974ac257bbd0e9c30e1393807eaf782597f559faaa1739a05cff15f3308fce4e1"))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }

    @Test
    public void listMemberPointCashRecord() throws Exception {
        this.mockMvc.perform(get("/bonus-point/asset/member/member-point/list")
                .header("Authorization", "Browser_17b335190903491a845913a387908e7a&1"))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }

    @Test
    public void getMemberPointCashRecordDetail() throws Exception {
        this.mockMvc.perform(get("/bonus-point/asset/member/member-point-detail/get")
                .header("Authorization", "Browser_17b335190903491a845913a387908e7a&1"))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }
}
