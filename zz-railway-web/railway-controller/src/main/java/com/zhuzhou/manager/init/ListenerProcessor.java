package com.zhuzhou.manager.init;

import com.zhuzhou.entity.sys.SysMenu;
import com.zhuzhou.spi.sys.SysMenuService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RestController
public class ListenerProcessor implements BeanPostProcessor {

    public static List<SysMenu> list = new ArrayList<>();

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        if (methods != null) {
            for (Method method : methods) {
                GetMapping annotation = AnnotationUtils.findAnnotation(method, GetMapping.class);
                if (null != annotation) {
                    String url = annotation.value()[0];
                    String name = annotation.name();
                    String permission = url.substring(1).replace("/", ":");
                    list.add(toBean(name, url, permission));
                } else {
                    PostMapping annotation2 = AnnotationUtils.findAnnotation(method, PostMapping.class);
                    if (null != annotation2) {
                        String url = annotation2.value()[0];
                        String name = annotation2.name();
                        String permission = url.substring(1).replace("/", ":");
                        list.add(toBean(name, url, permission));
                    }
                }
            }
        }
        return bean;
    }

    private SysMenu toBean(String name, String url, String permission) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setName(name);
        sysMenu.setUrl(url);
        sysMenu.setPermission(permission);
        sysMenu.setType(1);
        sysMenu.setParentId(1);
        return sysMenu;
    }

    @GetMapping("/insertapi")
    public void sort() {
        List<SysMenu> collect = list.stream().sorted(Comparator.comparing(SysMenu::getPermission)).collect(Collectors.toList());

        // sysUserController的接口会出现两次？删掉一半
        collect.remove(74);
        collect.remove(75);
        collect.remove(76);
        collect.remove(77);
        collect.remove(78);
        collect.remove(79);
        collect.remove(80);
        collect.remove(81);

        int a = 1;
        collect.get(0).setSort(a);
        for (int i = 1; i < collect.size(); i++) {
            boolean flag;
            if (collect.get(i).getPermission().startsWith("sys")) {
                flag = collect.get(i).getPermission().substring(0, 5)
                        .equals(collect.get(i - 1).getPermission().substring(0, 5));
            } else {
                flag = collect.get(i).getPermission().substring(0, 3)
                        .equals(collect.get(i - 1).getPermission().substring(0, 3));
            }
            if (flag) {
                a++;
                collect.get(i).setSort(a);
            } else {
                a = 1;
                collect.get(i).setSort(a);
            }
        }
        for (SysMenu sysMenu : collect) {
            System.out.println(sysMenu.getPermission() + "\t" + sysMenu.getSort());
        }
        // 插入数据库
        // sysMenuService.saveBatch(collect);
        collect.clear();
    }
}