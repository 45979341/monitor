package com.zhuzhou.impl.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuzhou.entity.bo.DriverPhasesStatisticBO;
import com.zhuzhou.entity.bo.PhasesStatisticBO;
import com.zhuzhou.entity.statistic.DriverPhasesStatistic;
import com.zhuzhou.entity.statistic.PhasesStatistic;

/**
 * @author xiechonghu
 * @date 2019/8/16 9:20
 * @description:
 */
public class PhasesStatistic2PhasesStatisticBO {

    public IPage<DriverPhasesStatisticBO> driverConvert(IPage<DriverPhasesStatistic> page) {
        IPage<DriverPhasesStatisticBO> pageBO = new Page<>();
        pageBO.setSize(page.getSize());
        pageBO.setCurrent(page.getCurrent());
        pageBO.setTotal(page.getTotal());
        pageBO.setPages(page.getPages());
        return pageBO;
    }

    public IPage<PhasesStatisticBO> phasesStatisticConvert(IPage<PhasesStatistic> page) {
        IPage<PhasesStatisticBO> pageBO = new Page<>();
        pageBO.setSize(page.getSize());
        pageBO.setCurrent(page.getCurrent());
        pageBO.setTotal(page.getTotal());
        pageBO.setPages(page.getPages());
        return pageBO;
    }
}
