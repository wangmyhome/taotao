package com.taotao.sso.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.com.utils.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;

@RequestMapping("user")
@Controller
public class UserController {
    
    @Autowired
    private UserService userService;
    
    public static final String COOKIE_NAME = "TT_TOKEN";
    

    /**
     * 跳转到登录页面
     * 
     * @return
     */
    @RequestMapping(value = "login",method = RequestMethod.GET)
    public String toLogin(){
        return "login";
    }
    
    /**
     * 跳转到注册页面
     * 
     * @return
     */
    @RequestMapping(value = "register",method = RequestMethod.GET)
    public String toRegister(){
        return "register";
    }
    
    @RequestMapping(value = "check/{param}/{type}",method = RequestMethod.GET)
    public ResponseEntity<Boolean> check(@PathVariable("param")String param,
            @PathVariable("type")Integer type){
        try {
            Boolean bool = this.userService.check(param,type);
            if(null == bool){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            //为了兼容前端的业务逻辑做出妥协处理
            return ResponseEntity.ok(!bool);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    @RequestMapping(value = "doRegister",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String,Object>> doRegister(@Valid User user,BindingResult bindingResult){
        Map<String,Object> result = new HashMap<String,Object>();
        if(bindingResult.hasErrors()){
            //校验有错误
            List<String> msgs = new ArrayList<String>();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError : allErrors) {
                msgs.add(objectError.getDefaultMessage());
            }
            result.put("status", "400");
            result.put("data", StringUtils.join(msgs,'|'));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        
        Boolean bool = this.userService.saveUser(user);
        if(bool){
            //注册成功
            result.put("status","200");
        }else{
            result.put("status","300");
        }
        return ResponseEntity.ok().body(result);
    }
    
    @RequestMapping(value = "doLogin",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> doLogin(@RequestParam("username")String username,
            @RequestParam("password")String password,
            HttpServletRequest request,HttpServletResponse response
            ){
        Map<String,Object> result = new HashMap<String,Object>();
        try {
            String token = this.userService.doLogin(username,password);
            if(token == null){
                //登陆失败
                result.put("status",400);
            }else{
                //登陆成功，需要将token写入到cookie中
                result.put("status",200);
                CookieUtils.setCookie(request, response, COOKIE_NAME, token);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //登陆失败
            result.put("status", 500);
        }
        return result;
    }
    
    @RequestMapping(value = "{token}",method = RequestMethod.GET)
    public ResponseEntity<User> queryUserByToken(@PathVariable("token")String token){
//        try {
//            User user = this.userService.queryUserToken(token);
//            if(null == user){
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }
//            return ResponseEntity.ok(user);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        User user = new User();
        user.setUsername("该服务没有了，以后别调用了，请访问ssoquery.taotao.com或Dubbo中的服务");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
    }
}
