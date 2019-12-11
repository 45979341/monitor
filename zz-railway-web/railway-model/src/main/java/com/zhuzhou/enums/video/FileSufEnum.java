package com.zhuzhou.enums.video;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author chenzeting
 * @Date 2019-03-21
 * @Description: 文件后缀名枚举
 **/
@AllArgsConstructor
@Getter
public enum FileSufEnum {

    IDX("idx"),
    MP4("mp4"),
    LKJ("lkj");

    private String suf;
}
