package com.zhuzhou.dao.statistic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhuzhou.entity.statistic.*;
import com.zhuzhou.page.statistic.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-06-27
 * @Description:
 **/
@Repository
public interface StatisticMapper {

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
    IPage<OneRailAnalysis> oneRailAnalysis(@Param("page") RecordIdPage page);

    /**
     * 一趟出乘
     *
     * @param page
     * @return
     */
    IPage<RailStatistic> railStatistic(@Param("page") RailStatisticPage page);

    /**
     * 司机统计
     *
     * @param page
     * @return
     */
    IPage<DriverPhasesStatistic> driverStatistic(@Param("page") DriverStatisticPage page);

    /**
     * 运用车间统计
     *
     * @param page
     * @return
     */
    IPage<PhasesStatistic> workshopStatistic(@Param("page") SearchPage page);

    /**
     * 机务段统计
     *
     * @param page
     * @return
     */
    IPage<PhasesStatistic> locomotiveDepotStatistic(@Param("page") SearchPage page);

    /**
     * 铁路局统计
     *
     * @param page
     * @return
     */
    IPage<PhasesStatistic> railwayStatistic(@Param("page") SearchPage page);
}
