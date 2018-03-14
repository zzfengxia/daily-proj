package com.zz.utils;

import com.zz.exception.BizException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Francis.zz on 2017/8/1.
 * 支持https单向认证忽略证书，https 双向认证带证书，https 双向认证忽略证书，http请求
 * 使用httpclient4.5.x版本
 */
public class CustomApacheHttpClient {
    private final static Logger logger = LoggerFactory.getLogger(CustomApacheHttpClient.class);

    private final static String KEY_STORE_TYPE_JKS = "jks";         // keystore，jks证书
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";		// .p12后缀的证书
    // 超时重试次数
    private static final int TIMEOUT_RETRY_COUNT = 3;

    private String trustPath ;
    private String trustStorePassword ;
    private String keyPath ;
    private String keyStorePassword ;
    private String keyPassword ;
    private CloseableHttpClient client;
    private RequestConfig config;
    //private String url;

    public void setTrustStore(String trustPath, String trustStorePassword) {
        this.trustPath = trustPath;
        this.trustStorePassword = trustStorePassword;
    }

    public void setKeyStore(String keyPath, String keyStorePassword, String keyPassword) {
        this.keyPath = keyPath;
        this.keyStorePassword = keyStorePassword;
        this.keyPassword = keyPassword;
    }

    public CustomApacheHttpClient(int timeOut) {
        // 创建请求参数
        if(config == null) {
            config = RequestConfig.custom()
                    .setSocketTimeout(timeOut)
                    .setConnectTimeout(timeOut)
                    .setConnectionRequestTimeout(timeOut)
                    .build();
        }
    }

    /**
     * SSL客户端
     *
     * @param keyPath               密钥库地址
     * @param keyStorePassword      密钥库文件密码
     * @param keyPassword           私钥密码
     * @param trustPath             信任库地址
     * @param trustStorePassword    信任库密码
     * @param timeOut               超时时间
     */
    public CustomApacheHttpClient(String keyPath, String keyStorePassword, String keyPassword, String trustPath, String trustStorePassword, int timeOut) {
        this(timeOut);
        this.trustPath = trustPath;
        this.trustStorePassword = trustStorePassword;
        this.keyPath = keyPath;
        this.keyStorePassword = keyStorePassword;
        this.keyPassword = keyPassword;
    }

    /**
     * SSL忽略信任证书
     *
     * @param keyPath           密钥库地址
     * @param keyStorePassword  密钥库文件密码
     * @param keyPassword       私钥密码
     * @param timeOut           超时时间
     */
    public CustomApacheHttpClient(String keyPath, String keyStorePassword, String keyPassword, int timeOut) {
        this(timeOut);
        this.keyPath = keyPath;
        this.keyStorePassword = keyStorePassword;
        this.keyPassword = keyPassword;
    }

    private void createClient() {
        this.client = HttpClientBuilder.create()
                .setRetryHandler(new DefaultHttpRequestRetryHandler())
                .setDefaultRequestConfig(config)
                .build();
    }

    private void createSSLClient() {
        if(keyPath != null && keyPassword != null && keyStorePassword != null && trustPath != null && trustStorePassword != null) {
            createSSLClientWithBothCert();
            return;
        }
        if(keyPath != null && keyPassword != null && keyStorePassword != null && trustPath == null && trustStorePassword == null) {
            createSSLClientWithBothNoTrustCert();
            return;
        }
        if(keyPath == null && keyPassword == null && keyStorePassword == null && trustPath == null && trustStorePassword == null) {
            createSSLClientWithSingleNoCert();
        }
    }

    /**
     * https单向认证忽略服务器证书
     */
    private void createSSLClientWithSingleNoCert() {
        try {
            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(new org.apache.http.conn.ssl.TrustStrategy() {
                        @Override
                        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            return true;
                        }
                    }).build();

            // 这里填多个协议时会报 handshake_failure 异常
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
                    new String[]{"TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", sslsf)
                    .build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);

            this.client = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .setConnectionManager(cm)
                    .setRetryHandler(new DefaultHttpRequestRetryHandler())
                    .setConnectionManagerShared(true)
                    .build();
        } catch (Exception e) {
            logger.error("error", e);
        }
    }

    /**
     * https双向认证忽略服务器证书
     */
    private void createSSLClientWithBothNoTrustCert() {
        FileInputStream keyStoreFile = null;
        try {
            // 密钥库
            keyStoreFile = new FileInputStream(new File(keyPath));
        } catch (FileNotFoundException e1) {
            logger.error("Error:", e1);
        }

        // 服务端支持的安全协议
        String[] supportedProtocols = { "TLSv1.2" };
        SSLConnectionSocketFactory socketFactory = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(getKstype(keyPath));

            keyStore.load(keyStoreFile, keyStorePassword.toCharArray());

            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(new org.apache.http.conn.ssl.TrustStrategy() {
                        @Override
                        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            return true;
                        }
                    }).loadKeyMaterial(keyStore, keyPassword.toCharArray()).build();

            socketFactory = new SSLConnectionSocketFactory(sslContext, supportedProtocols, null,
                    NoopHostnameVerifier.INSTANCE);

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", socketFactory)
                    .build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);

            this.client = HttpClients.custom()
                    .setSSLSocketFactory(socketFactory)
                    .setConnectionManager(cm)
                    .setConnectionManagerShared(true)
                    .setDefaultRequestConfig(config)
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                    .build();
        } catch (Exception e) {
            logger.error("Error:", e);
        }
    }

    /**
     * https双向认证带证书
     */
    private void createSSLClientWithBothCert() {
        FileInputStream trustStoreFile = null;
        FileInputStream keyStoreFile = null;
        try {
            // 信任库
            trustStoreFile = new FileInputStream(new File(trustPath));
            // 密钥库
            keyStoreFile = new FileInputStream(new File(keyPath));
        } catch (FileNotFoundException e1) {
            logger.error("Error:", e1);
        }

        // 服务端支持的安全协议
        String[] supportedProtocols = { "TLSv1.2" };
        SSLConnectionSocketFactory socketFactory = null;
        try {
            KeyStore truststore = KeyStore.getInstance(getKstype(trustPath));
            KeyStore keyStore = KeyStore.getInstance(getKstype(keyPath));

            truststore.load(trustStoreFile, trustStorePassword.toCharArray());
            keyStore.load(keyStoreFile, keyStorePassword.toCharArray());

            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(truststore, null)
                    .loadKeyMaterial(keyStore, keyPassword.toCharArray()).build();
            socketFactory = new SSLConnectionSocketFactory(sslContext, supportedProtocols, null,
                    NoopHostnameVerifier.INSTANCE);

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", socketFactory)
                    .build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);

            this.client = HttpClients.custom()
                    .setSSLSocketFactory(socketFactory)
                    .setConnectionManager(cm)
                    .setConnectionManagerShared(true)
                    .setDefaultRequestConfig(config)
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                    .build();
        } catch (Exception e) {
            logger.error("Error:", e);
        }
    }

    private String getKstype(String certName) {
        if(certName.endsWith(".p12")) {
            return KEY_STORE_TYPE_P12;
        }
        return KEY_STORE_TYPE_JKS;
    }

    /**
     * POST/FORM请求，HTTP、HTTPS均可；SSL需要证书，暂只实现双向认证
     *
     * @param url
     * @param formparams
     * @param charset
     * @return
     * @throws Exception
     */
    public String doPost(String url, Map<String, String> formparams, String charset) throws Exception {
        if(url.substring(0, 5).equalsIgnoreCase("https")) {
            createSSLClient();
        }else {
            createClient();
        }
        if(this.client == null) {
            throw new BizException("Failed to create Http client");
        }

        HttpPost httpPost = new HttpPost(url);
        // 创建参数队列
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keys = formparams.keySet();
        for(String key : keys) {
            nvps.add(new BasicNameValuePair(key, formparams.get(key)));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, charset));

        String recvData = null;
        CloseableHttpResponse response = null;
        try {
            httpPost.setConfig(config);
            response = client.execute(httpPost) ;
            HttpEntity entity = response.getEntity();
            int respCode = response.getStatusLine().getStatusCode();
            if (respCode != HttpStatus.SC_OK) {
                throw new BizException("Failed to execute Http request: " + respCode);
            }

            if (entity != null) {
                recvData = EntityUtils.toString(entity, "utf-8");
                logger.info("Response content: " + recvData);
            }
            EntityUtils.consume(entity);
       }finally {
            if(response != null) {
                response.close();
            }
            this.client.close();
        }
        return recvData;
    }

    /**
     * POST，application/json请求，HTTP、HTTPS均可；SSL需要证书，暂只实现双向认证
     *
     * @param url
     * @param jsonReq
     * @param charset
     * @return
     * @throws Exception
     */
    public String doPost(String url, String jsonReq, String charset) throws Exception {
        if(url.substring(0, 5).equalsIgnoreCase("https")) {
            createSSLClient();
        }else {
            createClient();
        }
        if(this.client == null) {
            throw new BizException("Failed to create Http client");
        }

        HttpPost httpPost = new HttpPost(url);

        StringEntity reqEntity = new StringEntity(jsonReq, charset);   //解决中文乱码问题
        reqEntity.setContentEncoding(charset);
        reqEntity.setContentType("application/json");
        httpPost.setEntity(reqEntity);

        String recvData = null;
        CloseableHttpResponse response = null;
        try {
            httpPost.setConfig(config);
            response = client.execute(httpPost) ;
            HttpEntity entity = response.getEntity();
            int respCode = response.getStatusLine().getStatusCode();
            if (respCode != HttpStatus.SC_OK) {
                throw new BizException("Failed to execute Http request: " + respCode);
            }

            if (entity != null) {
                recvData = EntityUtils.toString(entity, "utf-8");
                logger.info("Response content: " + recvData);
            }
            EntityUtils.consume(entity);
        }finally {
            if(response != null) {
                response.close();
            }
            this.client.close();
        }
        return recvData;
    }

    /**
     * GET请求
     *
     * @param url
     * @param charset
     * @return
     * @throws Exception
     */
    public String doGet(String url, String charset) throws Exception {
        if(url.substring(0, 5).equalsIgnoreCase("https")) {
            createSSLClient();
        }else {
            createClient();
        }
        if(this.client == null) {
            throw new BizException("Failed to create Http client");
        }

        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(config);

        String recvData = null;
        CloseableHttpResponse response = null;
        try{

            response = this.client.execute(httpGet);
            int respCode = response.getStatusLine().getStatusCode();

            if(respCode != HttpStatus.SC_OK) {
                throw new BizException("Failed to execute Http request,error code: " + respCode);
            }
            HttpEntity resEntity = response.getEntity();
            if(resEntity != null){
                recvData = EntityUtils.toString(resEntity,charset);
            }
        }finally {
            if(response != null) {
                response.close();
            }
            this.client.close();
        }
        return recvData;
    }
}
