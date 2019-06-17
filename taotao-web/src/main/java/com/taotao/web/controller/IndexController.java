package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.service.IndexService;

@Controller
public class IndexController {
 
    @Autowired
    private IndexService indexService;
    
    @RequestMapping(value = "index",method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("index");
        
        //大广告位数据
        String indexAd1 = this.indexService.queryIndexAD1();
        mv.addObject("indexAD1",indexAd1);
        return mv;
    }
}
