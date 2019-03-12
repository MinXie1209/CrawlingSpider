package top.myjnxj.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Request
 * @Description TODO
 * @Author JNXJ
 * @Date 2019-03-11 15:54
 * @Version 1.0.0
 **/
@Data
@NoArgsConstructor
public class Request {
    public static final String GET = "GET";
    public static final String POST = "POST";
    private String url;
    private List<NameValuePair> params;
    private String method;


    public Request(String url) {
        this.url = url;
    }


}
