package com.fuzamei.bonuspoint.controller.asset;

import com.alibaba.fastjson.JSON;
import com.fuzamei.bonuspoint.entity.dto.asset.PlatformCashRecordDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class PlatformCashQueryControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void listMemberCashRecord() throws Exception {
        PlatformCashRecordDTO platformCashRecordDTO = new PlatformCashRecordDTO();
        platformCashRecordDTO.setCurrentPage(1);
        platformCashRecordDTO.setPageSize(5);
        platformCashRecordDTO.setMobile("15957180382");
        log.info(JSON.toJSONString(platformCashRecordDTO));
        this.mockMvc.perform(post("/bonus-point/asset/platform/member-cash/list")
                .header("Authorization", "Browser_17b335190903491a845913a387908e7a&1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(platformCashRecordDTO)))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }

    @Test
    public void listCompanyCashRecord() throws Exception {
        PlatformCashRecordDTO platformCashRecordDTO = new PlatformCashRecordDTO();
        platformCashRecordDTO.setUid(1L);
        log.info(JSON.toJSONString(platformCashRecordDTO));
        this.mockMvc.perform(post("/bonus-point/asset/platform/company-cash/list")
                .header("Authorization", "Browser_17b335190903491a845913a387908e7a&1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(platformCashRecordDTO)))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }

    @Test
    public void getRechargeCashRecord() throws Exception {
        PlatformCashRecordDTO platformCashRecordDTO = new PlatformCashRecordDTO();
        platformCashRecordDTO.setCompanyName("复杂美");
        log.info(JSON.toJSONString(platformCashRecordDTO));
        this.mockMvc.perform(post("/bonus-point/asset/platform/recharge/list")
                .header("Authorization", "e654ac4f405e5b0d7ce3e00f2abc6c1658f946c31fa17a7293c405bded0b5beb128c1510aa031cfbbdedc8dac9d45bfe18e9bdf82508cad4695b3bd7d1a9598f0c879aa5c1d2bbf2dfa938c0223fa5a5f690a316c9c1932f8630df465a923fb077199bd7deeee94b59fbbf2cb8208829ded31526aa74ba33396123b3118936c7e9e8d551c1dbca6d1e38674e9dd62598c01564a4b1935e78781880f4b3487f1c408c616e89fc9304714ab81eb5508a93")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(platformCashRecordDTO)))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }

    @Test
    public void getWithdrawCashRecord() throws Exception {
        PlatformCashRecordDTO platformCashRecordDTO = new PlatformCashRecordDTO();
        log.info(JSON.toJSONString(platformCashRecordDTO));
        this.mockMvc.perform(post("/bonus-point/asset/platform/withdraw/list")
                .header("Authorization", "e654ac4f405e5b0d7ce3e00f2abc6c1658f946c31fa17a7293c405bded0b5beb128c1510aa031cfbbdedc8dac9d45bfe18e9bdf82508cad4695b3bd7d1a9598f0c879aa5c1d2bbf2dfa938c0223fa5a5f690a316c9c1932f8630df465a923fb077199bd7deeee94b59fbbf2cb8208829ded31526aa74ba33396123b3118936c7e9e8d551c1dbca6d1e38674e9dd62598c01564a4b1935e78781880f4b3487f1c408c616e89fc9304714ab81eb5508a93")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(platformCashRecordDTO)))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }

    @Test
    public void listRechargeCashRecord() throws Exception {
        PlatformCashRecordDTO platformCashRecordDTO = new PlatformCashRecordDTO();
        platformCashRecordDTO.setCurrentPage(6);
        platformCashRecordDTO.setPageSize(3);
        log.info(JSON.toJSONString(platformCashRecordDTO));
        this.mockMvc.perform(get("/bonus-point/asset/platform/recharge/list")
                .header("Authorization", "Browser_17b335190903491a845913a387908e7a&1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(platformCashRecordDTO)))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }

    @Test
    public void getPlatformCashRecord() throws Exception {
        this.mockMvc.perform(get("/bonus-point/asset/platform/platform-cash/get")
                .header("Authorization", "Browser_17b335190903491a845913a387908e7a&1"))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }

    @Test
    public void listCompany() throws Exception {
        PlatformCashRecordDTO platformCashRecordDTO = new PlatformCashRecordDTO();
        log.info(JSON.toJSONString(platformCashRecordDTO));
        this.mockMvc.perform(get("/bonus-point/asset/platform/company/list")
                .header("Authorization", "Browser_17b335190903491a845913a387908e7a&1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(platformCashRecordDTO)))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }

    @Test
    public void listProvisionsNotice() throws Exception {
        PlatformCashRecordDTO platformCashRecordDTO = new PlatformCashRecordDTO();
        log.info(JSON.toJSONString(platformCashRecordDTO));
        this.mockMvc.perform(get("/bonus-point/asset/platform/provisions-notice/list")
                .header("Authorization", "Browser_17b335190903491a845913a387908e7a&1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(platformCashRecordDTO)))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }

    @Test
    public void listMemberPointCashRecord() throws Exception {
        PlatformCashRecordDTO platformCashRecordDTO = new PlatformCashRecordDTO();
        platformCashRecordDTO.setUid(1L);
        log.info(JSON.toJSONString(platformCashRecordDTO));
        this.mockMvc.perform(get("/bonus-point/asset/platform/member-point/list")
                .header("Authorization", "Browser_17b335190903491a845913a387908e7a&1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(platformCashRecordDTO)))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }

    @Test
    public void getMemberPointCashRecordDetail() throws Exception {
        PlatformCashRecordDTO platformCashRecordDTO = new PlatformCashRecordDTO();
        platformCashRecordDTO.setUid(1L);
        log.info(JSON.toJSONString(platformCashRecordDTO));
        this.mockMvc.perform(get("/bonus-point/asset/platform/member-point-detail/get")
                .header("Authorization", "Browser_17b335190903491a845913a387908e7a&1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(platformCashRecordDTO)))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }

    @Test
    public void listCompanyRateCashRecord() throws Exception {
        PlatformCashRecordDTO platformCashRecordDTO = new PlatformCashRecordDTO();
        platformCashRecordDTO.setUid(1L);
        log.info(JSON.toJSONString(platformCashRecordDTO));
        this.mockMvc.perform(get("/bonus-point/asset/platform/company-rate/list")
                .header("Authorization", "Browser_17b335190903491a845913a387908e7a&1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(platformCashRecordDTO)))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }
}
