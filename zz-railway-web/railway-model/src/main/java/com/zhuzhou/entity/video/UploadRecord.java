package com.zhuzhou.entity.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhuzhou.consts.SymbolConst;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.framework.utils.stomp.ReflectionUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenzeting
 * @since 2019-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("upload_record")
@NoArgsConstructor
public class UploadRecord implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * lkj路径
     */
    private String lkjFile;

    /**
     * idx路径
     */
    private String idxFile;

    /**
     * 目录
     */
    private String dir;

    /**
     * 机车号
     */
    private String cabNum;

    /**
     * 车次
     */
    private String trainNum;

    /**
     * 司机号
     */
    private String driverNum;

    /**
     * 司机名
     */
    private String driverName;

    /**
     * 文件数
     */
    private Integer fileNum;

    /**
     * 日期
     */
    private String date;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    public UploadRecord(Builder builder) {
        ReflectionUtils.copyProperties(this, builder);
    }

    @Data
    @Accessors(chain = true)
    public static class Builder {

        /**
         * lkj路径
         */
        private String lkjFile;

        /**
         * idx路径
         */
        private String idxFile;

        /**
         * 目录
         */
        private String dir;

        /**
         * 机车号
         */
        private String cabNum;

        /**
         * 车次
         */
        private String trainNum;

        /**
         * 司机号
         */
        private String driverNum;

        /**
         * 司机名
         */
        private String driverName;

        /**
         * 文件数
         */
        private Integer fileNum;

        /**
         * 日期
         */
        private String date;

        /**
         * 开始时间
         */
        private String startTime;

        /**
         * 结束时间
         */
        private String endTime;

        /**
         * 创建时间
         */
        private Timestamp createTime;

        public Builder create () {
            return new Builder();
        }

        public Builder setStartTime (String time) {
            this.startTime = getStringBuffer(time);
            return this;
        }
        public Builder setStartTime (Date date) {
            String time = LocalDateTime.fromDateFields(date).toString("HH:mm:ss");
            this.startTime = time;
            return this;
        }

        public Builder setEndTime (Date date) {
            String time = LocalDateTime.fromDateFields(date).toString("HH:mm:ss");
            this.endTime = time;
            return this;
        }

        public Builder setEndTime (String time) {
            try {
                //结束时间+1500，视为视频15分钟后结束
                time = String.valueOf(Integer.parseInt(time));
            } catch (Exception e) {
            }
            this.endTime = getStringBuffer(time);
            return this;
        }

        public Builder setDate (String date) {
            Date parse = DateUtils.parse(DateUtils.yymd, date);
            String format = DateUtils.format(DateUtils.ymd, parse);
            this.date = format;
            return this;
        }

        private String getStringBuffer(String time) {
            while (time.length() < 6) {
                time = "0" + time;
            }
            char[] startChar = time.toCharArray();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < startChar.length; i++) {
                if (i > 0 && i % 2 == 0) {
                    sb.append(SymbolConst.COLON);
                }
                sb.append(startChar[i]);
            }
            return sb.toString();
        }

        public UploadRecord build () {
            return new UploadRecord(this);
        }
    }
}
