package com.zhuzhou.spi.system;

import com.zhuzhou.entity.system.SystemDept;
import com.zhuzhou.spi.BaseService;

import java.util.Map;

/**
 * <p>
 * 设备管理
 * </p>
 *
 * @author wangxiaokuan
 * @since 2019-12-04
 */
public interface SystemDeptService extends BaseService<SystemDept> {
    /**
     * 获得当前部门上级部门
     * @param id
     * @return
     */
    public Map<String,String> getAncestor(Integer id);
}
