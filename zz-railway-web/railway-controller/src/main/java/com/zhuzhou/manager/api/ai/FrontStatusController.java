package com.zhuzhou.manager.api.ai;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhuzhou.spi.ai.FrontStatusService;
/**
 * <p>
 *  前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-06-06
 * Description:
 */
@Controller
public class FrontStatusController {
    @Autowired
    private FrontStatusService frontStatusService;
}
