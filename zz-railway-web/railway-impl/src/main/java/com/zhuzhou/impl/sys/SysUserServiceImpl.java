package com.zhuzhou.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhuzhou.consts.SymbolConst;
import com.zhuzhou.dao.sys.SysUserMapper;
import com.zhuzhou.entity.sys.SysMenu;
import com.zhuzhou.entity.sys.SysRole;
import com.zhuzhou.entity.sys.SysUser;
import com.zhuzhou.framework.exception.ApplicationException;
import com.zhuzhou.framework.exception.ErrorResponseMsgEnum;
import com.zhuzhou.framework.utils.encoder.MD5Encoder;
import com.zhuzhou.framework.utils.stomp.ConstUtils;
import com.zhuzhou.framework.utils.stomp.RandomUtils;
import com.zhuzhou.impl.BaseServiceImpl;
import com.zhuzhou.impl.shiro.JwtToken;
import com.zhuzhou.impl.shiro.JwtUtil;
import com.zhuzhou.spi.sys.SysMenuService;
import com.zhuzhou.spi.sys.SysRoleService;
import com.zhuzhou.spi.sys.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-08
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void login(SysUser sysUser, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        //将用户请求参数封装后，直接提交给Shiro处理
        UsernamePasswordToken upToken = new UsernamePasswordToken(sysUser.getLoginName(), sysUser.getPassword());
        subject.login(upToken);
        //Shiro认证通过后会将user信息放到subject内，生成token并返回
        SysUser principal = (SysUser)subject.getPrincipal();
        String token = JwtUtil.sign(principal.getLoginName(), principal.getSalt());
        response.setHeader(ConstUtils.AUTH_HEADER, token);
    }

    @Override
    public SysUser getByLoginName(String loginName) {
        return sysUserMapper.findByLoginName(loginName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysUser user) {
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPass(SysUser user) {
        if (StringUtils.isNotEmpty(user.getPassword())) {
            encryptUser(user);
        }
        updateById(user);
    }

    @Override
    public List<String> getPermissionByUserId(String userId) {
        SysUser sysUser = getById(userId);
        String roleIds = sysUser.getRoleIds();
        List<SysMenu> menuList = sysMenuService.list();
        String[] split = roleIds.split(SymbolConst.COMMA);
        Set<String> menuIdSet = new HashSet<>();
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
            menuIdSet.addAll(Arrays.stream(splitMenu).collect(Collectors.toSet()));
        }
        return menuList.stream().filter(menu -> menuIdSet.contains(menu.getId().toString()))
                .map(SysMenu::getPermission).collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleByUserId(String userId) {
        SysUser sysUser = getById(userId);
        String roleIds = sysUser.getRoleIds();
        String[] split = roleIds.split(SymbolConst.COMMA);
        QueryWrapper qw = new QueryWrapper();
        qw.in("id", Arrays.asList(split));
        List<SysRole> list = sysRoleService.list(qw);
        return list.stream().map(SysRole::getRoleName).collect(Collectors.toList());
    }

    /**
     * 加密用户
     *
     * @param sysUser
     */
    private void encryptUser(SysUser sysUser) {
        // 登录密码需进行进行加密处理
        String salt = RandomUtils.getRandString(2 << 2);
        sysUser.setSalt(salt);
        //登录密码加密
        String encryptPassword = MD5Encoder.encodePasswordDiscuz(sysUser.getPassword(), salt);
        sysUser.setPassword(encryptPassword);
        sysUser.setStatus(1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(SysUser sysUser) {
        if (Optional.ofNullable(getByLoginName(sysUser.getLoginName())).isPresent()) {
            throw new ApplicationException(ErrorResponseMsgEnum.USER_EXISTS);
        }
        // 登录密码需进行进行加密处理
        String salt = RandomUtils.getRandString(2 << 2);
        sysUser.setSalt(salt);
        //登录密码加密
        String encryptPassword = MD5Encoder.encodePasswordDiscuz(sysUser.getPassword(), salt);
        sysUser.setPassword(encryptPassword);
        sysUser.setStatus(0);
        //新增用户
        save(sysUser);
    }


}
