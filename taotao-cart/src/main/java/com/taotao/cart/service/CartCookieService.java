package com.taotao.cart.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.mapper.Mapper;
import com.taotao.cart.bean.Item;
import com.taotao.cart.pojo.Cart;
import com.taotao.com.utils.CookieUtils;

@Service
public class CartCookieService {

    public static final String COOKTE_NAME ="TT_CART";
    
    public static final Integer COOKTE_TIME =60 * 60 * 24 *30 * 12;
    
    public static final ObjectMapper MAPPER = new ObjectMapper();
    @Autowired
    private ItemService itemService;
    
    public void addItemToCart(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = CookieUtils.getCookieValue(request, COOKTE_NAME,true);
        List<Cart> carts = null;
        if(StringUtils.isEmpty(cookieValue)){
            carts = new ArrayList<Cart>();
        }else{
            try {
                carts = MAPPER.readValue(cookieValue, MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
            } catch (Exception e) {
                e.printStackTrace();
                carts = new ArrayList<Cart>();
            }
        }
        
        //判断该商品在购物车是否在存在
        Cart cart = null;
        for (Cart c : carts) {
            if(c.getItemId().longValue() == itemId.longValue()){
                cart = c;
                break;
            }
        }
        if(null == cart){
            //不存在
            // 购物车中不存在该商品
            Item item = this.itemService.queryItemById(itemId);
            System.out.println(item);
            if(null == item){
                //TODO 给出用户提示
                return;
            }
            
            cart = new Cart();
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cart.setItemId(itemId);
            cart.setItemImage(item.getImages()[0]);
            cart.setItemTitle(item.getTitle());
            cart.setItemPrice(item.getPrice());
            cart.setNum(1); // TODO 先默认为1

            // 将Cart保存到数据库
           carts.add(cart);
            
        }else{
            //存在
            // 该商品已经存在购物车中
            cart.setNum(cart.getNum() + 1); // TODO 先默认为1
            cart.setUpdated(new Date());
        }
        //将集合写入到cookie
        try {
            CookieUtils.setCookie(request, response, COOKTE_NAME, MAPPER.writeValueAsString(carts),
                    COOKTE_TIME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Cart> queryCartList(HttpServletRequest request) {
        String cookieValue = CookieUtils.getCookieValue(request, COOKTE_NAME,true);
        List<Cart> carts = null;
        if(StringUtils.isEmpty(cookieValue)){
            carts = new ArrayList<Cart>();
        }else{
            try {
                carts = MAPPER.readValue(cookieValue, MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
            } catch (Exception e) {
                e.printStackTrace();
                carts = new ArrayList<Cart>();
            }
        }
        return carts;
    }

    public void updateNum(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        List<Cart> carts= queryCartList(request);
        //判断该商品在购物车是否在存在
        for (Cart c : carts) {
            if(c.getItemId().longValue() == itemId.longValue()){
                c.setNum(num);
                c.setUpdated(new Date());
                break;
            }
        }
        //将集合写入到cookie
        try {
            CookieUtils.setCookie(request, response, COOKTE_NAME, MAPPER.writeValueAsString(carts),
                    COOKTE_TIME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void deleteItem(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<Cart> carts= queryCartList(request);
        //判断该商品在购物车是否在存在
        for (Cart c : carts) {
            if(c.getItemId().longValue() == itemId.longValue()){
                carts.remove(c);
                break;
            }
        }
        //将集合写入到cookie
        try {
            CookieUtils.setCookie(request, response, COOKTE_NAME, MAPPER.writeValueAsString(carts),
                    COOKTE_TIME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
