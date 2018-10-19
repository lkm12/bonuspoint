package com.fuzamei.bonuspoint.controller.good;

import com.alibaba.fastjson.JSON;
import com.fuzamei.bonuspoint.controller.BaseControllerTest;
import com.fuzamei.bonuspoint.entity.dto.good.GoodDTO;
import com.fuzamei.bonuspoint.entity.dto.point.ApplyPointDTO;
import com.fuzamei.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * test
 *
 * @author liumeng
 * @create 2018年5月15日
 */
//@Transactional
@Slf4j
public class GoodManagerControllerTest extends BaseControllerTest {

    @Test
    public void saveGood() throws Exception {
        log.info("测试添加商品");
        GoodDTO goodDTO = new GoodDTO();
        goodDTO.setName("test");
        goodDTO.setPrice(new BigDecimal(10));
        goodDTO.setGlobalPrice(new BigDecimal(10));
        goodDTO.setNum(100);
        goodDTO.setWorth(new BigDecimal(10));
        goodDTO.setSid(1L);
        goodDTO.setImageUrls("test1/M00/00/49/wKgCt1ssZJyAZ39ZAAClyyW2mKQ339.PNG");

        this.mockMvc
                .perform(post("/bonus-point/good/add").header(authorizationName, authorizationValue)
                        .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJsonString(goodDTO)))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void updateGood() throws Exception {
        log.info("测试编辑商品");
        GoodDTO goodDTO = new GoodDTO();
        goodDTO.setId(1L);
        goodDTO.setName("test");
        goodDTO.setPrice(new BigDecimal(10));
        goodDTO.setPrice(new BigDecimal(10));
        goodDTO.setNum(100);
        goodDTO.setWorth(new BigDecimal(10));
        goodDTO.setSid(1L);
        goodDTO.setImageUrls("test1/M00/00/49/wKgCt1ssZJyAZ39ZAAClyyW2mKQ339.PNG");

        this.mockMvc
                .perform(post("/bonus-point/good/update").header(authorizationName, authorizationValue)
                        .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJsonString(goodDTO)))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void shelfGood() throws Exception {
        log.info("测试上架商品");
        this.mockMvc.perform(get("/bonus-point/good/shelf/2").header(authorizationName, authorizationValue))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void dropGood() throws Exception {
        log.info("测试下架商品");
        this.mockMvc.perform(get("/bonus-point/good/drop/155").header(authorizationName, authorizationValue))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteGood() throws Exception {
        log.info("测试删除商品");
        this.mockMvc.perform(get("/bonus-point/good/delete/1").header(authorizationName, authorizationValue))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

}
