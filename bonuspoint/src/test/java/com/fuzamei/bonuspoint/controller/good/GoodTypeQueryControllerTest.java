package com.fuzamei.bonuspoint.controller.good;

import com.fuzamei.bonuspoint.controller.BaseControllerTest;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * test
 * @author liumeng
 * @create 2018年5月15日
 */
@Transactional
@Slf4j
public class GoodTypeQueryControllerTest extends BaseControllerTest {

    @Test
    public void getGoodType() throws Exception {
        log.info("测试获取父分类");
        this.mockMvc.perform(get("/bonus-point/goodtype/types/1")).andExpect(status().isOk()).andDo(print()).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void listTypes() throws Exception {
        log.info("测试获取父分类");
        this.mockMvc.perform(get("/bonus-point/goodtype/types")).andExpect(status().isOk()).andDo(print()).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void getSubtype() throws Exception {
        log.info("测试获取子分类");
        this.mockMvc.perform(get("/bonus-point/goodtype/subtypes/1")).andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void listSubType() throws Exception {
        log.info("测试获取父分类对应的所有子分类");
        this.mockMvc.perform(get("/bonus-point/goodtype/types/1/subtypes")).andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    public void listAllTypes() throws Exception {
        log.info("测试获取所有的分类信息");
        this.mockMvc.perform(get("/bonus-point/goodtype/alltypes/list")).andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }
}
