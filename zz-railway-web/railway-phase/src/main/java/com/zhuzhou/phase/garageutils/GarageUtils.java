package com.zhuzhou.phase.garageutils;

import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.model.GarageAsist;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-15
 * @Description: 库内工具
 */
public class GarageUtils {

    /**
     * 返回定压
     * @param head
     * @return
     */
    public static GarageAsist getConstPipe (LkjHead head) {
        int constPipe = 0;   //定压
        GarageAsist asist = new GarageAsist();

        if (head.getTrainType().contains("客")) {
            asist.setConstPipe(600);
        } else {
            asist.setConstPipe(500);
        }
        return asist;
    }

}
