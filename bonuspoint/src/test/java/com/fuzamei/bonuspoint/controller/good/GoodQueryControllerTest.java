package com.fuzamei.bonuspoint.controller.good;

import com.fuzamei.bonuspoint.controller.BaseControllerTest;
import com.fuzamei.bonuspoint.entity.dto.good.GoodExchangeDTO;
import com.fuzamei.bonuspoint.entity.dto.good.QueryGoodDTO;
import com.fuzamei.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * test
 * @author liumeng
 * @create 2018年5月15日
 */

@Transactional
@Slf4j
public class GoodQueryControllerTest extends BaseControllerTest {

    @Test
    public void getGood() throws Exception {
        log.info("获取指定商品");
        this.mockMvc.perform(get("/bonus-point/good/get/1"))
                .andExpect(status().isOk()).andDo(print()).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void queryGood() throws Exception {
        log.info("获取指定商品");
        QueryGoodDTO queryGoodDTO = new QueryGoodDTO();
        queryGoodDTO.setStatus("1");
        queryGoodDTO.setPid(0L);
        log.info(JsonUtil.toJsonString(queryGoodDTO));
        this.mockMvc
                .perform(post("/bonus-point/good/query")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJsonString(queryGoodDTO)))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void companyQueryGood() throws Exception {
        log.info("集团查询本集团商品");
        QueryGoodDTO queryGoodDTO = new QueryGoodDTO();
        queryGoodDTO.setGoodName("100");
        log.info(JsonUtil.toJsonString(queryGoodDTO));
        this.mockMvc
                .perform(post("/bonus-point/good/company/query").header(authorizationName, authorizationValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJsonString(queryGoodDTO)))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }


    @Test
    public void queryGoodExchanges() throws Exception {
        log.info("测试商品兑换信息");

        GoodExchangeDTO goodExchangeDTO = new GoodExchangeDTO();
        goodExchangeDTO.setPlatformId(1L);
//        goodExchangeDTO.setBegin(10000000L);
//        goodExchangeDTO.setCompanyName("复杂");
//        goodExchangeDTO.setCurrentPage(1);
//        goodExchangeDTO.setPageSize(5);
//        goodExchangeDTO.setGoodName("java");
//        goodExchangeDTO.setMobile("15158196253");
//        goodExchangeDTO.setEnd(System.currentTimeMillis());
//        goodExchangeDTO.setSubTypeId(1L);
//        goodExchangeDTO.setTypeId(1L);
        log.info(JsonUtil.toJsonString(goodExchangeDTO));
        this.mockMvc
                .perform(post("/bonus-point/good/exchanges").header(authorizationName, authorizationValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJsonString(goodExchangeDTO)))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void appshow() throws Exception {
        log.info("测试商品首页");
        this.mockMvc.perform(get("/bonus-point/good/appshow?size=10&pid=2"))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void appTypeShow() throws Exception {
        log.info("测试分类预览商品");
        this.mockMvc.perform(get("/bonus-point/good/typeshow?pid=0 "))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void previewCompany() throws Exception {
        log.info("测试预览商家品牌");
        this.mockMvc.perform(get("/bonus-point/good/companyshow?pid=0"))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

}
