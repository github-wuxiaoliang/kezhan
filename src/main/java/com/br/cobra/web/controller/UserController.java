package com.br.cobra.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.br.cobra.common.util.PropertiesUtil;
import com.br.cobra.web.constant.Constants;


@RequestMapping("/user")
@Controller
public class UserController extends BaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    
    @RequestMapping(value="loginOut",method={RequestMethod.GET})
    public String loginOut(HttpServletRequest req){
        req.getSession().setAttribute(Constants.SESSION_USER_INFO, null);
        
        String indexUrl = PropertiesUtil.getStringValue("internal.cas.url");
        
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("key", token);
//        try {
//            // 单点用户注销
//            String resultJson = HttpClientUtil.simpleGetInvoke(url, params);
//            JSONObject result = JSONObject.parseObject(resultJson);
//            if("success".equals(result.getString("status"))){
//                req.getSession().removeAttribute(Constants.SESSION_USER_INFO);
//                req.getSession().removeAttribute(Constants.SESSION_TOKEN);
//            }
//        } catch(Exception e){
//            LOGGER.error("用户注销失败."+e.getMessage(),e);
//        }

        return "redirect:"+indexUrl;
    }
    
}
