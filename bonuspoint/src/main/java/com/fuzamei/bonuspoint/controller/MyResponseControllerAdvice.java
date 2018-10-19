package com.fuzamei.bonuspoint.controller;

import com.alibaba.fastjson.JSON;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.common.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-20 14:30
 **/
@ControllerAdvice
public class MyResponseControllerAdvice implements ResponseBodyAdvice<Object> {

    @Value(("${token.client.app}"))
    private String appClient;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        ServletServerHttpRequest req = (ServletServerHttpRequest) serverHttpRequest;
        HttpServletRequest httpReq = req.getServletRequest();
        //   Token token = (Token) httpReq.getAttribute("token");
        String client = httpReq.getHeader("client");
        if (client!=null && appClient.equals(client)) {
            String jsonStr = JSON.toJSONString(o);
            jsonStr=JsonUtil.jsonNumberToString(jsonStr);
            return JSON.parseObject(jsonStr);
        }
        return o;
    }
}