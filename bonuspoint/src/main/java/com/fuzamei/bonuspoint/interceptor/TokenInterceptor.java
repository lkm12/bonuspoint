package com.fuzamei.bonuspoint.interceptor;

import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.util.RedisTemplateUtil;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.bonuspoint.util.TokenUtil;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangtao
 * @create 2018/6/1
 */
@Slf4j
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {


    private static final String OPTIONS = "OPTIONS";
    private static final String TOKEN = "token";
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    /** 加密密钥 */
    @Value("${token.encrypt.key}")
    private String encryptKey;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // 处理预查询

        if (OPTIONS.equals(request.getMethod()) || StringUtil.contains(request.getRequestURI().toLowerCase(), TOKEN)) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 拦截商品分类
        if (request.getRequestURI().contains("/bonus-point/goodtype") && request.getMethod().toUpperCase().equals("GET")) {
            return true;
        }
        log.info("开始进入请求地址拦截{}", request.getRequestURI());
        String tokenStr = request.getHeader("Authorization");

        if (StringUtil.isBlank(tokenStr)) {
            log.info("token不能为空");
            ResponseVO responseVO = new ResponseVO(CommonResponseEnum.TOKEN_BLANK_ERROR);
            request.setAttribute("responseVO", responseVO);
            request.getRequestDispatcher("/token-error").forward(request, response);
            return false;
        }
        try {
            Boolean activeToken = TokenUtil.isActiveToken(tokenStr, encryptKey, redisTemplateUtil);
            if (!activeToken) {
                log.info("token错误");
                ResponseVO responseVO = new ResponseVO(CommonResponseEnum.TOKEN_NOT_ACTIVE);
                request.setAttribute("responseVO", responseVO);
                request.getRequestDispatcher("/token-error").forward(request, response);
                return false;
            }
        } catch (Exception e) {
            log.info("token解析错误");
            ResponseVO responseVO = new ResponseVO(CommonResponseEnum.TOKEN_ERROR);
            request.setAttribute("responseVO", responseVO);
            request.getRequestDispatcher("/token-error").forward(request, response);
            return false;
        }

        Token token = TokenUtil.getTokenObject(tokenStr, encryptKey);

        request.setAttribute("token", token);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }

}
