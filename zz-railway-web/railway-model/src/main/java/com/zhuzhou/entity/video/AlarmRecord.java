package com.zhuzhou.entity.video;

import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

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
 * @since 2019-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("alarm_record")
public class AlarmRecord implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 关联idx_index的id
     */
    private String recordId;

    /**
     * 报警序号
     */
    private String alarmNo;

    /**
     * 事件
     */
    private String eventItem;

    /**
     * 时间
     */
    private Date time;

    /**
     * 创建时间
     */
    private Timestamp createTime;


}
