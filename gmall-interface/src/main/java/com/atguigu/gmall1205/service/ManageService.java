package com.atguigu.gmall1205.service;

import com.atguigu.gmall1205.bean.*;

import java.util.List;

public interface ManageService {

    /**
     * 查询一级分类
     * @return
     */
    List<BaseCatalog1> getCatalog1();

    /**
     *  根据一级分类Id 查询二级分类
     * @param catalog1Id
     * @return
     */
    List<BaseCatalog2> getCatalog2(String catalog1Id);


    /**
     *  根据二级分类Id 查询三级分类
     * @param catalog2Id
     * @return
     */
    List<BaseCatalog3> getCatalog3(String catalog2Id);

    /**
     *  根据三级分类Id 查询平台属性
     * @param catalog3Id
     * @return
     */
    List<BaseAttrInfo> getAttrList(String catalog3Id);

    /**
     * 保存平台属性
     * @param baseAttrInfo
     */
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    /**
     *  根据平台属性id 查询平台属性值集合
     * @param attrId
     * @return
     */
    List<BaseAttrValue> getAttrValueListByAttrId(String attrId);

    /**
     *  根据平台属性值的attrId 获取平台属性对象
     * @param attrId
     * @return
     */
    BaseAttrInfo getAttrInfo(String attrId);
}
