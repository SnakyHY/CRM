package com.snakyhy.crm.workbench.web.controller;

import com.snakyhy.crm.settings.domain.User;
import com.snakyhy.crm.settings.service.UserService;
import com.snakyhy.crm.settings.service.impl.UserServiceImpl;
import com.snakyhy.crm.utils.*;
import com.snakyhy.crm.vo.PaginationVO;
import com.snakyhy.crm.workbench.domain.Activity;
import com.snakyhy.crm.workbench.service.ActivityService;
import com.snakyhy.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到用户控制器");
        String path=request.getServletPath();

        if("/workbench/activity/getUserList.do".equals(path)){

            getUserList(request,response);

        }else if ("/workbench/activity/save.do".equals(path)){

            save(request,response);

        }else if ("/workbench/activity/pageList.do".equals(path)){

            pageList(request,response);

        }else if ("/workbench/activity/delete.do".equals(path)){

            delete(request,response);

        }else if ("/workbench/activity/getUserListAndActivity.do".equals(path)){

            getUserListAndActivity(request,response);
            
        }else if ("/workbench/activity/update.do".equals(path)){

            update(request,response);
        }

    }

    private void update(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //修改时间
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity act=new Activity();
        act.setId(id);
        act.setCost(cost);
        act.setOwner(owner);
        act.setName(name);
        act.setStartDate(startDate);
        act.setEndDate(endDate);
        act.setDescription(description);
        act.setEditTime(editTime);
        act.setEditBy(editBy);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag=as.update(act);

        PrintJson.printJsonFlag(response, flag);


    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入查询用户信息列表和对应活动id条目修改");

        String id=request.getParameter("id");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Map<String,Object>map=as.getUserListAndActivity(id);

        PrintJson.printJsonObj(response, map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场的删除操作");

        String[] ids=request.getParameterValues("id");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag=as.delete(ids);

        PrintJson.printJsonFlag(response, flag);


    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到获得市场信息查询列表的操作(分页）");

        String name=request.getParameter("name");
        String owner=request.getParameter("owner");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");

        //sql的limit(0,5)第一个数字为略过的数据条数，第二个为每一页的数据行数
        //页数
        String pageNoStr=request.getParameter("pageNo");
        int pageNo=Integer.valueOf(pageNoStr);
        //每页的数据行数
        String pageSizeStr=request.getParameter("pageSize");
        int pageSize=Integer.valueOf(pageSizeStr);

        //计算出略过的记录数
        int skipCount=pageSize*(pageNo-1);

        Map<String,Object> map=new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        //为了实现代码复用，选择从业务层返回vo对象（范型复用）
        PaginationVO<Activity> vo=as.pageList(map);

        PrintJson.printJsonObj(response, vo);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动的添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity act=new Activity();
        act.setId(id);
        act.setCost(cost);
        act.setOwner(owner);
        act.setName(name);
        act.setStartDate(startDate);
        act.setEndDate(endDate);
        act.setDescription(description);
        act.setCreateTime(createTime);
        act.setCreateBy(createBy);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag=as.save(act);

        PrintJson.printJsonFlag(response, flag);


    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得用户信息列表");

        //用户相关的业务
        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response, uList);
    }
}
