package com.fuzamei.bonuspoint.interceptor;

import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: bonus-point-cloud
 * @description: 用户角色与权限控制
 * @author: WangJie
 * @create: 2018-05-17 17:19
 **/
@Slf4j
@Component
public class IdentityIntercept extends HandlerInterceptorAdapter {

    private static final String COMPANY = "/company/";
    private static final String SUPER_MANAGER = "/main-platform/";
    private static final String MEMBER = "/member/";
    private static final String PLATFORM = "/platform/";
    private static final String OPTIONS = "OPTIONS";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 处理预查询
        if(OPTIONS.equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }


        Token token = (Token)request.getAttribute("token");
        if (token!=null) {
            Integer role = token.getRole();
            String uri = request.getRequestURI();
            boolean rightAuthorization = true;
            log.info("正在访问{}" + uri);
            if (StringUtil.contains(uri, SUPER_MANAGER) && !Roles.SUPER_MANAGER.equals(role)) {
                log.info("无总平台权限");
                rightAuthorization = false;
            }
            if (StringUtil.contains(uri, PLATFORM) && !Roles.PLATFORM.equals(role)) {

                rightAuthorization = false;
                log.info("无平台权限");
            }
            if (StringUtil.contains(uri, COMPANY) && !Roles.COMPANY.equals(role)) {
                log.info("无集团权限");
                rightAuthorization = false;
            }
            if (StringUtil.contains(uri, MEMBER) && !Roles.MEMBER.equals(role)) {
                log.info("无会员权限");
                rightAuthorization = false;
            }
            if (!rightAuthorization) {
                ResponseVO responseVO = new ResponseVO(SafeResponseEnum.AUTHORITY_WRONG);
                request.setAttribute("responseVO", responseVO);
                request.getRequestDispatcher("/token-error").forward(request,response);
                return false;
            }
        }
        return super.preHandle(request, response, handler);
    }
}
