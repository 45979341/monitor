package com.zhuzhou.entity.idx;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("idx_analysis_log")
public class IdxAnalysisLog implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 关联idx_index的id
     */
    private String recordId;

    /**
     * 司机号
     */
    private String driverNum;

    /**
     * 副司机号
     */
    private String assisDriverNum;

    /**
     * 车次
     */
    private String trainNum;

    /**
     * 机车号
     */
    private String cabNum;

    /**
     * 项点id
     */
    private String phaseId;

    /**
     * 分析项目
     */
    private String item;

    /**
     * 分析内容
     */
    private String content;

    /**
     * 存在问题
     */
    private String question;

    /**
     * 分析意见
     */
    private String opinion;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 意见图片
     */
    private String pic;

    /**
     * 创建时间
     */
    private Timestamp createTime;
}
