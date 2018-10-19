package com.fuzamei.bonuspoint.controller.good;

import com.fuzamei.bonuspoint.controller.BaseControllerTest;
import com.fuzamei.bonuspoint.entity.dto.good.GoodSubTypeDTO;
import com.fuzamei.bonuspoint.entity.dto.good.GoodTypeDTO;
import com.fuzamei.bonuspoint.validation.ExNum;
import com.fuzamei.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@Slf4j
public class GoodTypeManagerControllerTest extends BaseControllerTest {

    @Test
    public void saveGoodType() throws Exception {
        log.info("添加商品父分类");
        GoodTypeDTO goodType = new GoodTypeDTO();
        goodType.setName("test");
        goodType.setImg("test1/M00/00/49/wKgCt1srY2iAdMvvAAClyyW2mKQ4610054");
        log.info(JsonUtil.toJsonString(goodType));
        this.mockMvc
                .perform(post("/bonus-point/goodtype/types").header(authorizationName, authorizationValue)
                        .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJsonString(goodType)))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void updateGoodType() throws Exception {
        log.info("更新商品父分类");
        GoodTypeDTO goodType = new GoodTypeDTO();
        goodType.setName("test");
        goodType.setImg("test1/M00/00/49/wKgCt1srY2iAdMvvAAClyyW2mKQ4610054");
        log.info(JsonUtil.toJsonString(goodType));
        this.mockMvc
                .perform(put("/bonus-point/goodtype/types/1").header(authorizationName, authorizationValue)
                        .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJsonString(goodType)))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }
    @Test
    public void deleteType() throws Exception {
        log.info("删除商品分类");
        this.mockMvc.perform(delete("/bonus-point/goodtype/types/23").header(authorizationName, authorizationValue))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }
    @Test
    public void saveSubType() throws Exception {
        log.info("添加商品子分类");
        GoodSubTypeDTO goodSubTypeDTO = new GoodSubTypeDTO();
        goodSubTypeDTO.setName("test");
        goodSubTypeDTO.setPid((long) 1);
        goodSubTypeDTO.setImg("test1/M00/00/49/wKgCt1srY2iAdMvvAAClyyW2mKQ4610054");
        log.info(JsonUtil.toJsonString(goodSubTypeDTO));
        this.mockMvc
                .perform(post("/bonus-point/goodtype/subtypes").header(authorizationName, authorizationValue)
                        .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJsonString(goodSubTypeDTO)))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

    }

    @Test
    public void updateSubType() throws Exception {
        log.info("更新商品子分类");
        GoodSubTypeDTO goodSubTypeDTO = new GoodSubTypeDTO();
        goodSubTypeDTO.setName("test_update");
        goodSubTypeDTO.setPid((long) 1);
        goodSubTypeDTO.setImg("test1/M00/00/49/wKgCt1srY2iAdMvvAAClyyW2mKQ4610054");
        log.info(JsonUtil.toJsonString(goodSubTypeDTO));
        this.mockMvc
                .perform(put("/bonus-point/goodtype/subtypes/1").header(authorizationName, authorizationValue)
                        .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJsonString(goodSubTypeDTO)))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteSubtype() throws Exception {
        log.info("删除商品子分类");
        this.mockMvc.perform(delete("/bonus-point/goodtype/subtypes/144").header(authorizationName, authorizationValue))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

}
