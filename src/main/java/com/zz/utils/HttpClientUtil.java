package com.zz.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Francis.zz on 2016-05-20.
 * 描述：封装操作HttpClient的相关方法。<br/>
 * 使用jar包：httpcore-4.4.4.jar，httpclient-4.5.2.jar <br/>
 */
public class HttpClientUtil {
    private static RequestConfig config;
    private static String DEFAULT_CHARSET = "utf-8";

    /**
     * 创建HttpClient
     * @param outTime 超时时间
     * @param charset 字符编码
     * @return*/
    public static CloseableHttpClient createClient(int outTime, String charset) {
        // 创建请求参数
        if(config == null) {
            config = RequestConfig.custom()
                    .setSocketTimeout(outTime)
                    .setConnectTimeout(outTime)
                    .setConnectionRequestTimeout(outTime)
                    .build();
        }
        DEFAULT_CHARSET = charset;
        ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(Charset.forName(charset)).build();
        // 设置连接超时，socket超时时间
        BasicHttpClientConnectionManager bcc = new BasicHttpClientConnectionManager();
        bcc.setConnectionConfig(connectionConfig);

        SocketConfig sk = SocketConfig.custom().setSoTimeout(3000).build();
        bcc.setSocketConfig(sk);
        return HttpClients.custom()
                .setConnectionManager(bcc)
                .setDefaultConnectionConfig(connectionConfig)
                .build();
    }

    /**
     * Post请求
     * @param url
     * @param client
     * @param jsonReq json字符串
     * @return
     * @throws Exception
     */
    public static String excuteHttpPost(String url, CloseableHttpClient client, String jsonReq) throws Exception {
        /*if(client == null || StringUtil.isEmpty(jsonReq)) {
            throw new Exception("The params of executing http post request is invalid.");
        }*/
        // 创建请求体
        StringEntity entity = new StringEntity(jsonReq, "UTF-8");  // 解决中文乱码问题
        entity.setContentType("application/json");
        entity.getContent();
        HttpPost post = new HttpPost(url);
        post.setConfig(config);     // 设置请求参数

        // 设置请求编码
        //post.addHeader("Content-Type", "html/text;charset=UTF-8");
        // 设置请求编码
        post.setEntity(entity);

        String result = null;
        CloseableHttpResponse resp = null;
        try {
            // 执行请求
            resp = client.execute(post);
            int respCode = resp.getStatusLine().getStatusCode();

            HttpEntity respEntity = resp.getEntity();
            result = EntityUtils.toString(respEntity, "utf-8");
            System.out.println("Receive resp string：" + result);
            if(respCode != HttpStatus.SC_OK) {
                throw new Exception("Failed to execute Http request: " + respCode);
            }
        }catch (ConnectTimeoutException ce) {
            ce.printStackTrace();
        }finally {
            if(resp != null) {
                resp.close();
            }
        }

        return result;
    }

    /**
     * 执行Post请求，传递Map参数，使用request.getParameter获取
     * @param url
     * @param client
     * @param params
     * @return
     * @throws Exception
     */
    public static String excuteHttpPost(String url, CloseableHttpClient client, Map<String, String> params) throws Exception {
        if(client == null || StringUtil.isEmpty(params)) {
            throw new Exception("The params of executing http post request is invalid.");
        }
        HttpPost post = new HttpPost(url);
        post.setConfig(config);

        List<NameValuePair>  nvps = new ArrayList<NameValuePair>();
        Set<String> keys = params.keySet();
        for(String key : keys) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }

        post.setEntity(new UrlEncodedFormEntity(nvps, DEFAULT_CHARSET));

        String result = null;
        CloseableHttpResponse resp = null;
        try {
            // 执行请求
            resp = client.execute(post);
            int respCode = resp.getStatusLine().getStatusCode();

            HttpEntity respEntity = resp.getEntity();
            result = EntityUtils.toString(respEntity);
            System.out.println("Receive resp string：" + result);
            if(respCode != HttpStatus.SC_OK) {
                throw new Exception("Failed to execute Http request: " + respCode);
            }
        }catch (ConnectTimeoutException ce) {
            ce.printStackTrace();
        }finally {
            if(resp != null) {
                resp.close();
            }
        }

        return result;
    }

    /**
     * Post请求
     * @param url
     * @param client
     * @param xmlStr xml字符串
     * @return
     * @throws Exception
     */
    public static String excuteXmlPost(String url, CloseableHttpClient client, String xmlStr) throws Exception {
        // 创建请求体
        StringEntity entity = new StringEntity(xmlStr, "UTF-8");  // 解决中文乱码问题
        entity.setContentType("text/xml");
        HttpPost post = new HttpPost(url);
        post.setConfig(config);     // 设置请求参数

        // 设置请求编码
        post.addHeader("Content-Type", "text/xml;charset=UTF-8");
        // 设置请求编码
        post.setEntity(entity);

        String result = null;
        CloseableHttpResponse resp = null;
        try {
            // 执行请求
            resp = client.execute(post);
            int respCode = resp.getStatusLine().getStatusCode();

            HttpEntity respEntity = resp.getEntity();
            result = EntityUtils.toString(respEntity, "utf-8");
            System.out.println("Receive resp string：" + result);
            if(respCode != HttpStatus.SC_OK) {
                throw new Exception("Failed to execute Http request: " + respCode);
            }
        }catch (ConnectTimeoutException ce) {
            ce.printStackTrace();
        }finally {
            if(resp != null) {
                resp.close();
            }
        }

        return result;
    }
}
