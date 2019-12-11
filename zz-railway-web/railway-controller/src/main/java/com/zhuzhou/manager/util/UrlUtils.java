package com.zhuzhou.manager.util;

import com.zhuzhou.dao.sys.SysMenuMapper;
import com.zhuzhou.entity.sys.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlUtils {

    private WebApplicationContext applicationContext;

    private SysMenuMapper mapper;

    public List<SysMenu> getAllUrl() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> methodMap = mapping.getHandlerMethods();
        List<SysMenu> list = new ArrayList<>();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : methodMap.entrySet()) {
            SysMenu menu = new SysMenu();
            RequestMappingInfo info = entry.getKey();
            String name = info.getName();
            menu.setName(name);
            PatternsRequestCondition con = info.getPatternsCondition();
            for (String pattern : con.getPatterns()) {
                menu.setUrl(pattern);
                String temp = pattern.replace("/", ":");
                menu.setPermission(temp.substring(temp.indexOf(":")+1));
                list.add(menu);
                break;
            }
        }
        return list;
    }

    public void addUrlIntoDb() {
        List<SysMenu> allUrl = this.getAllUrl();
        for (SysMenu menu : allUrl) {
            mapper.insert(menu);
        }
    }

}
