package com.wm.service.utils;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 测试httpclient 4.0 1. 重新设计了HttpClient 4.0 API架构，彻底从内部解决了所有 HttpClient 3.x
 * 已知的架构缺陷代码。 2. HttpClient 4.0 提供了更简洁，更灵活，更明确的API。 3. HttpClient 4.0
 * 引入了很多模块化的结构。 4. HttpClient
 * 4.0性能方面得到了不小的提升，包括更少的内存使用，通过使用HttpCore模块更高效完成HTTP传输。 5. 通过使用 协议拦截器(protocol
 * interceptors), HttpClient 4.0实现了 交叉HTTP（cross-cutting HTTP protocol） 协议 6.
 * HttpClient 4.0增强了对连接的管理，更好的处理持久化连接，同时HttpClient 4.0还支持连接状态 7. HttpClient
 * 4.0增加了插件式（可插拔的）的 重定向（redirect） 和 验证（authentication）处理。 8. HttpClient
 * 4.0支持通过代理发送请求，或者通过一组代理发送请求。 9. 更灵活的SSL context 自定义功能在HttpClient 4.0中得以实现。 10.
 * HttpClient 4.0减少了在省城HTTP请求 和 解析HTTP响应 过程中的垃圾信息。 11. HttpClient团队鼓励所有的项目升级成
 * HttpClient 4.0
 * 
 * @author mx.li
 */
public class HttpClientUtils {

    private HttpClient client;
    private StringBuffer url = new StringBuffer();
    private String encoding = "UTF-8";
    private List<NameValuePair> params = new ArrayList<NameValuePair>();

    public HttpClientUtils(String url) {
        this.url.append(url);
        client = new DefaultHttpClient();
        HttpParams params = client.getParams();
        params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, encoding);
        params.setParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, encoding);
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);// 链接时间
        HttpConnectionParams.setSoTimeout(params, 2 * 60 * 1000);// 请求时间
    }

    public String doPostWithNoParamsName(String content) {
        String result = null;
        try {
            HttpResponse response = null;
            HttpPost postMethod = new HttpPost(this.url.toString());
            StringEntity se = new StringEntity(content, "utf-8");
            se.setContentEncoding("utf-8");
            se.setContentType("application/json");
            postMethod.setEntity(se);
            // HttpClient client = new DefaultHttpClient();
            response = client.execute(postMethod);
            result = EntityUtils.toString(response.getEntity(), "utf-8");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }

    public String doPost() {
        String result = null;
        try {
            HttpPost post = new HttpPost(this.url.toString());
            // modified by 庞焱鸣：由抛异常改为catch，防止控制台输出连接失败信息
            post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            result = getRespone(post);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }

    public void setParams(String key, Object value) {
        if (value != null) {
            params.add(new BasicNameValuePair(key, value.toString()));
        }
    }

    public void setInitUrl(String newUrl) {
        url.append(newUrl);
    }

    public String doGet() throws Exception {
        String result = "";
        try {
            int count = 0;
            for (NameValuePair nv : params) {
                if (count == 0) {
                    url.append("?");
                } else {
                    url.append("&");
                }
                String key = nv.getName();
                String value = nv.getValue();
                url.append(key);
                url.append("=");
                url.append(value);

                count++;
            }
            System.out.println(this.url.toString());
            HttpGet get = new HttpGet(this.url.toString());
            result = getRespone(get);
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }

    public String getUrl() throws Exception {
        int count = 0;
        for (NameValuePair nv : params) {
            if (count == 0) {
                url.append("?");
            } else {
                url.append("&");
            }
            String key = nv.getName();
            String value = nv.getValue();
            url.append(key);
            url.append("=");
            url.append(value);

            count++;
        }
        return this.url.toString();
    }

    private String getRespone(HttpUriRequest req) throws Exception {
        String result = "";
        // 查看默认request头部信息
        // System.out.println("Accept-Charset:" +
        // req.getFirstHeader("Accept-Charset"));
        // 以下这条如果不加会发现无论你设置Accept-Charset为gbk还是utf-8，他都会默认返回gb2312（本例针对google.cn来说）
        req.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.2)");
        // 用逗号分隔显示可以同时接受多种编码
        req.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
        req.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
        // 验证头部信息设置生效
        // System.out.println("Accept-Charset:" +
        // req.getFirstHeader("Accept-Charset").getValue());
        HttpResponse response;
        try {
            response = client.execute(req);
            org.apache.http.HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, encoding);
            }
        } catch (SocketTimeoutException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
        return result;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

}
