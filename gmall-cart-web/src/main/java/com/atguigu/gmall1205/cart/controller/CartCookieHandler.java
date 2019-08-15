package com.atguigu.gmall1205.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall1205.bean.CartInfo;
import com.atguigu.gmall1205.bean.SkuInfo;
import com.atguigu.gmall1205.config.CookieUtil;
import com.atguigu.gmall1205.service.ManageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作cookie中的数据
 */
@Component
public class CartCookieHandler {

    // 定义购物车名称
    private String cookieCartName = "CART";
    // 设置cookie 过期时间
    private int COOKIE_CART_MAXAGE=7*24*3600;

    @Reference
    private ManageService manageService;
    /**
     * 添加购物车
     * @param request
     * @param response
     * @param skuId
     * @param userId
     * @param skuNum
     */
    public void addToCart(HttpServletRequest request, HttpServletResponse response, String skuId, String userId, int skuNum) {

        /*
            1.  根据skuId ,userId查询购物车中是否有该商品，如果有则数量相加
            2.  如果没有，则直接添加到购物车
         */
        // 获取cookie 中的购物车数据
        String cartJson  = CookieUtil.getCookieValue(request, cookieCartName, true);
        // 获取出来的数据，是单独一个商品，还是有很多的商品？ cartJson 转换为cartInfo 的集合

        List<CartInfo> cartInfoList = new ArrayList<>();

        // 定义一个变量来记录是否有该商品
        boolean ifExist=false;
        if (StringUtils.isNotEmpty(cartJson)){
            cartInfoList = JSON.parseArray(cartJson, CartInfo.class);
            // 循环遍历当前集合判断是否有该商品Id
            for (CartInfo cartInfo : cartInfoList) {
                // 如果有该商品
                if (cartInfo.getSkuId().equals(skuId)){
                    // 数量相加
                    cartInfo.setSkuNum(cartInfo.getSkuNum()+skuNum);
                    // 赋值实时价格
                    cartInfo.setSkuPrice(cartInfo.getCartPrice());
                    // 修改变量
                    ifExist=true;
                    break;
                }
            }
        }
        // 如果ifExist=false 说明没有找到该商品，则新增到cookie 中
        if (!ifExist){
            // 则新增到cartInfoList中 根据skuId 查询到当前的商品信息
            SkuInfo skuInfo = manageService.getSkuInfo(skuId);
            // 创建一个对象
            CartInfo cartInfo = new CartInfo();
            // 属性赋值
            cartInfo.setSkuId(skuId);
            cartInfo.setCartPrice(skuInfo.getPrice());
            cartInfo.setSkuPrice(skuInfo.getPrice());
            cartInfo.setSkuName(skuInfo.getSkuName());
            cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());

            cartInfo.setUserId(userId);
            cartInfo.setSkuNum(skuNum);
            // 将cartInfo 放入cookie
            cartInfoList.add(cartInfo);
        }

        // 将修改之后的cartInfoList 放入cookie
        CookieUtil.setCookie(request,response,cookieCartName,JSON.toJSONString(cartInfoList),COOKIE_CART_MAXAGE,true);

    }

    /**
     * 查询购物车列表
     * @param request
     * @return
     */
    public List<CartInfo> getCartList(HttpServletRequest request) {

        // 获取购物车中的数据
        String cookieValue = CookieUtil.getCookieValue(request, cookieCartName, true);
        // 将cookieValue 转换为集合对象
        List<CartInfo> cartInfoList = JSON.parseArray(cookieValue, CartInfo.class);
        return cartInfoList;
    }

    /**
     * 删除购物车
     * @param request
     * @param response
     */
    public void deleteCartCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request,response,cookieCartName);
    }

    /**
     *
     * @param request
     * @param response
     * @param skuId
     * @param isChecked
     */
    public void checkCart(HttpServletRequest request, HttpServletResponse response, String skuId, String isChecked) {
        List<CartInfo> cartList = getCartList(request);
        if (cartList!=null && cartList.size()>0){
            // 循环遍历
            for (CartInfo cartInfo : cartList) {
                if (cartInfo.getSkuId().equals(skuId)){
                    cartInfo.setIsChecked(isChecked);
                }
            }
        }
        // 将最新的集合放入cookie
        CookieUtil.setCookie(request,response,cookieCartName,JSON.toJSONString(cartList),COOKIE_CART_MAXAGE,true);
    }
}
