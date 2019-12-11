package com.zhuzhou.push.utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 刘欣武
 * @version $Id: ThreadPoolUtil, v 0.1 2019/11/28 9:47 刘欣武 Exp$
 */
public class ThreadPoolUtil {

    private static ThreadPoolExecutor threadPoolExecutor = null;
    private static int corePoolSize = 20;
    private static int maximumPoolSize = 100;
    private static long keepAliveTime = 2L;

    private static BlockingQueue workQueue = new LinkedBlockingDeque(100);

    private static ThreadFactory threadFactory = new ThreadFactory() {
        private  final AtomicInteger threadNum = new AtomicInteger();
        public Thread newThread(Runnable runnable){
            return new Thread(runnable,"myThread:"+threadNum.getAndIncrement());
        }
    };

    static {
        if(threadPoolExecutor==null){
            threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,TimeUnit.SECONDS,
                    workQueue,threadFactory);
            threadPoolExecutor.prestartAllCoreThreads();
        }
    }

    public static void execute(Runnable command){
        threadPoolExecutor.execute(command);
    }

    public static <T>Future submit(Callable callable){
        return threadPoolExecutor.submit(callable);
    }

}


