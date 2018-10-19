package com.fuzamei.bonuspoint.controller;

import com.alibaba.fastjson.JSONObject;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: bonus-point-cloud
 * @description: 用于token拦截后的处理，处理/error接口
 * @author: WangJie
 * @create: 2018-07-17 09:30
 **/
@RestController
@Slf4j
/*public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseVO error(){
        return new ResponseVO(ResponseEnum.BAD_REQUEST);
    }

    @RequestMapping("/token-error")
    public ResponseVO tokenError(@RequestAttribute("responseVO") ResponseVO responseVO){
        return responseVO;
    }

    @Override
    public String getErrorPath() {
        log.info("进入自定义错误处理页面");
        return "redirect:/error";
    }
}*/

public class MyErrorController {

    @RequestMapping("/token-error")
    public ResponseVO tokenError(@RequestAttribute("responseVO") ResponseVO responseVO){

        return responseVO;
    }


}