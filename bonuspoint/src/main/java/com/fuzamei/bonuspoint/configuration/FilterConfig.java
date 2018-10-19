package com.fuzamei.bonuspoint.configuration;

import com.fuzamei.bonuspoint.filter.CorsFilter;
import com.fuzamei.bonuspoint.filter.ResponseFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @program: bonus-point-cloud
 * @description: 配置过滤器
 * @author: WangJie
 * @create: 2018-07-19 10:45
 **/

public class FilterConfig {

    public final CorsFilter corsFilter;

    @Autowired
    public FilterConfig(CorsFilter corsFilter) {
        this.corsFilter = corsFilter;
    }

    /**
     * 配置过滤器
     * @return
     */

    //@Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(corsFilter);
        registration.addUrlPatterns("/*");
     //   registration.addInitParameter("paramName", "paramValue");
        registration.setName("CorsFilter");
        return registration;
    }



}
