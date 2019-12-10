package com.video.data.utils;

import com.video.data.model.HttpModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 利用HttpClient封装 get 和 post请求
 * Created by xiaoxia on 2018/2/23 0023.
 */
public class HttpRequest {
    private final static Log log = LogFactory.getLog(HttpRequest.class);
    private static final String GZIP_CONTENT_TYPE = "application/x-gzip";
    private static final String DEFUALT_CONTENT_TYPE = "application/x-www-form-urlencoded; charset=UTF-8";
    public static final String USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1";

    public static final int default_socketTimeout = 10000000;

    private HttpRequest(){}

    /**
     * 发送HTTP_POST请求  时默认采用UTF-8解码
     * 该方法会自动对<code>params</code>中的[中文][|][ ]等特殊字符进行<code>URLEncoder.encode(string,encodeCharset)</code>
     * @param reqURL  请求的url
     * @param params 请求参数
     * @return
     */
    public static HttpModel sendPostRequest(String reqURL, Map<String, String> params){
        return sendPostRequest(reqURL, params, false, null,
                null, null, null);
    }

    public static HttpModel sendPostRequest(String reqURL, Map<String, String> params, String proxyIpInfo){
        return sendPostRequest(reqURL, params, false, null,
                null, null, proxyIpInfo);
    }

    /**
     * 发送HTTP_GET请求
     * @param reqURL 请求的url地址(含参数),默认采用utf-8编码
     * @return
     */
    public static HttpModel sendGetRequest(String reqURL){
        return sendGetRequest(reqURL,null,false);
    }

    public static HttpModel sendGetRequest(String reqURL, String encoding, Boolean inGZIP) {
        return sendGetRequest(reqURL, encoding, inGZIP, null, "");
    }

    public static HttpModel sendGetRequest(String reqURL, Map<String, String> header) {
        return sendGetRequest(reqURL, null, false, header, "");
    }

    /**
     * @param reqURL
     * @param header
     * @param proxyIpInfo
     * @return
     */
    public static HttpModel sendGetRequest(String reqURL, Map<String, String> header, String proxyIpInfo) {
        return sendGetRequest(reqURL, null, false, header, proxyIpInfo);
    }

    /**
     * 代理IP格式：http:42.203.38.176:27681
     * @param reqURL string
     * @param encoding string
     * @param inGZIP bool
     * @param header map
     * @param proxyIp string
     * @return HttpModel
     */
    public static HttpModel sendGetRequest(String reqURL, String encoding, Boolean inGZIP, Map<String, String> header, String proxyIp) {
        HttpModel httpModel = new HttpModel();
        RequestConfig requestConfig = getRequestConfig(proxyIp);
        CloseableHttpClient httpClient = getHttpClient(requestConfig);
        long responseLength = 0;//响应长度
        String responseContent = null; //响应内容
        HttpGet httpGet = new HttpGet(reqURL);
        if(inGZIP){
            httpGet.setHeader(HTTP.CONTENT_TYPE,GZIP_CONTENT_TYPE);
        }
        httpGet.setHeader(HTTP.USER_AGENT, USER_AGENT);
        httpGet.setConfig(requestConfig);
        if(header != null) {
            for (Map.Entry entry : header.entrySet()) {
                httpGet.setHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        try {
            trustEveryone();
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                if (null != entity) {
                    responseLength = entity.getContentLength();

                    if (inGZIP) {
                        responseContent = unGZipContent(entity, encoding == null ? "UTF-8" : encoding);
                    } else {
                        responseContent = EntityUtils.toString(entity, encoding == null ? "UTF-8" : encoding);
                    }

                    close(entity);
                }
                log.debug("请求地址: " + httpGet.getURI());
                log.debug("响应状态: " + response.getStatusLine());
                log.debug("响应长度: " + responseLength);
                log.debug("响应内容: " + responseContent);
                httpModel.setCode(response.getStatusLine().getStatusCode());
                httpModel.setContent(responseContent);
            }
            //关闭连接,释放资源
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpModel;
    }

    public static HttpModel sendPostRequest(String reqURL, Map<String, String> params, Boolean gzip, String encodeCharset,
                                         String decodeCharset, Map<String, String> header, String proxyIp) {
        String responseContent = null;
        RequestConfig requestConfig = getRequestConfig(proxyIp);
        CloseableHttpClient httpClient = getHttpClient(requestConfig);
        HttpPost httpPost = new HttpPost(reqURL);
        if(gzip){
            httpPost.setHeader(HTTP.CONTENT_TYPE,GZIP_CONTENT_TYPE);
        }
        httpPost.setHeader(HTTP.USER_AGENT, USER_AGENT);

        List<NameValuePair> formParams = new ArrayList<NameValuePair>(); //创建参数队列
        Set<Map.Entry<String, String>>  paramSet = params.entrySet();
        HttpModel httpModel = new HttpModel();
        if(paramSet.size() > 0){
            for(Map.Entry<String,String> entry : paramSet){
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        log.debug("send params:"+formParams.toString());
        if(header != null) {
            for (Map.Entry entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        try {
            // 设置参数
            httpPost.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset==null ? "UTF-8" : encodeCharset));

            httpPost.setConfig(requestConfig);
            trustEveryone();
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                if (null != entity) {
                    String contentType = "";
                    Header[] headers = httpPost.getHeaders(HTTP.CONTENT_TYPE);
                    if (headers != null && headers.length > 0) {
                        contentType = headers[0].getValue();
                    }

                    if (contentType.equalsIgnoreCase(GZIP_CONTENT_TYPE)) {
                        responseContent = unGZipContent(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                    } else {
                        responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                    }
                    close(entity);
                }

                log.debug("请求地址: " + httpPost.getURI());
                log.debug("响应状态: " + response.getStatusLine());
                log.debug("响应内容: " + responseContent);
                httpModel.setCode(response.getStatusLine().getStatusCode());
                httpModel.setContent(responseContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpModel;
    }

    private static CloseableHttpClient getHttpClient(RequestConfig requestConfig) {
        SSLContextBuilder builder = new SSLContextBuilder();
        SSLConnectionSocketFactory sslConnectionSocketFactory = null;
        try {
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(),
                    NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException ignored) { }
        //不进行主机名验证
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", sslConnectionSocketFactory)
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(10000);
        return HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setDefaultCookieStore(new BasicCookieStore())
                .setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig).build();
    }

    private static RequestConfig getRequestConfig(String proxyIp) {
        RequestConfig requestConfig;
        if(!StringUtils.isEmpty(proxyIp)) {
            String[] proxyInfo = proxyIp.split(":");
            HttpHost proxy = new HttpHost(proxyInfo[1], Integer.parseInt(proxyInfo[2]), proxyInfo[0]);
            requestConfig = RequestConfig.custom()
                    .setSocketTimeout(default_socketTimeout)
                    .setConnectTimeout(default_socketTimeout)
                    .setProxy(proxy).build();
        }else{
            requestConfig = RequestConfig.custom()
                    .setSocketTimeout(default_socketTimeout)
                    .setConnectTimeout(default_socketTimeout).build();
        }
        return requestConfig;
    }

    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压
     * @param entity
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String unGZipContent(HttpEntity entity,String encoding) throws IOException {
        String responseContent = "";
        GZIPInputStream gis = new GZIPInputStream(entity.getContent());
        int count = 0;
        byte data[] = new byte[1024];
        while ((count = gis.read(data, 0, 1024)) != -1) {
            String str = new String(data, 0, count,encoding);
            responseContent += str;
        }
        return responseContent;
    }

    /**
     * 压缩
     * @param sendData
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream gZipContent(String sendData) throws IOException{
        if (StringUtils.isBlank(sendData)) {
            return null;
        }

        ByteArrayOutputStream originalContent = new ByteArrayOutputStream();
        originalContent.write(sendData.getBytes("UTF-8"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzipOut = new GZIPOutputStream(baos);
        originalContent.writeTo(gzipOut);
        gzipOut.close();
        return baos;
    }

    public static void close(HttpEntity entity) throws IOException {
        if (entity == null) {
            return;
        }
        if (entity.isStreaming()) {
            final InputStream instream = entity.getContent();
            if (instream != null) {
                instream.close();
            }
        }
    }

}
