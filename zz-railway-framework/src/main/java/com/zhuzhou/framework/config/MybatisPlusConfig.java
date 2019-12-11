package com.zhuzhou.framework.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.zhuzhou.framework.interceptor.SqlInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * mybatis配置
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

//    @Bean
//    public SqlInterceptor sqlInterceptor() {
//        return new SqlInterceptor();
//    }

}
