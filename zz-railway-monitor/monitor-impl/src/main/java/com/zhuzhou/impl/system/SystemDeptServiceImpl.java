package com.zhuzhou.impl.system;

import com.zhuzhou.dao.system.SystemDeptMapper;
import com.zhuzhou.entity.system.SystemDept;
import com.zhuzhou.impl.BaseServiceImpl;
import com.zhuzhou.spi.system.SystemDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 设备管理
 * </p>
 *
 * @author wangxiaokuan
 * @since 2019-12-05
 */
@Service
public class SystemDeptServiceImpl extends BaseServiceImpl<SystemDeptMapper, SystemDept> implements SystemDeptService {

    @Autowired
    private SystemDeptMapper systemDeptMapper;

    @Override
    public Map<String, String> getAncestor(Integer id) {
        SystemDept dept = systemDeptMapper.selectById(id);
        String[] split = dept.getAncestors().split(",");
        HashMap<String, String> map = new HashMap<>();
        if (split.length == 1) {
            map.put("first",dept.getDeptName());
        } else if (split.length == 2) {
            map.put("second",dept.getDeptName());
            map.put("first",systemDeptMapper.selectById(Integer.parseInt(split[1])).getDeptName());
        } else {
            map.put("third",dept.getDeptName());
            map.put("second",systemDeptMapper.selectById(Integer.parseInt(split[2])).getDeptName());
            map.put("first",systemDeptMapper.selectById(Integer.parseInt(split[1])).getDeptName());
        }
        return map;
    }
}
