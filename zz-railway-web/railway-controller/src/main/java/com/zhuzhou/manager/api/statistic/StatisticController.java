package com.zhuzhou.manager.api.statistic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhuzhou.dto.video.RecordStatusResult;
import com.zhuzhou.entity.bo.DriverPhasesStatisticBO;
import com.zhuzhou.entity.bo.PhasesStatisticBO;
import com.zhuzhou.entity.statistic.NearlyWeekDateNumber;
import com.zhuzhou.entity.statistic.NearlyWeekGestureAnalysis;
import com.zhuzhou.entity.statistic.OneRailAnalysis;
import com.zhuzhou.entity.statistic.RailStatistic;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.ExcelUtils;
import com.zhuzhou.framework.utils.stomp.ServletUtils;
import com.zhuzhou.page.statistic.DriverStatisticPage;
import com.zhuzhou.page.statistic.RailStatisticPage;
import com.zhuzhou.page.statistic.RecordIdPage;
import com.zhuzhou.page.statistic.SearchPage;
import com.zhuzhou.spi.statistic.StatisticService;
import com.zhuzhou.spi.video.RecordStatusService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author chenzeting
 * @Date 2019-06-27
 * @Description:
 * @menu 统计
 **/
@Controller
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private RecordStatusService recordStatusService;

    /**
     * 当天出乘数量
     *
     * @return 当天数量
     */
    @RequiresPermissions("statistic:today:driver")
    @GetMapping(name = "当天出乘数量",value = "/statistic/today/driver")
    public Result<Integer> todayDriver() {
        Integer count = statisticService.todayDriver();
        return Result.<Integer>success().setData(count);
    }

    /**
     *  项点分析统计
     * @return
     */
    @RequiresPermissions("statistic:phasesAnalysis:count")
    @GetMapping(name = "项点分析统计",value = "/statistic/phasesAnalysis/count")
    public Result getStatusNum() {
        RecordStatusResult record = recordStatusService.getCountForRecord();
        return Result.success().setData(record);
    }

    /**
     * 近一周出乘数量
     *
     * @return 近一周 日期和对应的数量
     */
    @RequiresPermissions("statistic:week:rail")
    @GetMapping(name = "近一周出乘数量",value = "/statistic/week/rail")
    public Result<List<NearlyWeekDateNumber>> nearlyWeekRailNumber() {
        List<NearlyWeekDateNumber> list = statisticService.nearlyWeekRailNumber();
        return Result.<List<NearlyWeekDateNumber>>success().setData(list);
    }

    /**
     * 近一周项点汇总
     *
     * @return 近一周 日期和对应的数量
     */
    @RequiresPermissions("statistic:week:phase")
    @GetMapping(name = "近一周项点汇总",value = "/statistic/week/phase")
    public Result<List<NearlyWeekDateNumber>> nearlyWeekPhaseNumber() {
        List<NearlyWeekDateNumber> list = statisticService.nearlyWeekPhaseNumber();
        return Result.<List<NearlyWeekDateNumber>>success().setData(list);
    }

    /**
     * 近一周违章项点汇总
     *
     * @return 近一周 日期和对应的数量
     */
    @GetMapping(name = "近一周违章项点汇总",value = "/statistic/week/illegalphase")
    public Result<List<NearlyWeekDateNumber>> nearlyWeekIllegalPhaseNumber() {
        List<NearlyWeekDateNumber> list = statisticService.nearlyWeekIllegalPhaseNumber();
        return Result.<List<NearlyWeekDateNumber>>success().setData(list);
    }

    /**
     * 近一周违章手势汇总
     *
     * @return 近一周 日期和对应的数量
     */
    @RequiresPermissions("statistic:week:illegalgesture")
    @GetMapping(name = "近一周违章手势汇总",value = "/statistic/week/illegalgesture")
    public Result<List<NearlyWeekDateNumber>> nearlyWeekIllegalGestureNumber() {
        List<NearlyWeekDateNumber> list = statisticService.nearlyWeekIllegalGestureNumber();
        return Result.<List<NearlyWeekDateNumber>>success().setData(list);
    }

    /**
     * 近一周手势项点汇总
     *
     * @return 近一周 日期和对应的不同状态的数量
     */
    @GetMapping(name = "近一周手势项点汇总",value = "/statistic/week/gesture")
    public Result<List<NearlyWeekGestureAnalysis>> nearlyWeekGestureAnalysis() {
        List<NearlyWeekGestureAnalysis> list = statisticService.nearlyWeekGestureAnalysis();
        return Result.<List<NearlyWeekGestureAnalysis>>success().setData(list);
    }

    /**
     * 出乘详情
     *
     * @param page
     * @return 一趟出乘详情分析，扣分项点和分数等
     */
    @GetMapping(name = "出乘详情",value = "/statistic/analysis/onerail")
    public Result<IPage<OneRailAnalysis>> oneRailAnalysis(RecordIdPage page) {
        IPage<OneRailAnalysis> listPage = statisticService.oneRailAnalysis(page);
        return Result.<IPage<OneRailAnalysis>>success().setData(listPage);
        //TODO 只按司机名分组，没法统计副司机信息

    }

    /**
     * 出乘详情 （导出）
     * TODO 测试时不需要登录认证
     *
     * @param page
     * @return
     * @throws IOException
     */
    @GetMapping(name = "出乘详情 （导出）",value = "/statistic/analysis/onerailexport")
    public ResponseEntity<byte[]> oneRailAnalysisExport(RecordIdPage page) throws IOException {
        IPage<OneRailAnalysis> listPage = statisticService.oneRailAnalysis(page);
        List<OneRailAnalysis> list = listPage.getRecords();
        //导出表格
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ExcelUtils.write(list, setRailDetailListMapping(), "出乘详情表", stream);
        ResponseEntity<byte[]> response = ServletUtils.getFileResponse("出乘详情表.xls", stream.toByteArray());
        stream.close();
        return response;
    }

    private Map<String, String> setRailDetailListMapping() {
        Map<String, String> mapping = new LinkedHashMap<>();
        mapping.put("项点编码", "phaseId");
        mapping.put("项点名称", "name");
        mapping.put("违章次数", "count");
        mapping.put("分数", "score");
        mapping.put("总计", "totalPoints");

        return mapping;
    }

    /**
     * 一趟出乘
     *
     * @param page
     * @return 默认近一周每一趟出乘统计
     */
    @GetMapping(name = "一趟出乘",value = "/statistic/analysis/rail")
    public Result<IPage<RailStatistic>> railStatistic(RailStatisticPage page) {
        IPage<RailStatistic> listPage = statisticService.railStatistic(page);
        return Result.<IPage<RailStatistic>>success().setData(listPage);
    }

    /**
     * 一趟出乘（导出）
     * TODO 测试时不需要登录认证
     *
     * @param page
     * @return
     * @throws IOException
     */
    @GetMapping(name = "一趟出乘（导出）",value = "/statistic/analysis/railexport")
    public ResponseEntity<byte[]> railStatisticExport(RailStatisticPage page) throws IOException {
        IPage<RailStatistic> listPage = statisticService.railStatistic(page);
        List<RailStatistic> list = listPage.getRecords();
        //导出表格
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ExcelUtils.write(list, setRailListMapping(), "一趟出乘统计表", stream);
        ResponseEntity<byte[]> response = ServletUtils.getFileResponse("一趟出乘统计表.xls", stream.toByteArray());
        stream.close();
        return response;

    }

    private Map<String, String> setRailListMapping() {
        Map<String, String> mapping = new LinkedHashMap<>();
        mapping.put("开车日期", "driverDate");
        mapping.put("车次", "trainNum");
        mapping.put("车型", "trainType");
        mapping.put("起点站", "originStation");
        mapping.put("终点站", "terminus");
        mapping.put("机车号", "motorNum");
        mapping.put("司机号", "driverNum");
        mapping.put("司机名", "driverName");
        mapping.put("副司机号", "assisDriverNum");
        mapping.put("副司机名", "assisDriverName");
        mapping.put("项点统计", "phaseNum");
        mapping.put("违章数量", "illegalNum");
        return mapping;
    }

    /**
     * 司机统计
     *
     * @param page
     * @return 司机和各项点违规次数
     */
    @GetMapping(name = "司机统计",value = "/statistic/phases/driver")
    public Result<IPage<DriverPhasesStatisticBO>> driverStatistic(DriverStatisticPage page) {
        IPage<DriverPhasesStatisticBO> driverStatistic = statisticService.driverStatistic(page);
        return Result.<IPage<DriverPhasesStatisticBO>>success().setData(driverStatistic);
        //TODO 没法区分是司机还是副司机违规
    }


    /**
     * 运用车间统计
     *
     * @param page
     * @return 运用车间和各项点违规次数
     */
    @GetMapping(name = "运用车间统计",value = "/statistic/phases/workshop")
    public Result<IPage<PhasesStatisticBO>> workshopStatistic(SearchPage page) {
        IPage<PhasesStatisticBO> result = statisticService.workshopStatistic(page);
        return Result.<IPage<PhasesStatisticBO>>success().setData(result);
    }

    /**
     * 机务段统计
     *
     * @param page
     * @return 机务段和各项点违规次数
     */
    @GetMapping(name = "机务段统计",value = "/statistic/phases/locomotivedepot")
    public Result<IPage<PhasesStatisticBO>> locomotiveDepotStatistic(SearchPage page) {
        IPage<PhasesStatisticBO> result = statisticService.locomotiveDepotStatistic(page);
        return Result.<IPage<PhasesStatisticBO>>success().setData(result);
    }

    /**
     * 铁路局统计
     *
     * @param page
     * @return 铁路局和各项点违规次数
     */
    @GetMapping(name = "铁路局统计",value = "/statistic/phases/railway")
    public Result<IPage<PhasesStatisticBO>> railwayStatistic(SearchPage page) {
        IPage<PhasesStatisticBO> result = statisticService.railwayStatistic(page);
        return Result.<IPage<PhasesStatisticBO>>success().setData(result);
    }

}
