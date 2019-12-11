package com.zhuzhou.impl.lkj;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.dao.lkj.ItemRecordMapper;
import com.zhuzhou.spi.lkj.ItemRecordService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-15
 */
@Service
public class ItemRecordServiceImpl extends BaseServiceImpl<ItemRecordMapper, ItemRecord> implements ItemRecordService {
    @Autowired
    private ItemRecordMapper itemRecordMapper;

    @Override
    public void add(String lkjId, ItemRecord.ItemEnum itemEnum, String lkjNo, int illegal) {
        ItemRecord itemRecord = new ItemRecord()
                .setMajorId(itemEnum.getMajorId())
                .setItemId(itemEnum.getItemId())
                .setLkjId(lkjId)
                .setLkjNo(lkjNo)
                .setIllegalStand(illegal);
        save(itemRecord);
    }
}
