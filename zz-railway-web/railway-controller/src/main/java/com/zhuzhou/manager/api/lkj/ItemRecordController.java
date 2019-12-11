package com.zhuzhou.manager.api.lkj;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zhuzhou.entity.config.ConfigPhase;
import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.form.IdForm;
import com.zhuzhou.form.config.ConfigPhaseListForm;
import com.zhuzhou.form.lkj.ItemRecordItemIdForm;
import com.zhuzhou.form.lkj.ItemRecordMajorIdForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.ReflectionUtils;
import com.zhuzhou.spi.config.ConfigPhaseService;
import com.zhuzhou.spi.lkj.ItemRecordService;
import com.zhuzhou.spi.video.LkjService;
import com.zhuzhou.vo.lkj.ItemRecordVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-05-15
 * Description:
 * @menu LKJ信息
 */
@Controller
public class ItemRecordController {
    @Autowired
    private ItemRecordService itemRecordService;
    @Autowired
    private LkjService lkjService;
    @Autowired
    private ConfigPhaseService configPhaseService;

    /**
     * 获取lkj大项统计
     * @return
     */
    @RequiresPermissions("item:record:count")
    @GetMapping(name = "获取lkj大项统计", value = "/item/record/count")
    public Result count(IdForm idForm) {
        ItemRecord itemRecord = new ItemRecord();
        itemRecord.setLkjId(idForm.getId());
        List<ItemRecord> list = itemRecordService.list(itemRecord);
        //每个大项，统计违章及记录的次数
        Map<Integer, Integer> collect = list.stream().collect(Collectors.toMap(ItemRecord::getMajorId, v -> {
            if (v.getIllegalStand() == 0 || v.getIllegalStand() == 2) {
                return 1;
            }
            return 0;
        }, (x, y) -> x + y));
        return Result.success().setData(collect);
    }

    /**
     * 获取lkj大项编号详情
     * @return
     */
    @RequiresPermissions("item:major:detail")
    @GetMapping(name = "获取lkj大项编号详情", value = "/item/major/detail")
    public Result list(ItemRecordMajorIdForm form) {
        ItemRecord itemRecord = new ItemRecord();
        ReflectionUtils.copyProperties(itemRecord, form);
        List<ItemRecord> list = itemRecordService.list(itemRecord);

        Map<Integer, ItemRecordVo> map = new HashMap<>(8);
        for (ItemRecord ir : list) {
            Integer itemId = ir.getItemId();
            ItemRecordVo itemRecordVo = map.get(itemId);
            if (itemRecordVo == null) {
                itemRecordVo = new ItemRecordVo();
                itemRecordVo.setIllegalStand(ir.getIllegalStand());
                map.put(itemId, itemRecordVo);
            }
            itemRecordVo.setCount(itemRecordVo.getCount() + 1);
        }
        return Result.success().setData(map);
    }

    /**
     * 获取lkj项点编号详情
     * @return
     */
    @RequiresPermissions("item:item:detail")
    @GetMapping(name = "获取lkj项点编号详情", value = "/item/item/detail")
    public Result list(ItemRecordItemIdForm form) {
        ItemRecord itemRecord = new ItemRecord();
        ReflectionUtils.copyProperties(itemRecord, form);
        List<ItemRecord> list = itemRecordService.list(itemRecord);
        List<String> lkjIdList = list.stream().map(m -> m.getLkjNo()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(lkjIdList)) {
            return Result.success().setData(null);
        }
        Collection<Lkj> lkjs = lkjService.listByIds(lkjIdList);
        return Result.success().setData(lkjs);
    }

    /**
     * 获取lkj所有违章项点
     * @return
     */
    @RequiresPermissions("lkj:item:list")
    @GetMapping(name = "获取lkj所有违章项点", value = "/lkj/item/list")
    public Result list(IdForm form) {
        ItemRecord itemRecord = new ItemRecord();
        itemRecord.setLkjId(form.getId());
        List<ItemRecord> list = itemRecordService.list(itemRecord);
        Map<Integer, ItemRecordVo> map = new HashMap<>(8);
        for (ItemRecord ir : list) {
            Integer itemId = ir.getItemId();
            ItemRecordVo itemRecordVo = map.get(itemId);
            if (itemRecordVo == null) {
                itemRecordVo = new ItemRecordVo();
                itemRecordVo.setIllegalStand(ir.getIllegalStand());
                map.put(itemId, itemRecordVo);
            }
            if (ir.getIllegalStand() == 0) {
                itemRecordVo.setCount(itemRecordVo.getCount() + 1);
            } else {
                itemRecordVo.setStandCount(itemRecordVo.getStandCount() + 1);
            }
        }
        return Result.success().setData(map);
    }

    /**
     * 获取LKJ项点打分列表
     * @return
     */
    @RequiresPermissions("item:score:list")
    @GetMapping(name = "获取LKJ项点打分列表", value = "/item/score/list")
    public Result<List<ConfigPhase>> scoreList(ConfigPhaseListForm form) {
        form.setType("LKJ");
        List<ConfigPhase> list = configPhaseService.list(form);
        return Result.<List<ConfigPhase>>success().setData(list);
    }
}
