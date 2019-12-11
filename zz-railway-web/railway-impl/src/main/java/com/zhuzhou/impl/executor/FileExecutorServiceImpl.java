package com.zhuzhou.impl.executor;

import com.zhuzhou.spi.executor.FileExecutorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author chenzeting
 * @Date 2019-03-25
 * @Description:
 **/
@Service
public class FileExecutorServiceImpl implements FileExecutorService {

//    @Value("${file.pool.size:10}")
//    private int poolSize;

    /**
     * 线程池
     */
    private ThreadPoolExecutor smsExecutor = new ThreadPoolExecutor(10, 10,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    @Override
    public void exec () {
        smsExecutor.execute(new FileThread());
    }

    /**
     * 该类为多线程类，用于服务端
     */
    @AllArgsConstructor
    public class FileThread implements Runnable {

        @Override
        public void run() {
        }
    }
}
