package com.zhuzhou.form.video;

import com.zhuzhou.entity.idx.IdxAnalysisLog;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import lombok.Data;

import java.util.Date;

/**
 * @Author chenzeting
 * @Date 2019-04-06
 * @Description:
 * @see IdxAnalysisLog
 **/
@Data
public class IdxAnalysisLogEditForm implements Form<IdxAnalysisLog> {

    /**
     * id
     */
    @NotEmpty(message = "id不能为空")
    private String id;

    /**
     * 项点id
     */
    private String phaseId;
    /**
     * 分析项目
     */
    private String item;

    /**
     * 是否违章
     */
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
     * 意见图片
     * 传 base64
     */
    private String base64Pic;
}
