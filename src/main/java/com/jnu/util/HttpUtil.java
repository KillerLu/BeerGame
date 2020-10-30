package com.jnu.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author ：Killer
 * @date ：Created in 20-10-28 下午4:23
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static final String MINIPROGRAM_USER_OPENID =
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";


    public static String get(String url, HttpHost proxy){
        CloseableHttpClient httpClient = HttpClientBuilder.create().setProxy(proxy).build();
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(get);
            HttpEntity entity = httpResponse.getEntity();
            String responseStr = EntityUtils.toString(entity);
            logger.info("response : " + responseStr);
            return responseStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String get(String url){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(get);
            HttpEntity entity = httpResponse.getEntity();
            String responseStr = EntityUtils.toString(entity,"UTF-8");
            logger.info("response str = " + responseStr);
            return responseStr;
        } catch (IOException e) {
            logger.error("", e);
        }
        return null;
    }

    public static void main(String[] args) {
        String url = String.format(MINIPROGRAM_USER_OPENID,"wx47ed71e5bd001c32","0f5c8cea449163c4409e558ce33eded1","dzzy2");
        get(url);

    }
}
