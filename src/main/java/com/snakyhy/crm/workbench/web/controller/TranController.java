package com.snakyhy.crm.workbench.web.controller;

import com.snakyhy.crm.settings.domain.User;
import com.snakyhy.crm.settings.service.UserService;
import com.snakyhy.crm.settings.service.impl.UserServiceImpl;
import com.snakyhy.crm.utils.DateTimeUtil;
import com.snakyhy.crm.utils.PrintJson;
import com.snakyhy.crm.utils.ServiceFactory;
import com.snakyhy.crm.utils.UUIDUtil;
import com.snakyhy.crm.workbench.domain.Activity;
import com.snakyhy.crm.workbench.domain.Clue;
import com.snakyhy.crm.workbench.domain.Customer;
import com.snakyhy.crm.workbench.domain.Tran;
import com.snakyhy.crm.workbench.service.ActivityService;
import com.snakyhy.crm.workbench.service.ClueService;
import com.snakyhy.crm.workbench.service.CustomerService;
import com.snakyhy.crm.workbench.service.impl.ActivityServiceImpl;
import com.snakyhy.crm.workbench.service.impl.ClueServiceImpl;
import com.snakyhy.crm.workbench.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到交易控制器");
        String path = request.getServletPath();

        if ("/workbench/transaction/add.do".equals(path)) {

            add(request,response);


        } else if ("/workbench/transaction/getCustomerName.do".equals(path)) {

            getCustomerName(request,response);

        }
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据客户名称模糊查客户名称列表");

        String name=request.getParameter("name");

        CustomerService cs= (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList=cs.getCustomerName(name);

        PrintJson.printJsonObj(response, sList);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到交易添加页");

        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList=us.getUserList();

        request.setAttribute("uList", uList);

        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
    }


}

