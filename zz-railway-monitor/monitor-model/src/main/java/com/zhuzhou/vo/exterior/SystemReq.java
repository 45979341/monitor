package com.zhuzhou.vo.exterior;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统信息
 * </p>
 *
 * @author wangxiaokuan
 * @since 2019-12-05
 */
@Data
public class SystemReq implements Serializable {
    private Long pageSize = 15L;
    private Long currentPage = 1L;
    private String deviceId;
}
