package com.atguigu.gmall1205.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall1205.bean.*;
import com.atguigu.gmall1205.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class MangeController {


    // 调用服务层
    @Reference
    private ManageService manageService;

    @RequestMapping("index")
    public String index(){
        // 返回一个index.html
        return "index";
    }

    @RequestMapping("getCatalog1")
    @ResponseBody
    public List<BaseCatalog1> getCatalog1(){
        return manageService.getCatalog1();
    }
    // localhost:8082/getCatalog2?catalog1Id=3
    @RequestMapping("getCatalog2")
    @ResponseBody
    public List<BaseCatalog2> getCatalog2(String catalog1Id){
        return manageService.getCatalog2(catalog1Id);
    }

    @RequestMapping("getCatalog3")
    @ResponseBody
    public List<BaseCatalog3> getCatalog3(String catalog2Id){
        return manageService.getCatalog3(catalog2Id);
    }

    //  http://localhost:8082/attrInfoList?catalog3Id=61
    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<BaseAttrInfo> attrInfoList(String catalog3Id){
        return manageService.getAttrList(catalog3Id);
    }


    // 将前台传递过来的Json 字符串转换为java对象
    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
        manageService.saveAttrInfo(baseAttrInfo);
        return "OK";
    }

    @RequestMapping("getAttrValueList")
    @ResponseBody
    public List<BaseAttrValue> getAttrValueList(String attrId){
        // 调用服务层
        // select * from baseAttrValue where attrId= attrId
        // return manageService.getAttrValueListByAttrId(attrId);
        // 从平台属性中获取平台属性值
        BaseAttrInfo baseAttrInfo = manageService.getAttrInfo(attrId);
        // 将平台属性值返回
        return baseAttrInfo.getAttrValueList();
    }

}
