package com.br.cobra.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/")
@Controller
public class IndexController {
    
    @RequestMapping(value="/",method=RequestMethod.GET)
    public String index(){
        return "redirect:/user_index/list";
    }
    

}
