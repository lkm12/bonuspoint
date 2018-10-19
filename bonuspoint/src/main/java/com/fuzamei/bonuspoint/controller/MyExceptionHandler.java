package com.fuzamei.bonuspoint.controller;

import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @program: bonus-point-cloud
 * @description: 全局异常处理
 * @author: WangJie
 * @create: 2018-08-21 10:59
 **/
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {
   // @ExceptionHandler(value = Exception.class)
    public ResponseVO defaultErrorhandler( Exception e){
        log.error(e.getMessage());
        return new ResponseVO(CommonResponseEnum.BAD_REQUEST);
    }
}
