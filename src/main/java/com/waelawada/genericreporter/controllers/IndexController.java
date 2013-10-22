package com.waelawada.genericreporter.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This
 *
 * @author: Wael Awada
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping("index")
    public String getIndexView(){
        return "index";
    }

    @RequestMapping("/")
    public String getIndexView2(){
        return "index";
    }

}
