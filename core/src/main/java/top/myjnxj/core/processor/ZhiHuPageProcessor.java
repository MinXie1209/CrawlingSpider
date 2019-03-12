package top.myjnxj.core.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.myjnxj.core.Spider;
import top.myjnxj.core.model.Page;
import top.myjnxj.core.model.Request;
import top.myjnxj.core.model.Site;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName ZhiHuPageProcessor
 * @Description TODO
 * @Author JNXJ
 * @Date 2019-03-11 20:19
 * @Version 1.0.0
 **/
@Slf4j
public class ZhiHuPageProcessor implements PageProcessor {
    private Site site = Site
            .me()
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
    private static Queue<String> oldUrls=new LinkedBlockingQueue<String>();
    public void process(Page page) {
        oldUrls.add(page.getRequest().getUrl());
        //log.info(page.getContent());
        Elements links = page.getDocument().select("a[href]");
        for (Element link:links){
            String url=link.attr("abs:href");
           // log.info("link:{}",url);
            if (oldUrls.contains(url)){
               // log.info("queue contains {}",url);
            }else{
                if (StringUtils.isNotEmpty(url)&&StringUtils.startsWith(url,"https://www.doutula.com/")){
                    page.getTargetRequests().add(new Request(url));
                }

            }
        }
    }

    public static void main(String[] args) {
        Spider.create(new ZhiHuPageProcessor())
                .addUrl("https://www.doutula.com/")
                .thread(30)
                .run();
    }
}
