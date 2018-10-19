package com.fuzamei.bonuspoint.controller.data.version;

import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.entity.dto.data.version.AppVersioinDTO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.service.data.version.AppVersionService;
import com.fuzamei.common.model.dto.PageDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @program: bonus-point-cloud
 * @description: 安装版本信息控制器
 * @author: WangJie
 * @create: 2018-08-02 14:46
 **/
@RestController
@Slf4j
public class AppVersionControl {

    @Value("${page.pageSize}")
    private Integer pageSize;
    @Value("${page.maxSize}")
    private Integer maxSize;
    private final AppVersionService appVersionService;


    @Autowired
    public AppVersionControl(AppVersionService appVersionService) {
        this.appVersionService = appVersionService;
    }

    @LogAnnotation(note = "添加App版本")
    @PostMapping("/bonus-point/main-platform/add-app-version")
    public ResponseVO addAppVersion(@RequestBody @Valid AppVersioinDTO appVersioinDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        return appVersionService.addAppVersion(appVersioinDTO);
    }

    /*   @GetMapping("/list-app-version")
       public ResponseVO getAppVersionList(){
           log.info("获取版本信息记录开始");
           return

       }*/
    @LogAnnotation(note = "获取版本更新列表")
    @PostMapping("/bonus-point/main-platform/list-app-version")
    public ResponseVO listAppVersions(@RequestBody PageDTO pageDTO){
        if (pageDTO.getPage() == null || pageDTO.getPage() < 1) {
            pageDTO.setPage(1);
        }
        if (pageDTO.getPageSize() == null || pageDTO.getPageSize()<1 ) {
            pageDTO.setPageSize(1);
        }
        if (pageDTO.getPageSize()>maxSize){
            pageDTO.setPageSize(maxSize);
        }
        return appVersionService.listAppVersion(pageDTO);
    }
    @LogAnnotation(note = "获取安卓最新版本信息")
    @GetMapping("/data/app-version")
    public ResponseVO getNewestAppVersion() {
        return appVersionService.getNewestAppVersionForAndroid();
    }

    @LogAnnotation(note = "获取IOS最新版本信息开始")
    @GetMapping("/data/app-version-ios")
    public ResponseVO getNewestAppVersionForIOS() {
        return appVersionService.getNewestAppVersionForIOS();
    }
}
