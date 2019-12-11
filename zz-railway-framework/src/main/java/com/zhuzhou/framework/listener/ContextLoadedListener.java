package com.zhuzhou.framework.listener;

import com.zhuzhou.framework.utils.spring.ApplicationContextUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * context监听器
 * @author chenzeting
 * @since 2019-08-21
 */
@Component
public class ContextLoadedListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContextUtils.setContext(event.getApplicationContext());
    }
}
