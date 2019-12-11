package com.zhuzhou.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuzhou.entity.sys.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-08
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据名称查询用户，用户名
     * @param loginName
     * @return
     */
    SysUser findByLoginName(@Param("loginName") String loginName);
}
