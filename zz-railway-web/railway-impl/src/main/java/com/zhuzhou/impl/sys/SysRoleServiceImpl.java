package com.zhuzhou.impl.sys;

import com.zhuzhou.dao.sys.SysRoleMapper;
import com.zhuzhou.entity.sys.SysRole;
import com.zhuzhou.impl.BaseServiceImpl;
import com.zhuzhou.spi.sys.SysRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-08
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
