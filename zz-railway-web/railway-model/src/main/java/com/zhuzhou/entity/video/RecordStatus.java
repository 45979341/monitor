package com.zhuzhou.entity.video;

import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenzeting
 * @since 2019-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("record_status")
public class RecordStatus implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联upload_record记录id
     */
    private String recordId;

    /**
     * 相点解析状态
     */
    private Integer phaseStatus;
    /**
     * 机械间解析状态
     */
    private Integer machineStatus;
    /**
     * 前方解析状态
     */
    private Integer frontStatus;
    /**
     * 遮挡解析状态
     */
    private Integer coverStatus;

    /**
     * 创建时间
     */
    private Timestamp createTime;


}
