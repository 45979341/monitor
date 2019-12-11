package com.zhuzhou.form.sys.menu;

import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import lombok.Data;

/**
 * @author xiechonghu
 * @date 2019/8/28 14:28
 * @description:
 */
@Data
public class SysMenuUpdateForm implements Form {
    /**
     * 父菜单ID,不填为0
     */
    private int parentId;

    /**
     * 排序
     */
    @NotEmpty(message = "请填写排序")
    private Integer sort;

    /**
     * 菜单名称
     */
    @NotEmpty(message = "请填写菜单名称")
    private String name;

    /**
     * 请求地址
     */
    @NotEmpty(message = "请填写请求地址")
    private String url;

    /**
     * 权限标识
     */
    @NotEmpty(message = "请填写权限标识")
    private String permission;

    /**
     * 类型: (1:菜单，2：按钮，3：接口)
     */
    @NotEmpty(message = "请填写菜单类型：(1:菜单，2：按钮，3：接口)")
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单状态: (0：显示，1：隐藏)
     */
    @NotEmpty(message = "请填写菜单状态：(0：显示，1：隐藏)")
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
}
