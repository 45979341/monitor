package com.zhuzhou.manager.util;

import com.zhuzhou.entity.sys.SysMenu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiechonghu
 * @date 2019/8/23 17:18
 * @description:
 */
public class TreeUtils {
    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public static List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        if (list == null) {
            return null;
        }
        List<SysMenu> returnList = new ArrayList<>();
        for (SysMenu t : list) {
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList.stream().sorted(Comparator.comparingInt(SysMenu::getSort)).collect(Collectors.toList());
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private static void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                for (SysMenu n : childList) {
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private static List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {

        List<SysMenu> tlist = new ArrayList<>();
        for (SysMenu n : list) {
            if (n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    List<SysMenu> returnList = new ArrayList<>();

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list   分类表
     * @param typeId 传入的父节点ID
     * @param prefix 子节点前缀
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int typeId, String prefix) {
        if (list == null) {
            return null;
        }
        for (SysMenu node : list) {
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (node.getParentId() == typeId) {
                recursionFn(list, node, prefix);
            }
            // 二、遍历所有的父节点下的所有子节点
            /*
             * if (node.getParentId()==0) { recursionFn(list, node); }
             */
        }
        return returnList;
    }

    private void recursionFn(List<SysMenu> set, SysMenu node, String p) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(set, node);
        if (hasChild(set, node)) {
            // 判断是否有子节点
            returnList.add(node);
            for (SysMenu n : childList) {
                n.setName(p + n.getName());
                recursionFn(set, n, p + p);
            }
        } else {
            returnList.add(node);
        }
    }

    /**
     * 判断是否有子节点
     */
    private static boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }
}
