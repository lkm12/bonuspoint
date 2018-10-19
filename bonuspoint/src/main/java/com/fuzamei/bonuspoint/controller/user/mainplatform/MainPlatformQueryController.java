package com.fuzamei.bonuspoint.controller.user.mainplatform;

import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.service.user.mainplatform.MainPlatformService;
import com.fuzamei.common.model.dto.PageDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: bonus-point-cloud
 * @description: 总平台 读接口
 * @author: WangJie
 * @create: 2018-09-11 14:02
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point/main-platform")
public class MainPlatformQueryController {


    @Value("${page.pageSize}")
    private Integer pageSize;
    @Value("${page.maxSize}")
    private Integer maxSize;
    private final MainPlatformService mainPlatformService;


    @Autowired
    public MainPlatformQueryController(MainPlatformService mainPlatformService) {
        this.mainPlatformService = mainPlatformService;

    }

    @LogAnnotation(note = "查看平台列表")
    @PostMapping("/list-platform")
    public ResponseVO listPlatform(@RequestBody PageDTO pageDTO){
        if (pageDTO.getPage() == null || pageDTO.getPage() < 1) {
            pageDTO.setPage(1);
        }
        if (pageDTO.getPageSize() == null || pageDTO.getPageSize()<1 ) {
            pageDTO.setPageSize(1);
        }
        if (pageDTO.getPageSize()>maxSize){
            pageDTO.setPageSize(maxSize);
        }
        return mainPlatformService.listPlatform(pageDTO);
    }








}
