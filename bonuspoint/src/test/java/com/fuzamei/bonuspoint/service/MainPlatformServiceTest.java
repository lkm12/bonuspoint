package com.fuzamei.bonuspoint.service;

import com.alibaba.fastjson.JSON;
import com.fuzamei.bonuspoint.entity.dto.PlatformInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.service.user.mainplatform.MainPlatformService;
import com.fuzamei.common.model.dto.PageDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-09-10 16:58
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MainPlatformServiceTest {

    @Autowired
    private MainPlatformService mainPlatformService;
    @Test
    public void addPlatform(){
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setPId(0L);
        accountDTO.setUsername("main-platform-admin");
        accountDTO.setPasswordHash("test password hash");
        PlatformInfoDTO platformInfoDTO = new PlatformInfoDTO();
        platformInfoDTO.setCashRate(0.5F);
        platformInfoDTO.setPointRate(2.1F);

        mainPlatformService.addPlatform(accountDTO,platformInfoDTO);
    }
    @Test
    public void listPlatform(){
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageSize(5);
        pageDTO.setPage(1);
        System.out.println(JSON.toJSONString(mainPlatformService.listPlatform(pageDTO)));
    }
}
