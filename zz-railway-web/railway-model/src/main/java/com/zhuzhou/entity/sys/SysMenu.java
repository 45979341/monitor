package com.zhuzhou.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 * 菜单权限表
 * </p>
 * @author chenzeting
 * @since 2019-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_menu")
public class SysMenu implements Serializable {

    /**
     * 菜单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父菜单ID
     */
    private Integer parentId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 类型: (1:菜单，2：按钮，3：接口)
     */
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单状态: (0：显示，1：隐藏)
     */
    private Integer status;

    /**
     * 禁用缓存
     */
    private Integer noCache;

    /**
     * 组件路径
     */
    private String componentPath;

    /**
     * 始终显示根菜单
     */
    private Integer alwaysShow;

    /**
     * 隐藏
     */
    private Integer hidden;

    /**
     * 标记视图
     */
    private Integer affix;

    /**
     * 重定向
     */
    private String redirect;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @TableField(select = false)
    private Timestamp createTime;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<SysMenu> children;

}
