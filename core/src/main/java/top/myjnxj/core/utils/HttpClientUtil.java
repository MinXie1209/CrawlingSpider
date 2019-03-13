package top.myjnxj.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import top.myjnxj.core.Spider;
import top.myjnxj.core.model.Request;
import top.myjnxj.core.model.Task;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Slf4j
public class HttpClientUtil {

    private static HttpClient client = null;

    public static void main(String[] args) throws IOException {
        while (true){
            String url = "https://www.toutiao.com//";
            String result = HttpClientUtil.get(new Request(url), new Spider());
            log.info("url:{},  result:{}", url, result);
        }

    }


    public static String get(Request request, Task task) throws IOException {
        String body = null;
        //创建httpClient实例
        HttpClient httpClient = getClient();
        HttpGet httpGet = null;
        List<NameValuePair> params = request.getParams();
        String url = request.getUrl();
        if (params != null) {
            StringBuilder stringBuilder = new StringBuilder(url);
            stringBuilder.append("?");
            for (NameValuePair param : params) {
                stringBuilder.append(param.getName());
                stringBuilder.append("=");
                stringBuilder.append(param.getValue());
            }
            url = stringBuilder.toString();
        } else {
            //get
            httpGet = new HttpGet(url);
        }
        if(task!=null&&task.getSite()!=null){
            Map<String,String> map=task.getSite().getHeaders();
            for (String key:map.keySet()){
                httpGet.addHeader(key,map.get(key));
            }
            httpGet.setHeader("User-Agent",task.getSite().getUserAgent());
        }
        /*HttpHost proxy=new HttpHost("119.122.212.110",9000);
        RequestConfig config= RequestConfig.custom()
                .setProxy(proxy)
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .build();
        httpGet.setConfig(config);*/
        CloseableHttpResponse response = null;
        response = (CloseableHttpResponse) httpClient.execute(httpGet);
        HttpEntity httpEntity = response.getEntity();
        // log.info("content-type:{}", httpEntity.getContentType());

        //  InputStream in=httpEntity.getContent();
        //  FileUtils.copyToFile(in,new File("D://123.png"));
        body = EntityUtils.toString(httpEntity, "utf-8");
        response.close();
        ((CloseableHttpClient) httpClient).close();
        return body;
    }

    private static HttpClient getClient() {
        if (client == null) {
           PoolingHttpClientConnectionManager connectionManager=new PoolingHttpClientConnectionManager();
           connectionManager.setMaxTotal(200);
           connectionManager.setDefaultMaxPerRoute(100);
           CloseableHttpClient httpClient=HttpClients.custom()
                   .setConnectionManager(connectionManager)
                   .setConnectionManagerShared(true)
                   .build();
           client=httpClient;
        }
        return client;
    }
}