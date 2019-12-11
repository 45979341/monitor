package com.zhuzhou.manager;

import com.zhuzhou.framework.config.CacheConfigure;
import com.zhuzhou.framework.config.RedisConfigure;
import com.zhuzhou.framework.config.ServletConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

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
    "com.zhuzhou.dao"
})
@MapperScan(value="com.zhuzhou.dao")
@EnableElasticsearchRepositories(basePackages = "com.zhuzhou.dao.es")
@Import({
    ServletConfigure.class,
    RedisConfigure.class,
    CacheConfigure.class
})
//@EnableScheduling
@EnableAsync
@SpringBootApplication
public class MonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonitorApplication.class);
    }
}
