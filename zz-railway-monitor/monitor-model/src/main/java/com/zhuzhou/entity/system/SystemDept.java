package com.zhuzhou.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 部门信息
 * </p>
 *
 * @author wangxiaokuan
 * @since 2019-12-04
 */
@Data
@TableName("sys_dept")
public class SystemDept implements Serializable {
    @TableId
    private Integer id;
    private Integer parentId;
    private String ancestors;
    private String deptName;
    private String leader;
    private String phone;
    private String email;
    private Integer status;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
}
