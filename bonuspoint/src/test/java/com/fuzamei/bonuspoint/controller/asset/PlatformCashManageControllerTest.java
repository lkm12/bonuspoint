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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class PlatformCashManageControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void checkRechargeCashRecord() throws Exception {
        this.mockMvc.perform(put("/bonus-point/asset/platform/recharge/check/556")
                .header("Authorization", "c6cc8956a0b75a90d7db752a47c76cac6f3ee7c62160a75882cb6cc8983c0fb379d80576707dda9a223160fc9a0fb41899360a3db3e0374089de03b24688e634f9d7e01fa2f87d354667d2856d3e8cc89386065d4ce048f11b840a28bda4ce2f7eb543b2a7486c0111dc7f230c58771f0baf91f1873dbdff53728b9d310873d84fa247a341c072d2add5ad0433efe637fe3297a86b3cb4176303acce0884a2e0231e904177a8900ac0b03bd1f0be400c&1"))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }

    @Test
    public void refuseRechargeCashRecord() throws Exception {
        PlatformCashRecordDTO platformCashRecordDTO = new PlatformCashRecordDTO();
        platformCashRecordDTO.setId(1L);
        platformCashRecordDTO.setReason("原因不详");
        log.info(JSON.toJSONString(platformCashRecordDTO));
        this.mockMvc.perform(put("/bonus-point/asset/platform/recharge/refuse")
                .header("Authorization", "Browser_17b335190903491a845913a387908e7a&1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(platformCashRecordDTO)))
                .andExpect(status().isOk()).andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
    }

}
