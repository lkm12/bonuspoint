package com.fuzamei.bonuspoint.controller.user;

import com.fuzamei.common.util.JsonUtil;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-09 10:39
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
public class PlatformControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private final static String authorizationName = "Authorization";
    private static String authorizationValue = "token&1";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }


    /**
     * 平台模糊查询下属集团（商户）信息
     * @author wangjie
     * @throws Exception
     */
    @Test
    public void testGetCompanyInfoList()throws Exception{
        Map<String,String> map = new HashMap<String,String>(1);
       // map.put("fuzzyMatch","吴");
        authorizationValue = "token&1";
        this.mockMvc.perform(post("/bonus-point/platform/list-companyInfo")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }



    /**
     * 平台添加集团
     * @author wangjie
     * @throws Exception
     */
    @Test
    public void testAddCompanyInfo()throws Exception{
        Map<String,String> map = new HashMap<String,String>(1);
        authorizationValue = "token&1";
        map.put("username","WangJieTest3");
        map.put("companyAddress","地球村");
        map.put("companyLeader","海");
        map.put("companyLeaderMobile","17826873177");
        map.put("companyTelephone","99999999");
        map.put("companyName","复杂美");
        map.put("companyEmail","89@qq.com");
        map.put("cashRate","0.1");
        map.put("pointRate","0.1");
        map.put("password","a123455");
        map.put("payword","123456");
        this.mockMvc.perform(post("/bonus-point/platform/add-companyInfo")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 平台修改集团备付金比例
     * @author wangjie
     * @throws Exception
     */
    @Test
    public void testUpdateCompanyCashRate()throws Exception{
        Map<String,Object> map = new HashMap<String,Object>(1);
        authorizationValue = "token&1";
        map.put("id","78");
        map.put("payword","123456");
        map.put("cashRate",0.18);
        this.mockMvc.perform(put("/bonus-point/platform/edit-cashrate")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }
    /**
     * 平台修改集团积分兑换比例
     * @author wangjie
     * @throws Exception
     */
    @Test
    public void testUpdateCompanyPointRate()throws Exception{
        Map<String,Object> map = new HashMap<String,Object>(1);
        authorizationValue = "token&1";
        map.put("id","78");
        map.put("payword","123456");
        map.put("pointRate",14.88);
        this.mockMvc.perform(put("/bonus-point/platform/edit-pointrate")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testListCompanyCashRateInfo() throws Exception{
        Map<String ,Object> map = new HashMap<>(5);
        authorizationValue = "token&1";
      //  map.put("companyName","复杂");
        map.put("orderType",1);
        this.mockMvc.perform(post("/bonus-point/platform/list-companyCashRateInfo")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testListCompanyPointRateInfo() throws Exception{
        Map<String ,Object> map = new HashMap<>(5);
        authorizationValue = "token&1";
        //  map.put("companyName","复杂");
        map.put("orderType",1);
        map.put("page",-1);
        map.put("pageSize",-1);
        this.mockMvc.perform(post("/bonus-point/platform/list-companyPointRateInfo")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

}
