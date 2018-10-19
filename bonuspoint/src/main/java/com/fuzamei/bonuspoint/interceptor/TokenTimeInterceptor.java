package com.fuzamei.bonuspoint.interceptor;

import com.fuzamei.bonuspoint.constant.CacheHeader;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.util.RedisTemplateUtil;
import com.fuzamei.bonuspoint.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * @create 2018/6/1
 */
@Slf4j
@Component
@RefreshScope
public class TokenTimeInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    private static final String OPTIONS = "OPTIONS";


    @Value("${token.outTime.browser}")
    private Long browserOutTime;

    @Value("${token.outTime.app}")
    private Long appOutTime;

    @Value(("${token.client.app}"))
    private String appClient;
    @Value("${token.client.browser}")
    private String browserClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        // 处理预查询
        if(OPTIONS.equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        Token token = (Token)request.getAttribute("token");
        if (token!=null) {
            // 更新token缓存
            String str = redisTemplateUtil.getStr(CacheHeader.TOKEN + token.getUid());
            if (StringUtil.isBlank(str)) {
                return;
            }
            log.info("更新token有效时间");

            if (token.getClient().equals(appClient)) {
                redisTemplateUtil.setStr(CacheHeader.TOKEN + token.getUid(), token.getTokenStr(), appOutTime, TimeUnit.SECONDS);
            }
            if (token.getClient().equals(browserClient)) {
                redisTemplateUtil.setStr(CacheHeader.TOKEN + token.getUid(), token.getTokenStr(), browserOutTime, TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }

}
