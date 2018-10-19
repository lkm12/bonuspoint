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
 * @create: 2018-04-20 14:35
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
@Slf4j
public class UserControllerTest {
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
    public void testUpdateNickname() throws Exception{
        Map<String,String> map = new HashMap<String,String>(4);
        map.put("nickname","hehe");
        this.mockMvc.perform(put("/bonus-point/member/edit-nickname")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 集团查询其某个会员的公钥
     * @throws Exception
     */
    @Test
    public void testGetMemberBlockInfo()throws  Exception{
        Map<String,String> map = new HashMap<String,String>(1);
        map.put("mobile","17826873177");
        this.mockMvc.perform(post("/bonus-point/company/info-block")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 转账时通过手机号查账号
     * @author wangjie
     * @throws Exception
     */
    @Test
    public void testGetUserInfoByMobile()throws Exception{
        Map<String,String> map = new HashMap<String,String>(1);
        map.put("mobile","17826873177");
        authorizationValue = "token&1";
        this.mockMvc.perform(post("/bonus-point/user/get-account-info-by-mobile")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 转账时通过公钥查账号
     * @author wangjie
     * @throws Exception
     */
    @Test
    public void testGetUserInfoByPublicKey()throws Exception{
        Map<String,String> map = new HashMap<String,String>(1);
        map.put("publicKey","c7f1d264af657a0c422bb50bdb0e07a725f493e0d76867e8d78338446d01a61b");
        authorizationValue = "token&1";
        this.mockMvc.perform(post("/bonus-point/user/get-account-info-by-publicKey")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonBytes(map)))
                .andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }


    /**
     * 集团查看会员积分信息
     * @author wangjie
     */
    @Test
    public void getMemberPointInfoList()throws Exception{
        Map<String,Object> map = new HashMap<>(4);
        map.put("mobile","1");
        this.mockMvc.perform(post("/bonus-point/company/list-member-point-info")
                .header(authorizationName,authorizationValue)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJsonString(map))).
                andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

}
