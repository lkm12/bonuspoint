package com.fuzamei.bonuspoint.controller.good;

import com.fuzamei.bonuspoint.controller.BaseControllerTest;
import com.fuzamei.bonuspoint.entity.dto.good.QueryOrderDTO;
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
public class GoodOrderQueryControllerTest extends BaseControllerTest {

    @Test
    public void queryGoodOrder() throws Exception {
        log.info("查询订单");
        QueryOrderDTO queryOrderDTO = new QueryOrderDTO();
        log.info(JsonUtil.toJsonString(queryOrderDTO));
        this.mockMvc
                .perform(post("/bonus-point/good/queryGoodOrder").header(authorizationName, authorizationValue)
                        .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJsonString(queryOrderDTO)))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void getGoodOrder() throws Exception {
        log.info("查询指定订单");
        this.mockMvc.perform(get("/bonus-point/good/getGoodOrder/66").header(authorizationName, authorizationValue))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void getCompanyPoint() throws Exception {
        log.info("查询指定订单");
        this.mockMvc.perform(get("/bonus-point/query/point-sum").header(authorizationName, authorizationValue)
                .param("gid", "2"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }


}
