package com.zhuzhou.phase.model;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Author: chenzeting
 * Date:     2019/8/11
 * Description: 项点辅助, 统一存取项点
 */
@Data
public class PhaseAssist {
    private Lkj lkj;
    private String lkjId;
    private ItemRecord.ItemEnum itemEnum;
    private Phase.PhaseEnum phaseEnum;
    private Date start;
    private Date end;
    private Integer itemRecordIllegal;
    private Integer phaseIllegal;

    public PhaseAssist (Builder builder) {
        this.lkj = builder.lkj;
        this.lkjId = builder.lkjId;
        this.itemEnum = builder.itemEnum;
        this.phaseEnum = builder.phaseEnum;
        this.start = builder.start;
        this.end = builder.end;
        this.itemRecordIllegal = builder.itemRecordIllegal;
        this.phaseIllegal = builder.phaseIllegal;
    }

    @Data
    @Accessors(chain = true)
    public static class Builder {
        private Lkj lkj;
        private String lkjId;
        private ItemRecord.ItemEnum itemEnum;
        private Phase.PhaseEnum phaseEnum;
        private Date start;
        private Date end;
        private Integer itemRecordIllegal = 0;
        private Integer phaseIllegal = 0;

        public PhaseAssist builder () {
            return new PhaseAssist(this);
        }
    }
}
