package com.zhuzhou.impl.sys;

import com.zhuzhou.consts.SymbolConst;
import com.zhuzhou.dao.sys.SysMenuMapper;
import com.zhuzhou.entity.sys.SysMenu;
import com.zhuzhou.entity.sys.SysRole;
import com.zhuzhou.entity.sys.SysUser;
import com.zhuzhou.framework.exception.ApplicationException;
import com.zhuzhou.framework.exception.ErrorResponseMsgEnum;
import com.zhuzhou.impl.BaseServiceImpl;
import com.zhuzhou.spi.sys.SysMenuService;
import com.zhuzhou.spi.sys.SysRoleService;
import com.zhuzhou.spi.sys.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-08
 */
@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public List<SysMenu> menuTree(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        SysUser user = sysUserService.getById(id);
        String roleIds = user.getRoleIds();
        if (StringUtils.isEmpty(roleIds)) {
            return null;
        }
        List<SysMenu> menuList = list();
        List<String> split = Arrays.stream(roleIds.split(SymbolConst.COMMA)).collect(Collectors.toList());
        Set<String> menuId = new HashSet<>();
        // 获取全部菜单
        for (String roleId : split) {
            SysRole role = sysRoleService.getById(roleId);
            if (role == null) {
                continue;
            }
            String menuIds = role.getMenuId();
            if (StringUtils.isEmpty(menuIds)) {
                continue;
            }
            String[] splitMenu = menuIds.split(SymbolConst.COMMA);
            //权限 去除重复
            menuId.addAll(Arrays.stream(splitMenu).collect(Collectors.toSet()));
        }
        //角色菜单列表包装
        return menuList.stream().filter(menu -> menuId.contains(menu.getId().toString())).collect(Collectors.toList());
    }

    @Override
    public List<SysMenu> menuList() {
        return sysMenuMapper.menuList();
    }

    @Override
    public Integer submenu(String menuId) {
        return sysMenuMapper.submenu(menuId);
    }


    @Override
    public void delete(String menuId) {
        SysMenu menu = getById(menuId);
        if (menu == null) {
            throw new ApplicationException(ErrorResponseMsgEnum.MENU_NOT_EXISTS);
        }
        if (submenu(menuId) > 0) {
            throw new ApplicationException(ErrorResponseMsgEnum.MENU_HAVE_CHILDREN);
        }
        List<SysRole> roles = sysRoleService.list();
        for (SysRole role : roles) {
            String[] split = role.getMenuId().split(",");
            for (String id : split) {
                if (menuId.equals(id)) {
                    throw new ApplicationException(ErrorResponseMsgEnum.MENU_IN_USE);
                }
            }
        }
        removeById(menuId);
    }
}
