package top.myjnxj.core;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.myjnxj.core.downloader.DefaultDownloader;
import top.myjnxj.core.downloader.Downloader;
import top.myjnxj.core.model.CountableThreadPool;
import top.myjnxj.core.model.Page;
import top.myjnxj.core.model.Request;
import top.myjnxj.core.model.Task;
import top.myjnxj.core.processor.PageProcessor;
import top.myjnxj.core.scheduler.QueueScheduler;
import top.myjnxj.core.scheduler.Scheduler;


import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName Spider
 * @Description TODO
 * @Author JNXJ
 * @Date 2019-03-11 15:47
 * @Version 1.0.0
 **/
@Slf4j
@NoArgsConstructor
public class Spider implements Runnable, Task {
    private Downloader downloader;
    private PageProcessor pageProcessor;
    protected List<Request> startRequests;
    //队列,存储被爬取的队列
    private Scheduler scheduler = new QueueScheduler();
    private int threadNum = 1;
    private AtomicInteger stat = new AtomicInteger(STAT_INTT);
    private static final int STAT_INTT = 0;
    private static final int STAT_RUNNING = 1;
    private static final int STAT_STOPPED = 2;

    private ExecutorService executorService;
    private CountableThreadPool countableThreadPool;

    private ReentrantLock newUrlLock = new ReentrantLock();
    private Condition newUrlCondition = newUrlLock.newCondition();
    private String uuid;
    private AtomicInteger pageCount = new AtomicInteger(0);

    public Spider(PageProcessor pageProcessor) {
        this.pageProcessor = pageProcessor;
    }

    public static Spider create(PageProcessor pageProcessor) {
        return new Spider(pageProcessor);
    }

    private void processRequest(Request request) throws IOException {
        //页面下载
        Page page = downloader.download(request, this);
        //链接提取和页面分析
        pageProcessor.process(page);
        for (Request targetRequest:page.getTargetRequests()){
            scheduler.push(targetRequest,this);
        }
    }

    //URL管理
    private void addRequest(Request request) {
        scheduler.push(request, this);
    }


    public Spider addUrl(String... urls) {
        for (String url : urls) {
            addRequest(new Request(url));
        }
        return this;
    }

    public void run() {
        checkRunningStat();
        initSpider();
        log.info("Spider {} start....", getUUID());
        //start
        while (!Thread.currentThread().isInterrupted() && stat.get() == STAT_RUNNING) {
            final Request request = scheduler.poll(this);
            if (request == null) {
                if (countableThreadPool.getThreadAlive() == 0) {
                    break;
                }
                waitNewUrl();
            } else {
                countableThreadPool.execute(new Runnable() {
                    public void run() {
                        try {
                            processRequest(request);
                            onSucess(request);
                        } catch (Exception e) {
                            onError(request);
                            log.error("process request {} error {}", request, e);
                        } finally {
                            pageCount.incrementAndGet();
                            singalNewUrl();
                        }
                    }
                });
            }
        }
        stat.set(STAT_STOPPED);
        countableThreadPool.shutdown();
        log.info("Spider {} closed! {} pages downloaded.", getUUID(), pageCount.get());
    }

    private void onError(Request request) {
        log.error("Spider request {} has an error!", request);
    }

    private void onSucess(Request request) {
        log.info("Spider request number {} : {} has success!", pageCount.get(),request);
    }

    private void singalNewUrl() {
        //log.info("signalNewUrl...");
        newUrlLock.lock();
        newUrlCondition.signalAll();
        newUrlLock.unlock();
    }

    private void waitNewUrl() {
        //log.info("waitNewUrl...");
        newUrlLock.lock();
        try {
            if (countableThreadPool.getThreadAlive() == 0) {
                return;
            }
            newUrlCondition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            newUrlLock.unlock();
        }
    }

    /**
     * Check thread status,and set thread status
     */
    private void checkRunningStat() {
        while (true) {
            int statNow = stat.get();
            if (statNow == STAT_RUNNING) {
                throw new IllegalStateException("Spider is already running");
            }
            // update stat
            if (stat.compareAndSet(statNow, STAT_RUNNING)) {
                break;
            }
        }

    }

    /**
     * 初始化
     * 1.初始化成员变量
     */
    private void initSpider() {
        if (downloader == null) {
            downloader = new DefaultDownloader();
        }
        //多线程下载
        //downloader.setThread(threadNum);
        if (startRequests != null) {
            for (Request request : startRequests) {
                addRequest(request);
            }
        }
        if (executorService != null) {
            countableThreadPool = new CountableThreadPool(threadNum, executorService);
        } else {
            countableThreadPool = new CountableThreadPool(threadNum);
        }
    }

    public Spider thread(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    public String getUUID() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
        return this.uuid;
    }
}
