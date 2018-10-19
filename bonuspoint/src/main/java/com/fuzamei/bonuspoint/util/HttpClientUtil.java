/**
 * FileName: HttpClientUtil
 * Author: wangtao
 * Date: 2018/5/3 14:10
 * Description:
 */
package com.fuzamei.bonuspoint.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * http
 *
 * @author wangtao
 * @create 2018/5/3
 */

public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    private HttpClientUtil() {
        throw new AssertionError("不能实例化 HttpClientUtil");
    }

    /**
     * POST请求
     *
     * @param url   请求地址
     * @param param 请求参数
     * @return 请求结果
     */
    public static String httpPost(String url, String param) {
        LOGGER.info("POST URL: {}, params: {}", url, param);
        HttpPost request = new HttpPost(url);
        StringEntity se = new StringEntity(param, StandardCharsets.UTF_8);
        se.setContentType("application/json");
        request.setEntity(se);


        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000).setSocketTimeout(5000).build();
        request.setConfig(requestConfig);


        try (CloseableHttpClient httpClient = HttpClients.createDefault();

             CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String resultString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                LOGGER.info("结果：{}", resultString);
                return resultString;
            }
        } catch (Exception e) {
            LOGGER.info("POST ERROR：{}", e.getMessage());
        }
        return null;
    }


    /**
     * get 请求
     *
     * @param url 请求地址
     * @return 请求结果
     */
    public static String httpGet(String url) {
        LOGGER.debug("GET URL: {}", url);
        HttpGet httpGet = new HttpGet(url);


        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000).setSocketTimeout(5000).build();
        httpGet.setConfig(requestConfig);


        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String resultString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                LOGGER.info("结果：{}", resultString);
                return resultString;
            }
        } catch (Exception e) {
            LOGGER.error("GET ERROR: {}", e.getMessage());
        }
        return null;
    }


}
