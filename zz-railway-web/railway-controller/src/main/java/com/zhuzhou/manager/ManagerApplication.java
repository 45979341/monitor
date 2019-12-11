package com.zhuzhou.manager;

import com.zhuzhou.framework.config.CacheConfigure;
import com.zhuzhou.framework.config.RedisConfigure;
import com.zhuzhou.framework.config.ServletConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @Author: chenzeting
 * Date:     2018/10/24
 * Description:
 */
@ComponentScan({
    "com.zhuzhou.manager",
    "com.zhuzhou.impl",
    "com.zhuzhou.spi",
    "com.zhuzhou.framework",
    "com.zhuzhou.dao",
    "com.zhuzhou.phase"
})
@MapperScan(value="com.zhuzhou.dao")

@Import({
    ServletConfigure.class,
    RedisConfigure.class,
    CacheConfigure.class,
})
//@EnableScheduling
@SpringBootApplication
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class);
    }
}
