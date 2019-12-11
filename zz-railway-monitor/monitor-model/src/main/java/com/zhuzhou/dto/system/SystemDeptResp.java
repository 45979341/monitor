package com.zhuzhou.dto.system;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 部门
 * </p>
 *
 * @author wangxiaokuan
 * @since 2019-12-05
 */
@Data
public class SystemDeptResp implements Serializable {
    private Integer id;
    private String name;
    private Integer pId;
}
