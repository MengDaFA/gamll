package com.atguigu.gmall1205.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall1205.bean.CartInfo;
import com.atguigu.gmall1205.bean.SkuInfo;
import com.atguigu.gmall1205.config.LoginRequire;
import com.atguigu.gmall1205.service.CartService;
import com.atguigu.gmall1205.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Reference
    private CartService cartService;
    @Autowired
    private CartCookieHandler cartCookieHandler;
    @Reference
    private ManageService manageService;

    @RequestMapping("addToCart")
    @LoginRequire(autoRedirect = false)
    public String addToCart(HttpServletRequest request, HttpServletResponse response){
        //从注解中得到userId
        String userId = (String) request.getAttribute("userId");
       //可以从前台获取到的数据
        String skuId = request.getParameter("skuId");
        String skuNum = request.getParameter("skuNum");
        //判断空为未登录，非空为登录
        if(userId!=null){
            //登录状态下，
            cartService.addToCart(skuId,userId,Integer.parseInt(skuNum));
        }else {
            // 用户未登录情况下添加购物车！ 放入cookie
            cartCookieHandler.addToCart(request,response,skuId,userId,Integer.parseInt(skuNum));
        }

        // 保存skuInfo到数据库
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);

        request.setAttribute("skuInfo",skuInfo);
        request.setAttribute("skuNum",skuNum);

        return "success";
    }



    @RequestMapping("cartList")
    @LoginRequire(autoRedirect = false)
    public String cartList(HttpServletRequest request,HttpServletResponse response){
        String userId = (String) request.getAttribute("userId");

        List<CartInfo> cartInfoList = new ArrayList<>();
        if (userId!=null){
            // 合并购物车：
            List<CartInfo> cartListCK = cartCookieHandler.getCartList(request);
            if (cartListCK!=null && cartListCK.size()>0){
                // 合并购物车
                cartInfoList = cartService.mergeToCartList(cartListCK,userId);
                // 删除未登录购物车
                cartCookieHandler.deleteCartCookie(request,response);
            }else {
                // 查询登录之后的购物车列表
                cartInfoList = cartService.getCartList(userId);
            }
        }else {
            // 查询未登录之后的购物车列表
            cartInfoList = cartCookieHandler.getCartList(request);
        }
        // 保存购物车集合
        request.setAttribute("cartInfoList",cartInfoList);

        return "cartList";
    }


    @RequestMapping("checkCart")
    @LoginRequire(autoRedirect = false)
    @ResponseBody
    public void checkCart(HttpServletRequest request,HttpServletResponse response){

        String isChecked = request.getParameter("isChecked");
        String skuId = request.getParameter("skuId");
        // 获取userId
        String userId = (String) request.getAttribute("userId");

        if (userId!=null){
            // 登录
            cartService.checkCart(skuId,isChecked,userId);
        }else{
            // 未登录
            cartCookieHandler.checkCart(request,response,skuId,isChecked);
        }
    }

    /**
     * 点击去结算，重定向到订单页面
     */
    @RequestMapping("toTrade")
    @LoginRequire(autoRedirect = true)
    public String toTrade(HttpServletRequest request,HttpServletResponse response){
        String userId = (String) request.getAttribute("userId");
        // 缺少一个被选中状态之后的合并！
        // 获取未登录购物车数据
        List<CartInfo> cartListCK = cartCookieHandler.getCartList(request);
        if (cartListCK!=null && cartListCK.size()>0){
            // 开始合并
            cartService.mergeToCartList(cartListCK,userId);
            // 删除cookie 购物车
            cartCookieHandler.deleteCartCookie(request,response);
        }
        // 重定向到订单
        return "redirect://order.gmall.com/trade";
    }

}
