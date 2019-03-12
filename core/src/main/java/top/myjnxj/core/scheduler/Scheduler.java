package top.myjnxj.core.scheduler;


import top.myjnxj.core.model.Request;
import top.myjnxj.core.model.Task;

public interface Scheduler {
    void push(Request request, Task task);
    Request poll(Task task);
}
