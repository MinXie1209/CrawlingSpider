package top.myjnxj.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Site
 * @Description TODO
 * @Author JNXJ
 * @Date 2019-03-11 20:20
 * @Version 1.0.0
 **/
@Data
@NoArgsConstructor
public class Site {
    private String userAgent;
    private String domain;
    private Map<String, String> headers = new HashMap<String, String>();
    private String charset;
    public static Site me() {
        return new Site();
    }

    public Site addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }
    public Site setUserAgent(String userAgent){
        this.userAgent=userAgent;
        return this;
    }
    public Site setDomain(String domain){
        this.domain=domain;
        return this;
    }
    public Site setCharset(String charset){
        this.charset=charset;
        return this;
    }
}
