package com.fuzamei.bonuspoint.controller.point;

import com.alibaba.fastjson.JSON;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by 18519 on 2018/5/6.
 *
 * lkm
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class MemberPointControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final static String authorizationName = "Authorization";
    private final static String authorizationValue = "token&1";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }




    /**
     * 会员查看积分使用明细
     * @author wangjie
     * @throws Exception
     */
    @Test
    public void memberPointListDetailTest()throws  Exception{
        Map<String,Object> map = new HashMap<>(6);
        map.put("type",1);
      //  map.put("startTime",1524537954113L);
       // map.put("endTime",1524637954113L);
        map.put("pointType",1);
        this.mockMvc.perform(post("/bonus-point/point/member/point-list-detail")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }


    /**
     * 会员查看持有通用积分列表
     * @author wangjie
     */
    @Test
    public void memberPointListRelation()throws  Exception{
        this.mockMvc.perform(get("/bonus-point/point/member/point-list-relation")
                .header(authorizationName,authorizationValue))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }


}
