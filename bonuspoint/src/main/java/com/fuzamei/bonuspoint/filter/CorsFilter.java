package com.fuzamei.bonuspoint.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-08-08 15:38
 **/
@Slf4j
public class CorsFilter implements Filter {     //filter 接口的自定义实现
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        response.setHeader("Access-Control-Allow-Origin", "*");
        if ("OPTIONS".equals(request.getMethod())){//这里通过判断请求的方法，判断此次是否是预检请求，如果是，立即返回一个204状态吗，标示，允许跨域；预检后，正式请求，这个方法参数就是我们设置的post了
            log.info(request.getRequestURL().toString());
            response.setStatus(HttpStatus.SC_OK); //HttpStatus.SC_NO_CONTENT = 204
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, OPTIONS, DELETE");//当判定为预检请求后，设定允许请求的方法
            response.setHeader("Access-Control-Allow-Headers", "authorization,Authorization,Content-Type,x-requested-with,Origin, No-Cache,If-Modified-Since,Pragma,Last-Modified,Cache-Control,Expires,X-E4M-With"); //当判定为预检请求后，设定允许请求的头部类型
            response.addHeader("Access-Control-Max-Age", "1");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
    @Override
    public void destroy() {
    }
}
