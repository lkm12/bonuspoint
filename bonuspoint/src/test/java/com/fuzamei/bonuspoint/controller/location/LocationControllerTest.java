package com.fuzamei.bonuspoint.controller.location;

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
 * @program: bonus-point-cloud
 * @description: 省市县三级地址controller类测试类
 * @author: WangJie
 * @create: 2018-04-28 16:32
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;


    private final static String authorizationName = "Authorization";
    private static String authorizationValue = "token&1";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testGetProvinceList() throws Exception{
        this.mockMvc.perform(get("/bonus-point/location/list-province")
                .header(authorizationName,authorizationValue))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    public void testGetCityList() throws Exception{
        this.mockMvc.perform(get("/bonus-point/location/420000/list-city")
                .header(authorizationName,authorizationValue))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    public void testGetAreaList() throws Exception{
        this.mockMvc.perform(get("/bonus-point/location/421100/list-area")
                .header(authorizationName,authorizationValue))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }


}
