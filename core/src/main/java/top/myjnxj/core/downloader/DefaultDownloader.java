package top.myjnxj.core.downloader;

import org.jsoup.Jsoup;
import top.myjnxj.core.Spider;
import top.myjnxj.core.model.Page;
import top.myjnxj.core.model.Request;
import top.myjnxj.core.utils.HttpClientUtil;

import java.io.IOException;

/**
 * @ClassName DefaultDownloader
 * @Description TODO
 * @Author JNXJ
 * @Date 2019-03-11 16:01
 * @Version 1.0.0
 **/
public class DefaultDownloader implements Downloader {
    public Page download(Request request, Spider spider) throws IOException {
        Page page=new Page();
        page.setRequest(request);
        page.setContent(HttpClientUtil.get(request));
        page.setDocument(Jsoup.parse(page.getContent()));
        return page;
    }
}
