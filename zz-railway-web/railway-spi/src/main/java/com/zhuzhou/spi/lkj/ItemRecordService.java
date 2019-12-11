package com.zhuzhou.spi.lkj;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.spi.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-15
 */
public interface ItemRecordService extends BaseService<ItemRecord> {

    /**
     * 添加项点记录
     * @param lkjId 关联lkj_index的id
     * @param itemEnum 项点位置， 如：item_1_2
     * @param lkjNo lkj文件序号
     * @param illegal  违章标准 {@link ItemRecord#getIllegalStand()}
     */
    void add(String lkjId, ItemRecord.ItemEnum itemEnum, String lkjNo, int illegal);
}
