package com.fuzamei.bonuspoint.configuration;

import com.fuzamei.bonuspoint.interceptor.IdentityIntercept;
import com.fuzamei.bonuspoint.interceptor.TokenInterceptor;
import com.fuzamei.bonuspoint.interceptor.TokenTimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-05-08 11:28
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;

    private final TokenTimeInterceptor tokenTimeInterceptor;

    private final IdentityIntercept identityIntercept;

    @Autowired
    public InterceptorConfig(TokenInterceptor tokenInterceptor, TokenTimeInterceptor tokenTimeInterceptor, IdentityIntercept identityIntercept) {
        this.tokenInterceptor = tokenInterceptor;
        this.tokenTimeInterceptor = tokenTimeInterceptor;
        this.identityIntercept = identityIntercept;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/bonus-point/message/reset/**","/bonus-point/safe/password-reset","/bonus-point/message/register/**")
                .excludePathPatterns("/bonus-point/member/member-register", "/bonus-point/browser-login","/bonus-point/member/admin-login",
                        "/bonus-point/member/memberLoginAPP","/bonus-point/memberTranfer/tranfer_bp_user","/bonus-point/member/memberLoginAPPCaptcha")
                .excludePathPatterns("/error", "/token-error")
                .excludePathPatterns("/data/**")
                .excludePathPatterns("/bonus-point/good/get/*")
                .excludePathPatterns("/bonus-point/good/query")
                .excludePathPatterns("/bonus-point/good/appshow")
                .excludePathPatterns("/bonus-point/good/typeshow")
                .excludePathPatterns("/bonus-point/good/companyshow")
                .excludePathPatterns("/bonus-point/good/company-goods-info/{companyId}")
                .excludePathPatterns("/bonus-point/location/**")
                .excludePathPatterns("/bonus-point/user/is-mobile-exist")
                .excludePathPatterns("/bonus-point/list-advertisement")
                .excludePathPatterns("/bonus-point/server/file/url");


        registry.addInterceptor(identityIntercept)
                .addPathPatterns("/**")
                .excludePathPatterns("/bonus-point/member/member-register", "/bonus-point/browser-login","/bonus-point/member/admin-login",
                        "/bonus-point/member/memberLoginAPP","/bonus-point/memberTranfer/tranfer_bp_user","/bonus-point/member/memberLoginAPPCaptcha");


        registry.addInterceptor(tokenTimeInterceptor)
                .addPathPatterns("/**");

    }
}
