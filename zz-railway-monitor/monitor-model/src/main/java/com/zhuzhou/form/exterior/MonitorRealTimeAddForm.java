package com.zhuzhou.form.exterior;

import com.zhuzhou.framework.form.Form;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author 刘欣武
 * @Date 2019-12-02
 * @Description:接收推送实时数据表单
 **/
@Data
public class MonitorRealTimeAddForm implements Form {

    private String deviceId;
    /**
     * 经度
     */
    private BigDecimal gpsLng;
    /**
     * 纬度
     */
    private BigDecimal gpsTidu;
    /**
     * 方向
     */
    private BigDecimal gpsAngle;
    /**
     * 速度
     */
    private BigDecimal gpsSpeed;

    /**
     * 上下行方向
     */
    private Integer gpsDir;

    /**
     * 图标名称
     */
    private String gpsName;

    /**
     * 时间
     */
    private Timestamp alarmTime;

    /**
     * 设备类型
     */
    private Integer devType;

    /**
     * F机内部编码
     */
    private Integer devInfoCode;

    /**
     * 设备注册id
     */
    private Integer devRegId;

    /**
     * 设备号
     */
    private Integer deviceCode;

    /**
     * 车牌号码
     */
    private String carName;

    /**
     * 车次字串
     */
    private String ccStr;

    /**
     * 车次字母
     */
    private String trainNumStr;

    /**
     * 车次数字
     */
    private Integer trainNum;

    /**
     * 机车号字串
     */
    private String trainStr;
    /**
     * 机车型号
     */
    private String trainCode;

    /**
     * 机车编号
     */
    private Integer trainNumber;


    /**
     * 司机号
     */
    private Integer driverId;

    /**
     * 副司机号
     */
    private Integer viceDriverId;

    /**
     * 司机名
     */
    private String driverName;

    /**
     * 副司机名
     */
    private String viceDriverName;

    /**
     * 客货
     */
    private int keHuo;
    /**
     * 本补
     */
    private int benBu;
    /**
     * 辆数
     */
    private int count;

    /**
     * 总重
     */
    private Integer maxWeight;

    /**
     * 计长
     */
    private Integer maxLength;

    /**
     * 公里标
     */
    private Integer mileStone;

    /**
     * 当前速度
     */
    private Integer curSpeed;

    /**
     * 限速
     */
    private Integer limitSpeed;

    /**
     * 柴油机转速
     */
    private Integer rotateSpeed;

    /**
     * 管压
     */
    private Integer pressureValue;

    /**
     * 闸缸压力
     */
    private int brakePressure;
    /**
     * 信号机编号
     */
    private int signalMachineNum;

    /**
     * 信号机类型 2:出站,3:进站,4:通过,5:预告,6:容许
     */
    private int signalMachineType;

    /**
     * 信号机灯数 0:单灯 1:多灯
     */
    private int signalLEDType;


    /**
     * 信号状态 0:无灯,1:绿,2:黄,3:双黄
     */
    private int signalLEDStatus;
    /**
     * 机车工况:0:零位,1:向后,2:向前,3:制动 4:牵引
     */
    private Integer workStatus;

    /**
     * 制动输出 0卸载，2常用，6紧急
     */
    private Integer breakOut;

    /**
     * 装置状态 0监控 1降级
     */
    private Integer driveStatusA;

    /**
     * 装置状态 0非调车 1调车
     */
    private Integer driveStatusB;

    /**
     * 司机室占用
     */
    private Integer driverRoom;

    /**
     * JCGK
     */
    private Integer jcgk;

    /**
     * 受电弓状态
     */
    private Integer sdgStatus;

    /**
     * 主断状态
     */
    private Integer zdStatus;

    /**
     * 手柄级位
     */
    private Integer sbJiwei;

    /**
     * 重连信息
     */
    private int reconnect;
    /**
     * 大闸指令
     */
    private Integer dazhac;

    /**
     * 小闸指令
     */
    private Integer xiaozhac;

    /**
     * 其他指令
     */
    private Integer otherzdc;

    /**
     * 其他指令屏蔽字节
     */
    private Integer otherzdpc;

    /**
     * zdj预留字节
     */
    private int nzdj;
    /**
     * zdg预留字节
     */
    private Integer zdg;

    /**
     * zfg预留字节
     */
    private Integer zfg;

    /**
     * 基本交路号
     */
    private Integer baseRoad;

    /**
     * 虚拟交路号
     */
    private Integer virtuRoad;

    /**
     * 车站号
     */
    private Integer stationNum;

    /**
     * 累计位移
     */
    private int weiyi;
    /**
     * 本分区支线
     */
    private int fenquZhixian;
    /**
     * 本分区侧线
     */
    private int fenquCexian;

    /**
     * 前方支线
     */
    private int qianfangZhixian;
    /**
     * 前方侧线
     */
    private int qianfangCexian;
    /**
     * 状态:0正常,1禁用
     */
    private Integer status;

    /**
     * 是否删除:0正常,1删除
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 备注
     */
    private String remark;


}
