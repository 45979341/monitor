package com.zhuzhou.entity.monitor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.io.FilenameUtils;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 报警关联文件信息
 * </p>
 *
 * @author chenzeting
 * @since 2019-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("monitor_alarm_file")
public class MonitorAlarmFile implements Serializable {

    /**
     * 文件id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件类型 0:无 1:视频 2:图片 3:音频
     */
    private Integer fileType;

    /**
     * 报警id
     */
    private String alarmId;

    /**
     * 设备标识
     */
    private String deviceId;

    /**
     * 状态:0正常,1禁用
     */
    private Integer status;

    /**
     * 是否删除:0正常,1删除
     */
    private Integer isDelete;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 备注
     */
    private String remark;

    public static void main(String[] args) {
        String extension = FilenameUtils.getExtension("d:/test.mp4");
        System.err.println(extension);
    }
}
