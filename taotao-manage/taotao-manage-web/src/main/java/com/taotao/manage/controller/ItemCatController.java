package com.taotao.manage.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.abel533.mapper.Mapper;
import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.BaseService;
import com.taotao.manage.service.ItemCatService;

@Controller
@RequestMapping("item/cat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;
    
    /**
     * 根据条件查询数据
     * 
     * @param parentId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ItemCat>> queryItemCatListByParentId(@RequestParam(value = "id",defaultValue="0") Long parentId){
            try {
//                List<ItemCat> list = this.itemCatService.queryItemCatListByParentId(parentId);
                ItemCat record = new ItemCat();
                record.setParentId(parentId);
                List<ItemCat> list = this.itemCatService.queryListByWhere(record);
                if(null == list || list.isEmpty()){
                    //资源不存在
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                return ResponseEntity.ok(list);
            } catch (Exception e) {
                e.printStackTrace();
            }            
        //500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    
}
