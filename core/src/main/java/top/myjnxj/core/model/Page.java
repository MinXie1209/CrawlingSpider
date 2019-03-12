package top.myjnxj.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Page
 * @Description TODO
 * @Author JNXJ
 * @Date 2019-03-11 15:55
 * @Version 1.0.0
 **/
@Data
@NoArgsConstructor
public class Page {
    private String content;
    private Request request;
    private Document document;
    private List<Request> targetRequests = new ArrayList<Request>();
    public Page(String url) {
        this.request=new Request(url);
    }
}
