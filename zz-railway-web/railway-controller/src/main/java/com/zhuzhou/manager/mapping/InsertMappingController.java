package com.zhuzhou.manager.mapping;

import com.zhuzhou.entity.sys.SysMenu;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.spi.sys.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xch
 * @date 2019/9/25 14:08
 */
@RestController
public class InsertMappingController {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private RequestMappingHandlerMapping mapping;

    @GetMapping("/insertmapping")
    public Result insertBatch() {
        List<SysMenu> list = new ArrayList<>();
        Map<RequestMappingInfo, HandlerMethod> methodMap = mapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : methodMap.entrySet()) {
            SysMenu menu = new SysMenu();
            menu.setParentId(1);
            menu.setType(1);
            RequestMappingInfo info = entry.getKey();
            menu.setName(info.getName());
            PatternsRequestCondition methodsCondition = info.getPatternsCondition();
            for (String url : methodsCondition.getPatterns()) {
                menu.setUrl(url);
                String permission = url.substring(1).replace("/", ":");
                menu.setPermission(permission);
                list.add(menu);
                break;
            }
        }
        List<SysMenu> sortedList = sort(list);
        // 存入数据库
        // sysMenuService.saveBatch(sort(sortedList));
        return Result.success();
    }

    private List<SysMenu> sort(List<SysMenu> list) {
        List<SysMenu> sortedList = list.stream()
                .filter(sysMenu -> !sysMenu.getUrl().equals("/error"))
                .sorted(Comparator.comparing(SysMenu::getPermission))
                .collect(Collectors.toList());
        int a = 1;
        sortedList.get(0).setSort(a);
        for (int i = 1; i < sortedList.size(); i++) {
            boolean flag;
            if (sortedList.get(i).getPermission().startsWith("sys")) {
                flag = sortedList.get(i).getPermission().substring(0, 5)
                        .equals(sortedList.get(i - 1).getPermission().substring(0, 5));
            } else {
                flag = sortedList.get(i).getPermission().substring(0, 3)
                        .equals(sortedList.get(i - 1).getPermission().substring(0, 3));
            }
            if (flag) {
                a++;
                sortedList.get(i).setSort(a);
            } else {
                a = 1;
                sortedList.get(i).setSort(a);
            }
        }
        int i = 1;
        for (SysMenu sysMenu : sortedList) {
            System.out.println(i + "\t" + sysMenu.getPermission() + "\t" + sysMenu.getSort());
            i++;
        }
        return sortedList;
    }
}
