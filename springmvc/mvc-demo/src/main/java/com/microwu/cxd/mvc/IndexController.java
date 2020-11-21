package com.microwu.cxd.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Description:
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/6   15:14
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView("index");
        view.addObject("welcome", "hello");
        return view;
    }

}