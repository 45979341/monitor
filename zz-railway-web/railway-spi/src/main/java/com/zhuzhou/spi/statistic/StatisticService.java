package com.zhuzhou.spi.statistic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhuzhou.entity.bo.DriverPhasesStatisticBO;
import com.zhuzhou.entity.bo.PhasesStatisticBO;
import com.zhuzhou.entity.statistic.NearlyWeekDateNumber;
import com.zhuzhou.entity.statistic.NearlyWeekGestureAnalysis;
import com.zhuzhou.entity.statistic.OneRailAnalysis;
import com.zhuzhou.entity.statistic.RailStatistic;
import com.zhuzhou.page.statistic.DriverStatisticPage;
import com.zhuzhou.page.statistic.RailStatisticPage;
import com.zhuzhou.page.statistic.RecordIdPage;
import com.zhuzhou.page.statistic.SearchPage;

import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-06-27
 * @Description:
 **/
public interface StatisticService {

    /**
     * 今天出乘数量
     *
     * @return
     */
    Integer todayDriver();

    /**
     * 近一周出乘数量
     *
     * @return
     */
    List<NearlyWeekDateNumber> nearlyWeekRailNumber();

    /**
     * 近一周项点汇总
     *
     * @return
     */
    List<NearlyWeekDateNumber> nearlyWeekPhaseNumber();

    /**
     * 近一周违章项点汇总
     *
     * @return
     */
    List<NearlyWeekDateNumber> nearlyWeekIllegalPhaseNumber();

    /**
     * 近一周手势违章汇总
     *
     * @return
     */
    List<NearlyWeekDateNumber> nearlyWeekIllegalGestureNumber();

    /**
     * 近一周手势项点分析
     *
     * @return
     */
    List<NearlyWeekGestureAnalysis> nearlyWeekGestureAnalysis();

    /**
     * 出乘详情
     *
     * @param page
     * @return
     */
    IPage<OneRailAnalysis> oneRailAnalysis(RecordIdPage page);

    /**
     * 一趟出乘
     *
     * @param page
     * @return
     */
    IPage<RailStatistic> railStatistic(RailStatisticPage page);

    /**
     * 司机统计
     *
     * @param page
     * @return
     */
    IPage<DriverPhasesStatisticBO> driverStatistic(DriverStatisticPage page);

    /**
     * 车间统计
     *
     * @param page
     * @return
     */
    IPage<PhasesStatisticBO> workshopStatistic(SearchPage page);


    /**
     * 机务段统计
     *
     * @param page
     * @return
     */
    IPage<PhasesStatisticBO> locomotiveDepotStatistic(SearchPage page);

    /**
     * 铁路局统计
     *
     * @param page
     * @return
     */
    IPage<PhasesStatisticBO> railwayStatistic(SearchPage page);
}
