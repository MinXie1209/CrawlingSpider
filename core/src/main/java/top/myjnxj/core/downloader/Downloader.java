package top.myjnxj.core.downloader;

import top.myjnxj.core.Spider;
import top.myjnxj.core.model.Page;
import top.myjnxj.core.model.Request;

import java.io.IOException;

public interface Downloader {
    Page download(Request request, Spider spider) throws IOException;
}
