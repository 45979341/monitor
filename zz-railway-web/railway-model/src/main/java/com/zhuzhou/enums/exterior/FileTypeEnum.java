package com.zhuzhou.enums.exterior;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-06-19
 * @Description: 文件类型枚举
 **/
@AllArgsConstructor
@Getter
public enum FileTypeEnum {
    VIDEO(1, "视频", Arrays.asList(new String[]{"ram", "rmvb", "flv", "mp4", "wmv", "avi", "mov"})),
    PIC(2, "图片", Arrays.asList(new String[]{"jpg", "jpeg", "gif", "png", "bmp"})),
    AUDIO(3, "音频", Arrays.asList(new String[]{"mp3", "wav", "midi", "vqf"}));

    public Integer id;
    public String desc;
    public List<String> postfixs;

    public static FileTypeEnum getFileType(String exName) {
        if (StringUtils.isEmpty(exName)) {
            return null;
        }
        for (FileTypeEnum each : values()) {
            List<String> list = each.getPostfixs();
            for (String postfix : list) {
                if (postfix.equalsIgnoreCase(exName)) {
                    return each;
                }
            }
        }
        return null;
    }
}