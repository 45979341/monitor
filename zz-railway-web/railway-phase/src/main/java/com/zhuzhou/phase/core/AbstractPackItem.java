package com.zhuzhou.phase.core;

import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.model.Situation;
import com.zhuzhou.phase.utils.SituationUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-07-30
 * @Description: 执行
 **/
@Slf4j
public abstract class AbstractPackItem implements IPackItem {

    /**
     * 退出出段
     */
    public abstract List<AbstractPhase> out();

    /**
     * 监控
     */
    public abstract List<AbstractPhase> monitor();

    /**
     * 降级
     */
    public abstract List<AbstractPhase> demotion();

    /**
     * 调车
     */
    public abstract List<AbstractPhase> shunt();

    /**
     * 非调车
     */
    public abstract List<AbstractPhase> unshunt();

    /**
     * 监控调车
     */
    public abstract List<AbstractPhase> monitorShunt();

    /**
     * 监控非调车
     */
    public abstract List<AbstractPhase> monitorUnshunt();

    /**
     * 降级调车
     */
    public abstract List<AbstractPhase> demotionShunt();

    /**
     * 降级非调车
     */
    public abstract List<AbstractPhase> demotionUnshunt();

    /**
     * 其它（整个list）
     */
    public abstract List<AbstractPhase> other();

    /**
     * 手势项点
     */
    public abstract List<AbstractHandPhase> hand();

    @Override
    public void inspectInject(List<Lkj> list, LkjHead lkjHead, String lkjId) {
        List<Situation> situations = SituationUtil.getSection(list);
        Class<? extends AbstractPackItem> clazz = this.getClass();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            Range annotation = method.getDeclaredAnnotation(Range.class);
            if (annotation == null) {
                continue;
            }
            String value = annotation.value();
            situations.forEach(m -> {
                if (value.equals(m.getSituation())) {
                    try {
                        List<AbstractPhase> in = (List<AbstractPhase>) method.invoke(this);
                        in.forEach(s -> {
                                try {
                                    s.exec(list, lkjHead, lkjId, m.getStart(), m.getEnd());
                                } catch (Exception e) {
                                    log.error("项点 [{}]异常", s.getClass().getName(), e);
                                }
                            }
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void injectHand(List<Lkj> list, LkjHead lkjHead, String lkjId, String dir, List<Mp4> mp4List) {
        List<AbstractHandPhase> hand = this.hand();
        hand.stream().forEach(f -> {
            try {
                f.exec(list, lkjHead, lkjId, dir, mp4List);
            } catch (Exception e) {
                log.error("项点 [", f.getClass().getName() + "]异常", e);
            }
        });
    }
}
