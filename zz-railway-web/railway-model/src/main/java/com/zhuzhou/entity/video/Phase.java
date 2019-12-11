package com.zhuzhou.entity.video;

import com.baomidou.mybatisplus.annotation.*;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableName;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("phase")
public class Phase implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 记录id
     */
    private String recordId;

    /**
     * 项点id
     */
    private Integer phaseId;

    /**
     * 相点名称
     */
    private String name;

    /**
     * 信号机类型
     */
    private String type;

    /**
     * 代码
     */
    private String code;

    /**
     * 最开始时间
     */
    private Date beginTime;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 对应开始时间视频地址
     */
    private String beginFile;

    /**
     * 视频地址
     */
    private String file;

    /**
     * 是否违章(0：违章，1：正常，2：视频缺失)
     */
    private Integer illegal;

    /**
     * 手势相点路径
     */
    private String handFile;

    @TableField(exist = false)
    private boolean phaseSign;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    @AllArgsConstructor
    @Getter
    public enum PhaseEnum {
        ENV_8 (147, "IDX", "开车事件"),
        ENV_7 (148, "IDX", "停车事件"),
        ENV_5 (149, "IDX", "监控动作（事件）"),
        ENV_9 (150, "IDX", "侧线运行（事件）"),
        ENV_A (151, "IDX", "人为紧急（事件）"),
        ENV_B (152, "IDX", "进入调车（事件）"),
        ENV_C (153, "IDX", "退出调车（事件）"),
        ENV_D (154, "IDX", "过分相点（事件）"),
        ENV_E (155, "IDX", "特殊区域（事件）"),
        ENV_F (156, "IDX", "调车开行（事件）"),
        ENV_G (157, "IDX", "红黄信号"),
        ENV_H (158, "IDX", "开关视频（事件）"),
        ENV_I (159, "IDX", "监控解锁（事件）"),
        ENV_J (160, "IDX", "内燃停机（事件）"),
        ENV_K (161, "IDX", "继乘交接（事件）"),
        ENV_L (162, "IDX", "总风低压（事件）"),
        ENV_M (163, "IDX", "机器间报警（事件）"),
        ENV_N (164, "IDX", "电器间报警（事件）"),
        ENV_O (165, "IDX", "黄灯信号（事件）"),
        ENV_Q (166, "IDX", "乘务巡检"),
        ENV_R (167, "IDX", "减压制动"),
        ENV_S (168, "IDX", "特殊通行"),
        ENV_T (169, "IDX", "打盹报警（事件）"),
        ENV_U (170, "IDX", "轨道异常（事件）"),

        LKJ_HEAD_1(141, "H", "通过信号(手比)"),
        LKJ_HEAD_2(142, "H", "准备停车信号(手比)"),
        LKJ_HEAD_3(143, "H", "侧线运行信号(手比)"),
        LKJ_HEAD_4(144, "H", "停车信号(手比)"),
        LKJ_HEAD_6(145, "H", "确认仪表(手比)"),
        LKJ_HEAD_7(146, "H", "探身瞭望(行为)"),

        LKJ_1 (1, "LKJ", "库内无最小减压"),
        LKJ_2 (2, "LKJ", "库内无最大减压"),
        LKJ_3 (3, "LKJ", "库内无过量减压"),
        LKJ_4 (4, "LKJ", "库内无紧急制动试验"),
        LKJ_5 (5, "LKJ", "库内无单阀试验"),
        LKJ_6 (6, "LKJ", "库内无过充试验"),
        LKJ_7 (7, "LKJ", "库内无柴油机调速试验"),
        LKJ_8 (8, "LKJ", "库内无工况试验"),
        LKJ_9 (9, "LKJ", "库内无高压试验"),
        LKJ_10 (10, "LKJ", "库内超速"),
        LKJ_11 (11, "LKJ", "连挂前无一度停车"),
        LKJ_12 (12, "LKJ", "连挂超速"),
        LKJ_13 (13, "LKJ", "连挂后拉钩试验"),
        LKJ_14 (14, "LKJ", "未按规定对标"),
        LKJ_15 (15, "LKJ", "贯通试验违标"),
        LKJ_16 (16, "LKJ", "始发站制动机未试验"),
        LKJ_17 (17, "LKJ", "第一停车站前未试闸"),
        LKJ_18 (18, "LKJ", "第一临时限速点前未试闸"),
        LKJ_19 (19, "LKJ", "停车超20分未简略试验"),
        LKJ_20 (20, "LKJ", "中间站本务机调车结束未简略试验"),
        LKJ_21 (21, "LKJ", "紧急制动后开车未试闸"),
        LKJ_22 (22, "LKJ", "初减压不足"),
        LKJ_23 (23, "LKJ", "累计减压超过最大减压量"),
        LKJ_24 (24, "LKJ", "列车管压过量供给"),
        LKJ_25 (25, "LKJ", "紧急制动后违规缓解"),
        LKJ_26 (26, "LKJ", "停车后未保压"),
        LKJ_27 (27, "LKJ", "摘解未保压"),
        LKJ_28 (28, "LKJ", "货车低速缓解"),
        LKJ_29 (29, "LKJ", "单阀制动停车"),
        LKJ_30 (30, "LKJ", "抱闸运行"),
        LKJ_31 (31, "LKJ", "紧急制动后小闸违规缓解"),
        LKJ_32 (32, "LKJ", "货车大劈叉"),
        LKJ_33 (33, "LKJ", "电制停车"),
        LKJ_34 (34, "LKJ", "违规解除电制"),
        LKJ_35 (35, "LKJ", "电制带闸"),
        LKJ_36 (36, "LKJ", "小闸未缓解"),
        LKJ_37 (37, "LKJ", "管压为0"),
        LKJ_38 (38, "LKJ", "路票解锁"),
        LKJ_39 (39, "LKJ", "临时路票解锁"),
        LKJ_40 (40, "LKJ", "绿证解锁"),
        LKJ_41 (41, "LKJ", "临时绿证解锁"),
        LKJ_42 (42, "LKJ", "手信号引导解锁"),
        LKJ_43 (43, "LKJ", "特定引导解锁"),
        LKJ_44 (44, "LKJ", "靠标解锁"),
        LKJ_45 (45, "LKJ", "调车点式解锁"),
        LKJ_46 (46, "LKJ", "股道无码确认"),
        LKJ_47 (47, "LKJ", "绿/绿黄灯确认"),
        LKJ_48 (48, "LKJ", "特殊发码确认"),
        LKJ_49 (49, "LKJ", "途中关机"),
        LKJ_50 (50, "LKJ", "错设为非本务状态"),
        LKJ_51 (51, "LKJ", "错误输入侧线股道号(短输长)"),
        LKJ_52 (52, "LKJ", "客车错输速度等级"),
        LKJ_53 (53, "LKJ", "分相未达速"),
        LKJ_54 (54, "LKJ", "手柄非零位过分相"),
        LKJ_55 (55, "LKJ", "超模式限速紧急制动"),
        LKJ_56 (56, "LKJ", "防溜紧急制动"),
        LKJ_57 (57, "LKJ", "警惕功能紧急制动"),
        LKJ_58 (58, "LKJ", "平调紧急制动"),
        LKJ_59 (59, "LKJ", "防撞土档紧急制动"),
        LKJ_60 (60, "LKJ", "降级状态下紧急制动"),
        LKJ_61 (61, "LKJ", "其它紧急制动"),
        LKJ_62 (62, "LKJ", "触发常用"),
        LKJ_63 (63, "LKJ", "触发卸载"),
        LKJ_64 (64, "LKJ", "特殊前行"),
        LKJ_65 (65, "LKJ", "降级"),
        LKJ_66 (66, "LKJ", "20km/h限速"),
        LKJ_67 (67, "LKJ", "揭示解除"),
        LKJ_68 (68, "LKJ", "车位向前"),
        LKJ_69 (69, "LKJ", "车位向后"),
        LKJ_70 (70, "LKJ", "车位对中"),
        LKJ_71 (71, "LKJ", "出站绿/绿黄灯误确认"),
        LKJ_72 (72, "LKJ", "区间非正常停车"),
        LKJ_73 (73, "LKJ", "轮对滑行"),
        LKJ_74 (74, "LKJ", "起车连续空转"),
        LKJ_75 (75, "LKJ", "未缓解加载"),
        LKJ_76 (76, "LKJ", "违规换向"),
        LKJ_77 (77, "LKJ", "回手柄违规"),
        LKJ_78 (78, "LKJ", "列车站内停车违规"),
        LKJ_79 (79, "LKJ", "停车后未回手柄"),
        LKJ_80 (80, "LKJ", "单机未前移"),
        LKJ_81 (81, "LKJ", "未转换机车信号开车"),
        LKJ_82 (82, "LKJ", "速度通道人工切换"),
        LKJ_83 (83, "LKJ", "信号突变"),
        LKJ_84 (84, "LKJ", "柴速超速"),
        LKJ_85 (85, "LKJ", "转速为0"),
        LKJ_86 (86, "LKJ", "区间停车"),
        LKJ_87 (87, "LKJ", "站内停车"),
        LKJ_88 (88, "LKJ", "揭示输入"),
        LKJ_89 (89, "LKJ", "揭示控制"),
        LKJ_90 (90, "LKJ", "中间站调车"),
        LKJ_91 (91, "LKJ", "模式选择错误"),
        LKJ_92 (92, "LKJ", "大秦特殊发码确认"),
        LKJ_93 (93, "LKJ", "信号异常"),
        LKJ_94 (94, "LKJ", "高速走停走"),
        LKJ_95 (95, "LKJ", "警惕报警"),
        LKJ_96 (96, "LKJ", "支线输入"),
        LKJ_97 (97, "LKJ", "未巡检"),
        LKJ_98 (98, "LKJ", "巡检违标"),
        LKJ_99 (99, "LKJ", "定标"),
        LKJ_100 (100, "LKJ", "反向行车"),
        LKJ_101 (101, "LKJ", "尾部安全距离不足"),
        LKJ_102 (102, "LKJ", "库内未检查缓解状态"),
        LKJ_103 (103, "LKJ", "停车后小闸未制动"),
        LKJ_104 (104, "LKJ", "充风不足开车"),
        LKJ_105 (105, "LKJ", "区停距离不足"),
        LKJ_106 (106, "LKJ", "特定地点未鸣笛"),
        LKJ_107 (107, "LKJ", "紧急试验"),
        LKJ_108 (108, "LKJ", "常用试验"),
        LKJ_109 (109, "LKJ", "出入库未按出入库键"),
        LKJ_110 (110, "LKJ", "库内IC卡输入"),
        LKJ_111 (111, "LKJ", "库内未做警惕装置状态试验"),
        LKJ_112 (112, "LKJ", "信号不良出库"),
        LKJ_113 (113, "LKJ", "进站信号未确认"),
        LKJ_114 (114, "LKJ", "违章使用调车键"),
        LKJ_115 (115, "LKJ", "重联补机过早提手柄"),
        LKJ_116 (116, "LKJ", "电制转牵引时间短"),
        LKJ_117 (117, "LKJ", "确认调车信号时间短"),
        LKJ_118 (118, "LKJ", "补机未及时回手柄"),
        LKJ_119 (119, "LKJ", "停车时间长未按规定打点"),
        LKJ_120 (120, "LKJ", "一次追加减压超过初次减压量"),
        LKJ_121 (121, "LKJ", "两端制动"),
        LKJ_122 (122, "LKJ", "下闸未排完风缓解"),
        LKJ_123 (123, "LKJ", "继乘站未插卡"),
        LKJ_124 (124, "LKJ", "运行中超过规定速度"),
        LKJ_125 (125, "LKJ", "靠标停车超速"),
        LKJ_126 (126, "LKJ", "站内停车距离误差大"),
        LKJ_127 (127, "LKJ", "早晚点分析"),
        LKJ_128 (128, "LKJ", "超长列车缓解"),
        LKJ_129 (129, "LKJ", "区间变坡点速度控制"),
        LKJ_130 (130, "LKJ", "关键车站缓解"),
        LKJ_131 (131, "LKJ", "补机起车进级晚"),
        LKJ_132 (132, "LKJ", "主断后缓解列车"),
        LKJ_133 (133, "LKJ", "过分相操纵违规"),
        LKJ_134 (134, "LKJ", "补机未按规定校正"),
        LKJ_135 (135, "LKJ", "动车小闸试验"),
        LKJ_136 (136, "LKJ", "中间站停车未检查闸瓦间隙"),
        LKJ_137 (137, "LKJ", "库内未做走车试验"),
        LKJ_138 (138, "LKJ", "停车后手柄未回零"),
        LKJ_139 (139, "LKJ", "自定义项点"),
        LKJ_140 (140, "LKJ", "惩罚制动");

        private Integer phaseId;
        private String code;
        private String name;
    }


}
