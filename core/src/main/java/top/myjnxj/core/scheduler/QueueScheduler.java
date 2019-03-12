package top.myjnxj.core.scheduler;


import top.myjnxj.core.model.Request;
import top.myjnxj.core.model.Task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName QueueScheduler
 * @Description TODO
 * @Author JNXJ
 * @Date 2019-03-11 20:49
 * @Version 1.0.0
 **/
public class QueueScheduler implements Scheduler {
    private BlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();

    public void push(Request request, Task task) {
            queue.add(request);
    }

    public Request poll(top.myjnxj.core.model.Task task) {
        return queue.poll();
    }
}
