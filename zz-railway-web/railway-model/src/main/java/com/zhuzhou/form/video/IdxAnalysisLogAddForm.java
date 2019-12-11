package com.zhuzhou.form.video;

import com.zhuzhou.entity.idx.IdxAnalysisLog;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Operator;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import com.zhuzhou.framework.validate.annotation.ValueSet;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author chenzeting
 * @Date 2019-04-06
 * @Description:
 * @see com.zhuzhou.entity.idx.IdxAnalysisLog
 **/
@Data
public class IdxAnalysisLogAddForm implements Form<IdxAnalysisLog> {

    /**
     * idx_id
     */
    @NotEmpty(message = "idxId不能为空")
    private String idxId;

    /**
     * recordId
     */
    @NotEmpty(message = "recordId不能为空")
    private String recordId;

    /**
     * 车次
     */
    @NotEmpty(message = "车次不能为空")
    private String trainNum;

    /**
     * 机车号
     */
    @NotEmpty(message = "机车号不能为空")
    private String cabNum;

    /**
     * 项点id
     */
    @NotEmpty(message = "请选择项点")
    private String phaseId;
    /**
     * 分析项目
     */
    @NotEmpty(message = "未指定项点")
    private String item;

    /**
     * 是否违章
     */
    @NotEmpty(message = "请填写违章")
    private Integer illegal;

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
     * 项点开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotEmpty(message = "请选择项点")
    private Date startTime;

    /**
     * 意见图片
     * 传 base64
     */
    private String base64Pic;
}
