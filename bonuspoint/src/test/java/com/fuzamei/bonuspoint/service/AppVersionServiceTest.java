package com.fuzamei.bonuspoint.service;

import com.alibaba.fastjson.JSON;
import com.fuzamei.bonuspoint.entity.po.data.version.AppVersioinPO;
import com.fuzamei.bonuspoint.service.data.version.AppVersionService;
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
 * @create: 2018-09-11 11:18
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppVersionServiceTest {
    @Autowired
    private AppVersionService appVersionService;

    @Test
    public void listAppVersionTest(){
        PageDTO pageDTO = new PageDTO();
        pageDTO.setFuzzyMatch("Android");
        pageDTO.setPage(1);
        pageDTO.setPageSize(10);
        System.out.println(JSON.toJSONString(appVersionService.listAppVersion(pageDTO)));
    }
}
