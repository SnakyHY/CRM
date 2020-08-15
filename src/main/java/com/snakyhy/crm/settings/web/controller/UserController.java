package com.snakyhy.crm.settings.web.controller;

import com.snakyhy.crm.settings.domain.User;
import com.snakyhy.crm.settings.service.UserService;
import com.snakyhy.crm.settings.service.impl.UserServiceImpl;
import com.snakyhy.crm.utils.MD5Util;
import com.snakyhy.crm.utils.PrintJson;
import com.snakyhy.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到用户控制器");
        String path=request.getServletPath();

        if("/settings/user/login.do".equals(path)){
            login(request, response);
        }else if ("/settings/user/xxx.do".equals(path)){

        }
    }

    //验证登陆操作
    private void login(HttpServletRequest request,HttpServletResponse response){
        System.out.println("进入登陆操作");
        String loginAct=request.getParameter("loginAct");
        String loginPwd=request.getParameter("loginPwd");
        //将密码源码转换为MD5的形式
        loginPwd=MD5Util.getMD5(loginPwd);
        //接收ip地址
        String ip=request.getRemoteAddr();

        //未来业务层开发统一使用代理类接口对象
        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());

        try{
            User user=us.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);
            //如果程序执行到此处，表示没有抛出任何异常，登陆成功
            /*String str="{\"success\"=true}";*/
            PrintJson.printJsonFlag(response, true);

        }catch (Exception e){
            e.printStackTrace();
            //业务层验证登陆失败
            /*
            * {"success"=false}
            * msg=错误信息
            */
            String msg=e.getMessage();
            //作为控制器展现多个信息：1.使用Map包装并解析 2.包装成Vo对象
            Map<String,Object> map=new HashMap<>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);
        }
    }
}
