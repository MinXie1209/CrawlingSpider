package top.myjnxj.core.processor;

import top.myjnxj.core.model.Page;
import top.myjnxj.core.model.Site;

public interface PageProcessor {
    void process(Page page);
    Site getSite();
}
