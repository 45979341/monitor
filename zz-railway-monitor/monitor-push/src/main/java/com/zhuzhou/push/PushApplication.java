package com.zhuzhou.push;

import com.zhuzhou.framework.config.ServletConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @Author: chenzeting
 * Date:     2019-12-05
 * Description:
 */
@ComponentScan({
    "com.zhuzhou.push",
    "com.zhuzhou.impl",
    "com.zhuzhou.spi",
    "com.zhuzhou.framework",
    "com.zhuzhou.dao"
})
@MapperScan(value="com.zhuzhou.dao")
@Import({
    ServletConfigure.class
})
@SpringBootApplication
public class PushApplication {
    public static void main(String[] args) {
        SpringApplication.run(PushApplication.class);
    }
}
