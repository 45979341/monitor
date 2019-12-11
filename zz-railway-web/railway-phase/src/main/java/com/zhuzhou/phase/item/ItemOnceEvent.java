package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/10/17 10:47
 * @description: 项点：
 * 十二到十三 38-49
 * 十六到二十四 55-74
 * 二十八 82 83
 * 二十九 86 87 88 89 90
 * 三十一 95 96 99
 * 三十二 109 110 123 126 140
 */
@Service
public class ItemOnceEvent extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        for (int i = 0; i < list.size(); i++) {
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();
            //十二、解锁
            //开车状态
            if ("路票解锁".equals(eventItem)) {
                Lkj cur = searchPrevious("路票记录号", list, i);
                if (cur != null) {
                    System.out.println(cur.getOther());
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I12_38)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_38).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                }
            } else if ("临时路票解锁".equals(eventItem)) {
                if (searchPrevious("确认键", list, i) != null
                        && searchPrevious("解锁键", list, i) != null
                        && searchPrevious("临时路票命令号", list, i) != null
                        && searchPrevious("临时路票记录号", list, i) != null) {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I12_39)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_39).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                }
            } else if ("绿证解锁".equals(eventItem)) {
                if (searchPrevious("确认键", list, i) != null
                        && searchPrevious("解锁键", list, i) != null
                        && searchPrevious("绿色凭证号", list, i) != null) {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I12_40)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_40).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                }
            } else if ("临时绿证解锁".equals(eventItem)) {
                if (searchPrevious("确认键", list, i) != null
                        && searchPrevious("解锁键", list, i) != null
                        && searchPrevious("临时绿色命令号", list, i) != null
                        && searchPrevious("临时绿证凭证号", list, i) != null) {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I12_41)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_41).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                }
            } else if ("引导解锁".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I12_42)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_42).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            } else if ("特定引导解锁".equals(eventItem)) {
                if (searchPrevious("解锁键", list, i) != null) {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I12_43)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_43).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                }
            } else if ("靠标解锁".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I12_44)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_44).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            } else if ("解锁成功".equals(eventItem)) {
                if (searchPrevious("解锁键", list, i) != null
                        && searchPrevious("调车停车", list, i) != null) {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I12_45)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_45).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                }
            } else if ("股道无码确认".equals(eventItem)) {
                Lkj cur = searchPrevious("确认键", list, i);
                if (cur != null) {
                    Lkj temp = searchPrevious("解锁键", list, i);
                    if (temp != null) {
                        System.out.println(temp);
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                                .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I12_46)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_46).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                    }
                }
            } else if ("绿/绿黄确认".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I12_47)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_47).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            } else if ("特殊无码解锁".equals(eventItem)) {
                if (searchPreviousLimit("确认键", list, i) != null && searchPreviousLimit("解锁键", list, i) != null) {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I12_48)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_48).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                }
            }
            // 十三、关机 Item49 途中关机
            else if ("关机".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I13_49)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_49).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            }
            // 十六、LKJ动作  重复先不做
            else if ("紧急制动".equals(eventItem)) {
                if (searchPreviousLimit("防溜报警开始", list, i) != null) {
                    // 往上只找到防溜报警开始
                    Lkj startLkj = searchPreviousLimit("防溜报警开始", list, i);
                    int limit = list.indexOf(startLkj);
                    Lkj endLkj = searchPreviousLimit("防溜报警结束", list, i, limit);
                    if (endLkj == null) {
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(startLkj.getTime())
                                .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I16_56)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_56).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                    } else {
                        // 如果有防溜报警结束就是其他紧急制动
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                                .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I16_61)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_61).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                    }
                } else if (searchPreviousLimit("警惕报警开始", list, i) != null) {
                    Lkj startLkj = searchPreviousLimit("警惕报警开始", list, i);
                    int limit = list.indexOf(startLkj);
                    Lkj endLkj = searchPreviousLimit("警惕报警结束", list, i, limit);
                    if (endLkj == null) {
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(startLkj.getTime())
                                .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I16_57)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_57).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                    } else {
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                                .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I16_61)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_61).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                    }
                } else if (searchPreviousLimit("平调信号", list, i) != null) {
                    Lkj startLkj = searchPreviousLimit("平调信号", list, i);
                    int index = list.indexOf(startLkj);
                    if (searchNext("调车开车", list, index, i) != null && searchNext("实施卸载", list, index, i) != null) {
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(startLkj.getTime())
                                .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I16_58)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_58).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                    }
                } else if (searchPreviousLimit("磁钢信号", list, i) != null) {
                    Date startTime = searchPreviousLimit("磁钢信号", list, i).getTime();
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(startTime)
                            .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I16_59)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_59).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                } else if (searchPreviousLimit("ZTL报警", list, i) != null) {
                    Date startTime = searchPreviousLimit("ZTL报警", list, i).getTime();
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(startTime)
                            .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I16_60)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_60).setItemRecordIllegal(2).setPhaseIllegal(1).builder());
                }
                // 超速模式紧急制动
                else if (lkj.getSpeed() >= NumberUtils.toInt(lkj.getRateLimit())) {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I16_55)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_55).setItemRecordIllegal(2).setPhaseIllegal(1).builder());
                } else {
                    // 其他紧急制动
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I16_61)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_61).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                }
            } else if ("实施常用".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I16_62)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_62).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            } else if ("实施卸载".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I16_63)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_63).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            }
            // 十七
            else if ("进入特殊前行".equals(eventItem)) {
                String other = lkj.getOther();
                // distance 为前行距离数
                int distance = NumberUtils.toInt(other.split(",")[1]);
                System.out.println(distance);
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I17_64)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_64).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            }
            // 十八
            else if ("降级运行".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I18_65)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_65).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            }
            // 十九
            else if (eventItem.contains("转入20km/h限速")) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I19_66)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_66).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            }
            // 二十
            else if ("当前揭示解除".equals(eventItem) || "调度命令号".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I20_67)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_67).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            } else if ("车位向前".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I21_68)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_68).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            }
            // 二十一  车位向后 车位对中 其他列大于50违规 小于等于50则记录
            else if ("车位向后".equals(eventItem)) {
                if (NumberUtils.toInt(lkj.getOther()) > 50) {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I21_69)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_69).builder());
                } else {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I21_69)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_69).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                }
            } else if ("车位对中".equals(eventItem)) {
                if (NumberUtils.toInt(lkj.getOther()) > 50) {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I21_70)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_70).builder());
                } else {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I21_70)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_70).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                }
                // 三十二 225
                if ("出站".equals(list.get(i + 1).getEventItem()) && NumberUtils.toInt(lkj.getOther()) > 40) {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I32_126)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_126).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                }
            }
            // 二十二
            else if ("地面信号确认".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I22_71)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_71).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            }
            // 二十四
            else if ("轮对滑行".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I24_73)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_73).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            } else if ("空转".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I24_74)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_74).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            }
            // 二十八
            else if ("速度通道人工切换".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I28_82)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_82).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            } else if ("信号突变".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I28_83)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_83).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            }
            // 二十九
            else if ("区间停车".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I29_86)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_86).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            } else if ("站内调车".equals(eventItem)) {
                if (searchNextBeforeEvent("进入调车", list, i, "站内开车") != null) {
                    // 中间站调车
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I29_90)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_90).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                } else {
                    // 站内停车
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I29_87)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_87).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                }
                // 揭示输入
            } else if ("区间限速揭示".equals(eventItem)) {
                Date startTime = lkj.getTime();
                do {
                    lkj = list.get(++i);
                    if (!"区间限速揭示".equals(lkj.getEventItem())) {
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(startTime)
                                .setEnd(list.get(i - 1).getTime()).setItemEnum(ItemRecord.ItemEnum.I29_88)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_88).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                        i -= 1;
                        break;
                    }
                } while (i + 1 < list.size());
                // 揭示控制
            } else if ("临时限速起点".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I29_89)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_89).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            }
            // 三十一 194，195，198
            else if ("警惕键".equals(eventItem)) {
                Date startTime = lkj.getTime();
                do {
                    lkj = list.get(++i);
                    if (!"警惕键".equals(lkj.getEventItem())) {
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(startTime)
                                .setEnd(list.get(i - 1).getTime()).setItemEnum(ItemRecord.ItemEnum.I31_95)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_95).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                        i -= 1;
                        break;
                    }
                } while (i + 1 < list.size());
            } else if ("支线输入".equals(eventItem)) {
                String other = lkj.getOther();
                System.out.println(other);
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I31_96)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_96).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            } else if ("定标键".equals(eventItem)) {
                // 连续出现计录一次
                Date startTime = lkj.getTime();
                do {
                    lkj = list.get(++i);
                    if (!"定标键".equals(lkj.getEventItem())) {
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(startTime)
                                .setEnd(list.get(i - 1).getTime()).setItemEnum(ItemRecord.ItemEnum.I31_99)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_99).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                        i -= 1;
                        break;
                    }
                } while (i + 1 < list.size());
            } else if ("紧急制动实验".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I31_107)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_107).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            } else if ("常用制动试验".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I31_108)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_108).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            }
            // 三十二 208 209 222 239
            else if ("退出出段".equals(eventItem)) {
                Lkj cur = searchNext("入段", list, i);
                if (cur != null) {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I32_109)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_109).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                }
            } else if ("惩罚制动".equals(eventItem)) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                        .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I33_140)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_140).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            }
        }

    }

    /**
     * 往上搜索指定事件
     */
    private static Lkj searchPrevious(String event, List<Lkj> list, int index) {
        for (int i = 1; i < index; i++) {
            Lkj lkj = list.get(index - i);
            if (event.equals(lkj.getEventItem())) {
                return lkj;
            }
        }
        return null;
    }

    /**
     * 在往前指定条数(50)之内寻找指定事件
     */
    private static Lkj searchPreviousLimit(String event, List<Lkj> list, int index) {
        int limit = 50;
        for (int i = 1; i < index && i <= limit; i++) {
            Lkj lkj = list.get(index - i);
            if (event.equals(lkj.getEventItem())) {
                return lkj;
            }
        }
        return null;
    }

    /**
     * 在limit之后找到制定事件
     */
    private static Lkj searchPreviousLimit(String event, List<Lkj> list, int index, int limit) {
        for (int i = index; i > limit; i--) {
            Lkj lkj = list.get(i);
            if (event.equals(lkj.getEventItem())) {
                return lkj;
            }
        }
        return null;
    }

    /**
     * 往下搜索指定事件
     */
    private static Lkj searchNext(String event, List<Lkj> list, int index) {
        for (int i = 1; i + index < list.size(); i++) {
            Lkj lkj = list.get(index + i);
            if (event.equals(lkj.getEventItem())) {
                return lkj;
            }
        }
        return null;
    }

    /**
     * 往下搜索，在指定索引前找指定事件
     */
    private static Lkj searchNext(String event, List<Lkj> list, int index, int beforeIndex) {
        for (int i = 1; i + index < beforeIndex; i++) {
            Lkj lkj = list.get(index + i);
            if (event.equals(lkj.getEventItem())) {
                return lkj;
            }
        }
        return null;
    }

    /**
     * 往下搜索，在beforeEvent之前找到event
     */
    private static Lkj searchNextBeforeEvent(String event, List<Lkj> list, int index, String beforeEvent) {
        for (int i = 1; i + index < list.size(); i++) {
            Lkj lkj = list.get(index + i);
            if (beforeEvent.equals(lkj.getEventItem())) {
                break;
            }
            if (event.equals(lkj.getEventItem())) {
                return lkj;
            }
        }
        return null;
    }
}
