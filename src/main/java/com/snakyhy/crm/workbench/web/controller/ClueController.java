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
import com.snakyhy.crm.workbench.domain.Clue;
import com.snakyhy.crm.workbench.service.ActivityService;
import com.snakyhy.crm.workbench.service.ClueService;
import com.snakyhy.crm.workbench.service.impl.ActivityServiceImpl;
import com.snakyhy.crm.workbench.service.impl.ClueServiceImpl;
import org.apache.ibatis.javassist.SerialVersionUID;

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

        } else if ("/workbench/clue/save.do".equals(path)) {

            save(request,response);

        }else if ("/workbench/clue/detail.do".equals(path)) {

            detail(request,response);

        }else if ("/workbench/clue/getActivityListByClueId.do".equals(path)) {

            getActivityListByClueId(request,response);

        }else if ("/workbench/clue/unbund.do".equals(path)) {

            unbund(request,response);

        }else if ("/workbench/clue/getActivityByNameAndNotByClueId.do".equals(path)) {

            getActivityByNameAndNotByClueId(request,response);

        }else if ("/workbench/clue/bund.do".equals(path)) {

            bund(request,response);

        }
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入添加关联市场活动方法");

        String cid=request.getParameter("cid");
        String aids[]=request.getParameterValues("aid");

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=cs.bund(cid,aids);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getActivityByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据市场活动名称查询(模糊查询+排除线索关联市场活动列表");

        String aname=request.getParameter("aname");
        String clueId=request.getParameter("clueId");

        //打包成map（都是查询条件）
        Map<String,String>map=new HashMap<>();

        map.put("aname", aname);
        map.put("clueId", clueId);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList=as.getActivityByNameAndNotByClueId(map);

        PrintJson.printJsonObj(response, aList);


    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行解除关联操作");

        String id=request.getParameter("id");

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag=cs.unbund(id);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据线索id查找市场活动列表");

        String clueId=request.getParameter("clueId");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList=as.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(response, aList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到详细信息页");

        String id=request.getParameter("id");

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue c=cs.detail(id);

        request.setAttribute("c", c);

        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);


    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入保存创建线索功能");

       String id=UUIDUtil.getUUID();
       String fullname=request.getParameter("fullname");
       String appellation=request.getParameter("appellation");
       String owner=request.getParameter("owner");
       String company=request.getParameter("company");
       String job=request.getParameter("job");
       String email=request.getParameter("email");
       String phone=request.getParameter("phone");
       String website=request.getParameter("website");
       String mphone=request.getParameter("mphone");
       String state=request.getParameter("state");
       String source=request.getParameter("source");
       String createTime = DateTimeUtil.getSysTime();
       String createBy = ((User)request.getSession().getAttribute("user")).getName();
       String description=request.getParameter("description");
       String contactSummary=request.getParameter("contactSummary");
       String nextContactTime=request.getParameter("nextContactTime");
       String address=request.getParameter("address");

       Clue c=new Clue();

        c.setId(id);
        c.setFullname(fullname);
        c.setAppellation(appellation);
        c.setOwner(owner);
        c.setCompany(company);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setState(state);
        c.setSource(source);
        c.setCreateTime(createTime);
        c.setCreateBy(createBy);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag=cs.save(c);

        PrintJson.printJsonFlag(response, flag);


    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("获取用户信息列表");

        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList=us.getUserList();

        PrintJson.printJsonObj(response, uList);
    }
}

