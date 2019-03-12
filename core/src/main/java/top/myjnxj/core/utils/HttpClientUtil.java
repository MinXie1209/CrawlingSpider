package top.myjnxj.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import top.myjnxj.core.model.Request;

import java.io.IOException;
import java.util.List;


@Slf4j
public class HttpClientUtil {


    public static void main(String[] args) throws IOException {
        String url = "http://central.maven.org/maven2/";
        String result = HttpClientUtil.get(new Request("http://www.baidu.com"));
        log.info("url:{},  result:{}", url, result);
    }

    public static String get(Request request) throws IOException {
        String body = null;
        //创建httpClient实例
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = null;
        List<NameValuePair> params=request.getParams();
        String url=request.getUrl();
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
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
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
}