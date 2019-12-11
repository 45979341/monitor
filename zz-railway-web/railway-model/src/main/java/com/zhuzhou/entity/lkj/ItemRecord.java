package com.zhuzhou.entity.lkj;

import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableName;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

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
 * @author chenzeting
 * @since 2019-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("item_record")
public class ItemRecord implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联lkj_index的id
     */
    private String lkjId;

    /**
     * 项目（30大项）编号
     */
    private Integer majorId;
    /**
     * 项点编号
     */
    private Integer itemId;

    /**
     * 违章标准（0：违章，1：正常，2：记录）
     */
    private Integer illegalStand;

    /**
     * lkj文件序号
     */
    private String lkjNo;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    @Getter
    @AllArgsConstructor
    public enum ItemEnum {

        I1_1(1, 1),
        I1_2(1, 2),
        I1_3(1, 3),
        I1_4(1, 4),
        I1_5(1, 5),
        I1_6(1, 6),

        I2_7(2, 7),
        I2_8(2, 8),
        I2_9(2, 9),

        I3_10(3, 10),
        I4_11(4, 11),
        I4_12(4, 12),
        I4_13(4, 13),
        I5_14(5, 14),
        I6_15(6, 15),
        I7_16(7, 16),
        I7_19(7, 19),
        I7_20(7, 20),
        I7_21(7, 21),
        I7_28(7, 28),

        I8_22(8, 22),
        I8_23(8, 23),
        I8_24(8, 24),
        I8_25(8, 25),
        I8_26(8, 26),
        I8_27(8, 27),
        I8_28(8, 28),
        I8_29(8, 28),

        I9_29(9, 29),
        I9_30(9, 30),
        I9_31(9, 31),
        I9_32(9, 32),

        //I10_31(10,33),
        I10_33(10, 33),
        I10_35(10, 35),
        I10_34(10, 34),
        I10_36(10, 36),
        I11_37(11, 37),

        I12_38(11, 38),
        I12_39(12, 39),
        I12_40(12, 40),
        I12_41(12, 41),
        I12_42(12, 42),
        I12_43(12, 43),
        I12_44(12, 44),
        I12_45(12, 45),
        I12_46(12, 46),
        I12_47(12, 47),
        I12_48(12, 48),

        I13_49(13, 49),

        I14_51(14, 51),

        I15_54(15, 54),

        I16_55(16, 55),
        I16_56(16, 56),
        I16_57(16, 57),
        I16_58(16, 58),
        I16_59(16, 59),
        I16_60(16, 60),
        I16_61(16, 61),
        I16_62(16, 62),
        I16_63(16, 63),

        I17_64(17, 64),
        I18_65(18, 65),
        I19_66(19, 66),
        I20_67(20, 67),

        I21_68(21, 68),
        I21_69(21, 69),
        I21_70(21, 70),

        I22_71(22, 71),

        I23_72(23, 72),

        I24_73(24, 73),
        I24_74(24, 74),

        I25_75(25, 75),
        I25_76(25, 76),
        I25_77(25, 77),
        I25_78(25, 78),
        I25_79(25, 79),

        I26_80(26, 80),
        I27_81(27, 81),

        I28_82(28, 82),
        I28_83(28, 83),
        I28_84(28, 84),
        I28_85(28, 85),

        I29_86(29, 86),
        I29_87(29, 87),
        I29_88(29, 88),
        I29_89(29, 89),
        I29_90(29, 90),

        I31_95(31, 95),
        I31_96(31, 96),
        I31_98(31, 98),
        I31_99(31, 99),
        I31_102(31, 102),
        I31_103(31, 103),
        I31_104(31, 104),
        I31_105(31, 105),
        I31_107(31, 107),
        I31_108(31, 108),

        I32_109(32, 109),
        I32_110(32, 110),
        I32_112(32, 112),
        I32_113(32, 113),
        I32_115(32, 115),
        I32_116(32, 116),
        I32_117(32, 117),
        I32_118(32, 118),
        I32_119(32, 119),
        I32_121(32, 121),
        I32_123(32, 123),
        I32_124(32, 124),
        I32_125(32, 125),
        I32_126(32, 126),
        I32_128(32, 128),
        I32_132(32, 132),
        I32_131(32, 131),
        I32_133(32, 133),
        I32_134(32, 134),
        I32_135(32, 135),
        I32_136(32, 136),

        I33_137(33, 137),
        I33_138(33, 138),
        I33_140(33, 140),

        ;

        private int majorId;
        private int itemId;
    }
}
