package com.fuzamei.bonuspoint.controller.good;

import com.fuzamei.bonuspoint.controller.BaseControllerTest;
import com.fuzamei.bonuspoint.entity.dto.good.GoodOrderDTO;
import com.fuzamei.bonuspoint.entity.dto.good.GoodPayDTO;
import com.fuzamei.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * test
 * @author liumeng
 * @create 2018年5月16日
 */

@Transactional
@Slf4j
public class GoodOrderManageControllerTest extends BaseControllerTest {

    @Test
    public void orderGood() throws Exception {
        log.info("订购商品");
        GoodOrderDTO goodOrderDTO = new GoodOrderDTO();
        goodOrderDTO.setGoodId((long) 1);
        goodOrderDTO.setNum(10);
        goodOrderDTO.setAddressId(1L);
        goodOrderDTO.setDistribution("distribution");
        log.info(JsonUtil.toJsonString(goodOrderDTO));
        this.mockMvc
                .perform(post("/bonus-point/good/order").header(authorizationName, authorizationValue)
                        .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJsonString(goodOrderDTO)))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void cancelOrder() throws Exception {
        log.info("取消订单");
        this.mockMvc.perform(get("/bonus-point/good/order/cancel?id=2").header(authorizationName, authorizationValue))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void payOrder() throws Exception {
        log.info("支付订单");
        GoodPayDTO goodPayDTO = new GoodPayDTO();
        goodPayDTO.setPayModel(2);
        goodPayDTO.setPayword("123456");
        log.info(JsonUtil.toJsonString(goodPayDTO));
        this.mockMvc
                .perform(post("/bonus-point/good/order/pay/2").header(authorizationName, authorizationValue)
                        .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJsonString(goodPayDTO)))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void sendGood() throws Exception {
        log.info("商家发货");
        this.mockMvc
                .perform(get("/bonus-point/good/order/send").header(authorizationName, authorizationValue)
                        .param("id","2").param("logisticsInfo", "1111"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void confirmGood() throws Exception {
        log.info("用户收货");
        this.mockMvc
                .perform(get("/bonus-point/good/order/confirm").header(authorizationName, authorizationValue)
                        .param("id","2"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

}
