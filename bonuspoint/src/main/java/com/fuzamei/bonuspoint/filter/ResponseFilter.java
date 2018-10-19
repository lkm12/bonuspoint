package com.fuzamei.bonuspoint.filter;



import com.fuzamei.bonuspoint.util.RedisTemplateUtil;
import com.fuzamei.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @program: bonus-point-cloud
 * @description: 返回值过滤器
 * @author: WangJie
 * @create: 2018-07-19 10:38
 **/
@Slf4j
public class ResponseFilter implements Filter {
    @Value(("${token.client.app}"))
    private String appClient;
    /**
     * 加密密钥
     */
    @Value("${token.encrypt.key}")
    private String encryptKey;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {


        MyHttpServletResponseWrapper wrapperResponse = new MyHttpServletResponseWrapper((HttpServletResponse) response);//转换成代理类

        // 这里只拦截返回，直接让请求过去，如果在请求前有处理，可以在这里处理

        filterChain.doFilter(request, wrapperResponse);
        log.info("进入返回值过滤器处理{}", ((HttpServletRequest) request).getRequestURI());
            byte[] content = wrapperResponse.getContent();//获取返回值
            //判断是否有值
            if (content.length > 0) {

                String str = new String(content, "UTF-8");
                log.info("处理前返回值{}", str);
                String expectStr = JsonUtil.jsonNumberToString(str);
                //把返回值输出到客户端

                ServletOutputStream out = response.getOutputStream();
                out.write(expectStr.getBytes());


                log.info("处理后返回值{}", expectStr);
            }
        filterChain.doFilter(request, response);

    }

    @Override
    public void init(FilterConfig arg0)
            throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
