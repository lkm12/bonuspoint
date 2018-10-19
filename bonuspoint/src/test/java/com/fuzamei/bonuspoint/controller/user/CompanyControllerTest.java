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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-09 17:41
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
public class CompanyControllerTest {
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
     * 商户查看店铺基础信息
     * @throws Exception
     */
    @Test
    public void testGetCompanyBaseInfo() throws  Exception{

        authorizationValue = "token&343";
        this.mockMvc.perform(get("/bonus-point/company/company-base-info")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 商户更新店铺信息
     * @throws Exception
     */
    @Test
    public void testUpdateCompanyBaseInfo() throws  Exception{
        Map<String,Object>map = new HashMap<>(6);
        map.put("companyName","wj_company");
        map.put("companyLeader","wj_leader");
        map.put("companyLeaderIdCard","41152619950816265X");
        map.put("companyLeaderMobile","17826873177");
        map.put("companyEmail","123455@qq.com");
        map.put("headimgurl","http://www.t.cn");
        authorizationValue = "token&343";
        this.mockMvc.perform(put("/bonus-point/company/update-company-base-info")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }


}
