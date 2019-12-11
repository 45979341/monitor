package com.zhuzhou.entity.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenzeting
 * @since 2019-04-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("idx")
public class Idx implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联upload_record记录id
     */
    private String recordId;

    /**
     * 报文类型（F3，07）
     */
    private String messageType;

    /**
     * 中央处理平台时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date platformTime;

    /**
     * 中央处理平台车号
     */
    private String platformWagonNum;

    /**
     * 预留字节
     */
    private Integer taxValue;

    /**
     * 车次字母部分
     */
    private String alpTrainNum;

    /**
     * 车次数字部分
     */
    private String digTrainNum;

    /**
     * 车站号
     */
    private String stationNum;

    /**
     * 车站名
     */
    private String stationName;

    /**
     * 司机号
     */
    private String driverNum;

    /**
     * 司机名
     */
    @TableField(exist = false)
    private String driverName;

    /**
     * 副司机号
     */
    @TableField(exist = false)
    private String assisDriverNum;

    /**
     * 副司机号
     */
    private String assisDriverName;

    /**
     * 实际交路号
     */
    private String crossRoadNum;

    /**
     * 客/货、本/补（b0：0/1=货/客，b1：0/1=本务/补机）
     */
    private String goodsNum;

    /**
     * 速度
     */
    private Integer speed;

    /**
     * 限速
     */
    private Integer rateLimit;

    /**
     * 机车信号（00-无灯,01-绿,02-黄,03-双黄,04-红黄,05-红,06-白,07-绿黄,08-黄2)
b1:0/1=单灯/多灯
b0:0-8=颜色灯
     */
    private String cabSignal;

    /**
     * 机车工况（0：零位,1：向后[即二端向前],2：向前[即一端向前],3：制动,4：牵引）
     */
    private String cabStatus;

    /**
     * 信号机编号
     */
    private String signalNum;

    /**
     * 信号机种类（b2～b0: 02-出站,03-进站,04-通过,05-预告,06-容许）
     */
    private String signalType;

    /**
     * 公里标（米）
     */
    private String kilometre;

    /**
     * 总重（吨）
     */
    private Integer weight;

    /**
     * 计长（0.1米）
     */
    private Integer meterLengh;

     /**
     * 辆数
     */
    private Integer trainNum;

    /**
     * 列车管压力（kPa）
     */
    private Integer trainPipePressure;

    /**
     * 装置状态（b0:1/0-降级/监控，b2:1/0-调车/非调车）
     */
    private String deviceStatus;

    /**
     * TCMS报文有效
     */
    private String tcms;

    /**
     * 司机室状态（0-没占用，1-一端占用，2-二端占用，0xff-无效)
     */
    private String driverCabStatus;

    /**
     * 受电弓状态（0-无效，1-升弓，2-降弓，3-隔离）
     */
    private String pantographStatus;

    /**
     * 主断状态（1-断开，2-闭合，0xFF-无效）
     */
    private String mainRiftStatus;

    /**
     * 手柄级位（×0.1级,0xFFFF无效）
     */
    private String handleLevel;

    /**
     * 重联信息（1-重联，2-不重联，0xFF-无效）
     */
    private String reconnectionInfo;

    /**
     * 大闸指令（0-运转位，1-初制动，2-常用制动区，3-全制动，4-抑制位，5-重联位，6-紧急制动位，0xFF-无效）
     */
    private String bigBrakeCommand;

    /**
     * 小闸指令（0-运转位，1-制动区，2-全制动，0xFF-无效）
     */
    private String smallBrakeCommand;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 事件项点1
{0-特殊区域
1-过分相点	
2-进入调车	
3-人为紧急	
4-侧线运行
5-监控动作	
6-停车事件	
7-开车事件
}
     */
    private String eventPoint1;

    /**
     * 事件项点2
{0-调车开行	
1-退出调车
2-减压制动	
3-黄灯信号	
4-继乘交接	
5-监控解锁	
6-总风低压	
7-红黄信号
}
     */
    private String eventPoint2;

    /**
     * 事件项点3
{0-轴温报警
1-视频中断	
2-打盹报警	
3-特殊通行	
4-乘务巡检	
5-防火报警	
6-内燃停机	
7-开关视频}
     */
    private String eventPoint3;

    /**
     * 检测状态{0表示正常，1表示一级瞭望提醒，2表示二级瞭望提醒， 3表示三级瞭望提醒， 4表示驾驶姿势提醒，5表示图像采集器遮挡，5表示图像采集器遮挡，5表示图像采集器遮挡，6抽烟，7打电话；}，板卡自检：			0表示广铁模式，1表示广义F机模式；，{一端摄像头自检：	0表示正常，1表示故障； 二端摄像头自检：	0表示正常，1表示故障；}
     */
    private String detectionStatus;

    /**
     * 标志位
     */
    private String flagBit;


}
