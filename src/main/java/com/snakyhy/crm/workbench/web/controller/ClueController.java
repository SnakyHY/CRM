package com.snakyhy.crm.workbench.web.controller;

import com.snakyhy.crm.settings.domain.User;
import com.snakyhy.crm.settings.service.UserService;
import com.snakyhy.crm.settings.service.impl.UserServiceImpl;
import com.snakyhy.crm.utils.DateTimeUtil;
import com.snakyhy.crm.utils.PrintJson;
import com.snakyhy.crm.utils.ServiceFactory;
import com.snakyhy.crm.utils.UUIDUtil;
import com.snakyhy.crm.vo.PaginationVO;
import com.snakyhy.crm.workbench.domain.Activity;
import com.snakyhy.crm.workbench.domain.ActivityRemark;
import com.snakyhy.crm.workbench.service.ActivityService;
import com.snakyhy.crm.workbench.service.ClueService;
import com.snakyhy.crm.workbench.service.impl.ActivityServiceImpl;
import com.snakyhy.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到线索控制器");
        String path = request.getServletPath();

        if ("/workbench/clue/getUserList.do".equals(path)) {

            getUserList(request,response);

        } else if ("/workbench/clue/xxx.do".equals(path)) {


        }
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("获取用户信息列表");

        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList=us.getUserList();

        PrintJson.printJsonObj(response, uList);
    }
}

