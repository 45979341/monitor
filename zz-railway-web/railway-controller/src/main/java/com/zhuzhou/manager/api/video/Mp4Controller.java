package com.zhuzhou.manager.api.video;


import com.zhuzhou.consts.SymbolConst;
import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.form.video.Mp4KmLocationForm;
import com.zhuzhou.form.video.Mp4ListForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.spi.video.Mp4Service;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-03-20
 * Description:
 */
@Controller
public class Mp4Controller {
    @Autowired
    private Mp4Service mp4Service;

    /**
     * 获取视频数据
     * @param form
     */
    @RequiresPermissions("mp4:list")
    @GetMapping(name = "获取视频数据", value = "/mp4/list")
    public Result<Object> list (Mp4ListForm form) {
        List<Mp4> list = mp4Service.list(form);
        return Result.success().setData(list);
    }

    /**
     * 根据公里标定位视频
     * @return
     */
    @RequiresPermissions("mp4:km:location")
    @GetMapping(name = "根据公里标定位视频", value = "/mp4/km/location")
    public Result km (Mp4KmLocationForm form) {
        int count = mp4Service.kmLocation(form);
        return Result.success().setData(count);
    }

    @RequiresPermissions("mp4:test")
    @GetMapping(name = "mp4 test", value = "/mp4/test")
    public Result test () {
        File file = new File("D:\\zhuzhou\\source\\haulage_motor\\HXD3C0825-成都运达-000K1192-5016266-20180306");
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.getName().contains("mp4")) {
                String fileDir = "http://192.168.10.127:8300/source/HXD3C0825-成都运达-000K1192-5016266-20180306/";
                String name = f.getName();
                int substring = name.lastIndexOf(".mp4");
                String fileName = name.substring(0, substring);
                String[] under = fileName.split(SymbolConst.UNDER);
                Mp4 mp4 = new Mp4().setCabNum(under[0]).setFactoryName(under[1]).setChannelNum(under[2])
                        .setChannelName(under[3]).setPrefixDate(under[4]).setSuffixDate(under[5]).setRecordId("12");
                mp4Service.save(mp4);
            }
        }
        return Result.success();
    }
}
