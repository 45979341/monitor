package com.zhuzhou.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuzhou.entity.sys.SysMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-08
 */
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 查询菜单（需要的字段）
     * @return
     */
    List<SysMenu> menuList();

    /**
     * 子菜单数
     * @param menuId
     * @return
     */
    Integer submenu(String menuId);
}
