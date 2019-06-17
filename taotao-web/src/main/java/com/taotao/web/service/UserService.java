package com.taotao.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.sso.query.api.UserQueryService;
import com.taotao.sso.query.bean.User;

@Service
public class UserService {
    
//    @Autowired
//    private ApiService apiService;
//   
//    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Autowired
    private UserQueryService userQueryService;
    
    public User queryUserByToken(String token){
        
        return this.userQueryService.queryUserByToken(token);
    }
//    public User queryUserByToken(String token){
//        try {
//            String url = "http://sso.taotao.com/service/user/"+token;
//            String jsonData = this.apiService.doGet(url);
//            if(StringUtils.isNotEmpty(jsonData)){
//                return MAPPER.readValue(jsonData, User.class);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    
}
