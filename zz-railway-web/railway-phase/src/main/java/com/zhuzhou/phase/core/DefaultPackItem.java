package com.zhuzhou.phase.core;

import com.zhuzhou.framework.utils.spring.ApplicationContextUtils;
import com.zhuzhou.phase.hand.*;
import com.zhuzhou.phase.item.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * @Author chenzeting
 * @Date 2019-07-30
 * @Description: 执行。
 **/
@Component
public class DefaultPackItem extends AbstractPackItem {

    @Range("退出出段")
    @Override
    public List<AbstractPhase> out() {
        return Stream.of(
                ApplicationContextUtils.getContext().getBean("item1", Item1.class),
                ApplicationContextUtils.getContext().getBean("item2", Item2.class),
                ApplicationContextUtils.getContext().getBean("item3", Item3.class),
                ApplicationContextUtils.getContext().getBean("item4", Item4.class),
                ApplicationContextUtils.getContext().getBean("item5", Item5.class),
                ApplicationContextUtils.getContext().getBean("item6", Item6.class),
                ApplicationContextUtils.getContext().getBean("item7", Item7.class),
                ApplicationContextUtils.getContext().getBean("item8", Item8.class),
                ApplicationContextUtils.getContext().getBean("item9", Item9.class),
                ApplicationContextUtils.getContext().getBean("item10", Item10.class),
                ApplicationContextUtils.getContext().getBean("item102", Item102.class),
                ApplicationContextUtils.getContext().getBean("item112", Item112.class),
                ApplicationContextUtils.getContext().getBean("item137", Item137.class)
        ).collect(Collectors.toList());
    }

    @Range("监控")
    @Override
    public List<AbstractPhase> monitor() {
        return Stream.of(
                ApplicationContextUtils.getContext().getBean("item14", Item14.class),
                ApplicationContextUtils.getContext().getBean("item15", Item15.class),
                ApplicationContextUtils.getContext().getBean("item20", Item20.class),
                ApplicationContextUtils.getContext().getBean("item21", Item21.class),
                ApplicationContextUtils.getContext().getBean("item25", Item25.class),
                ApplicationContextUtils.getContext().getBean("item28", Item28.class),
                ApplicationContextUtils.getContext().getBean("item34", Item34.class),
                ApplicationContextUtils.getContext().getBean("item37", Item37.class),
                ApplicationContextUtils.getContext().getBean("item54", Item54.class),
                ApplicationContextUtils.getContext().getBean("item78", Item78.class)
        ).collect(Collectors.toList());
    }

    @Range("降级")
    @Override
    public List<AbstractPhase> demotion() {
        return Collections.EMPTY_LIST;
    }

    @Range("调车")
    @Override
    public List<AbstractPhase> shunt() {
        return Stream.of(
                ApplicationContextUtils.getContext().getBean("item117", Item117.class)
        ).collect(Collectors.toList());
    }

    @Range("非调车")
    @Override
    public List<AbstractPhase> unshunt() {
        return Stream.of(
                ApplicationContextUtils.getContext().getBean("item35", Item35.class),
                ApplicationContextUtils.getContext().getBean("item36", Item36.class)
        ).collect(Collectors.toList());
    }

    @Range("监控调车")
    @Override
    public List<AbstractPhase> monitorShunt() {
        return Stream.of(
                ApplicationContextUtils.getContext().getBean("item27", Item27.class),
                ApplicationContextUtils.getContext().getBean("item103", Item103.class),
                ApplicationContextUtils.getContext().getBean("item104", Item104.class),
                ApplicationContextUtils.getContext().getBean("item119", Item119.class)
        ).collect(Collectors.toList());
    }

    @Range("监控非调车")
    @Override
    public List<AbstractPhase> monitorUnshunt() {
        return Stream.of(
                ApplicationContextUtils.getContext().getBean("item19", Item19.class),
                ApplicationContextUtils.getContext().getBean("item22", Item22.class),
                ApplicationContextUtils.getContext().getBean("item23", Item23.class),
                ApplicationContextUtils.getContext().getBean("item24", Item24.class),
                ApplicationContextUtils.getContext().getBean("item26", Item26.class),
                ApplicationContextUtils.getContext().getBean("item30", Item30.class),
                ApplicationContextUtils.getContext().getBean("item31", Item31.class),
                ApplicationContextUtils.getContext().getBean("item32", Item32.class),
                ApplicationContextUtils.getContext().getBean("item51", Item51.class),
                ApplicationContextUtils.getContext().getBean("item72", Item72.class),
                ApplicationContextUtils.getContext().getBean("item79", Item79.class),
                ApplicationContextUtils.getContext().getBean("item80", Item80.class),
                ApplicationContextUtils.getContext().getBean("item84", Item84.class),
                ApplicationContextUtils.getContext().getBean("item85", Item85.class),
                ApplicationContextUtils.getContext().getBean("item98", Item98.class),
                ApplicationContextUtils.getContext().getBean("item121", Item121.class),
                ApplicationContextUtils.getContext().getBean("item124", Item124.class),
                ApplicationContextUtils.getContext().getBean("item133", Item133.class),
                ApplicationContextUtils.getContext().getBean("item138", Item138.class)
        ).collect(Collectors.toList());
    }

    @Range("降级调车")
    @Override
    public List<AbstractPhase> demotionShunt() {
        return Collections.EMPTY_LIST;
    }

    @Range("降级非调车")
    @Override
    public List<AbstractPhase> demotionUnshunt() {
        return Collections.EMPTY_LIST;
    }

    @Range("其它")
    @Override
    public List<AbstractPhase> other() {
        return Stream.of(
                ApplicationContextUtils.getContext().getBean("item11", Item11.class),
                ApplicationContextUtils.getContext().getBean("item12", Item12.class),
                ApplicationContextUtils.getContext().getBean("item13", Item13.class),
                ApplicationContextUtils.getContext().getBean("item16", Item16.class),
                ApplicationContextUtils.getContext().getBean("item29", Item29.class),
                ApplicationContextUtils.getContext().getBean("item33", Item33.class),
                ApplicationContextUtils.getContext().getBean("item75", Item75.class),
                ApplicationContextUtils.getContext().getBean("item76", Item76.class),
                ApplicationContextUtils.getContext().getBean("item77", Item77.class),
                ApplicationContextUtils.getContext().getBean("item81", Item81.class),
                ApplicationContextUtils.getContext().getBean("item110and123", Item110and123.class),
                ApplicationContextUtils.getContext().getBean("item113", Item113.class),
                ApplicationContextUtils.getContext().getBean("item115", Item115.class),
                ApplicationContextUtils.getContext().getBean("item116", Item116.class),
                ApplicationContextUtils.getContext().getBean("item118", Item118.class),
                ApplicationContextUtils.getContext().getBean("item125", Item125.class),
                ApplicationContextUtils.getContext().getBean("item128", Item128.class),
                ApplicationContextUtils.getContext().getBean("item131", Item131.class),
                ApplicationContextUtils.getContext().getBean("item132", Item132.class),
                ApplicationContextUtils.getContext().getBean("item134", Item134.class),
                ApplicationContextUtils.getContext().getBean("item135", Item135.class),
                ApplicationContextUtils.getContext().getBean("item136", Item136.class),
                ApplicationContextUtils.getContext().getBean("itemOnceEvent", ItemOnceEvent.class)
        ).collect(Collectors.toList());
    }

    @Override
    public List<AbstractHandPhase> hand() {
        return Stream.of(
                ApplicationContextUtils.getContext().getBean(PassItem.class),
                ApplicationContextUtils.getContext().getBean(ReadyStopItem.class),
                ApplicationContextUtils.getContext().getBean(SidingItem.class),
                ApplicationContextUtils.getContext().getBean(WatchItem.class),
                ApplicationContextUtils.getContext().getBean(StopItem.class)
        ).collect(Collectors.toList());
    }






}
