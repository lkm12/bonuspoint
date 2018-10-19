package com.fuzamei.bonuspoint.controller.user;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-04-20 17:40
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class UserAddressControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final static String authorizationName = "Authorization";
    private final static String authorizationValue = "token&1";
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testGetUserAddressList()throws  Exception{
        Map<String,String> map = new HashMap<String,String>(2);
        map.put("page","1");

        this.mockMvc.perform(post("/bonus-point/member/address/list")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testSaveUserAddress() throws Exception{
        Map<String,String> map = new HashMap<String,String>(8);
        map.put("receiver","张三");
        map.put("mobile","17826873177");
        map.put("address_province","110000");
        map.put("address_city","110100");
        map.put("address_area","110101");
        map.put("area_detail","双门洞");
        this.mockMvc.perform(post("/bonus-point/member/address/create")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testUpdateUserAddress()throws Exception{
        Map<String,Object> map = new HashMap<String,Object>(8);
        map.put("address_id",1);
        map.put("receiver","张三");
        map.put("mobile","17826873177");
        map.put("address_province","110000");
        map.put("address_city","110100");
        map.put("address_area","110101");
        map.put("area_detail","双门洞");
        this.mockMvc.perform(put("/bonus-point/member/address/update")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testDeleteUserAddress()throws Exception{
        Map<String,Object> map = new HashMap<String,Object>(8);
        map.put("address_id",1);
        this.mockMvc.perform(delete("/bonus-point/member/address/delete")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }


}
