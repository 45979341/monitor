package com.zhuzhou.spi.sys;

import com.zhuzhou.entity.sys.SysMenu;
import com.zhuzhou.spi.BaseService;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-08
 */
public interface SysMenuService extends BaseService<SysMenu> {
    /**
     * 根据用户返回菜单树
     *
     * @param id
     * @return
     */
    List<SysMenu> menuTree(String id);

    /**
     * 返回全部菜单（需要的字段）
     *
     * @return
     */
    List<SysMenu> menuList();

    /**
     * 是否存在子菜单
     *
     * @param id
     * @return
     */
    Integer submenu(String id);

    /**
     * 菜单删除
     * @param menuId
     */
    void delete(String menuId);
}
