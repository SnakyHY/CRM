package com.snakyhy.crm.workbench.web.controller;

import com.snakyhy.crm.settings.domain.User;
import com.snakyhy.crm.settings.service.UserService;
import com.snakyhy.crm.settings.service.impl.UserServiceImpl;
import com.snakyhy.crm.utils.DateTimeUtil;
import com.snakyhy.crm.utils.PrintJson;
import com.snakyhy.crm.utils.ServiceFactory;
import com.snakyhy.crm.utils.UUIDUtil;
import com.snakyhy.crm.workbench.domain.*;
import com.snakyhy.crm.workbench.service.ActivityService;
import com.snakyhy.crm.workbench.service.ClueService;
import com.snakyhy.crm.workbench.service.CustomerService;
import com.snakyhy.crm.workbench.service.TranService;
import com.snakyhy.crm.workbench.service.impl.ActivityServiceImpl;
import com.snakyhy.crm.workbench.service.impl.ClueServiceImpl;
import com.snakyhy.crm.workbench.service.impl.CustomerServiceImpl;
import com.snakyhy.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletContext;
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

        }else if ("/workbench/transaction/save.do".equals(path)) {

            save(request,response);

        }else if ("/workbench/transaction/detail.do".equals(path)) {

            detail(request,response);

        }else if ("/workbench/transaction/getHistoryListByTranId.do".equals(path)) {

            getHistoryListByTranId(request,response);

        }else if ("/workbench/transaction/changeStage.do".equals(path)) {

            changeStage(request,response);

        }else if ("/workbench/transaction/getCharts.do".equals(path)) {

            getCharts(request,response);

        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("获取交易阶段数据，制作图表");

        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());

        Map<String,Object> map=ts.getCharts();

        PrintJson.printJsonObj(response, map);

    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行改变阶段的操作");

        String id=request.getParameter("id");
        String stage=request.getParameter("stage");
        String money=request.getParameter("money");
        String expectedDate=request.getParameter("expectedDate");

        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Tran t=new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setEditBy(editBy);
        t.setEditTime(editTime);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);

        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag=ts.changeStage(t);

        //处理可能性
        Map<String,String> pMap= (Map<String, String>) this.getServletContext().getAttribute("pMap");

        String possibility=pMap.get(stage);
        t.setPossibility(possibility);

        Map<String,Object>map=new HashMap<>();
        map.put("success", flag);
        map.put("t", t);

        PrintJson.printJsonObj(response, map);
    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("获取交易历史记录列表");

        String tranId=request.getParameter("tranId");

        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<TranHistory> thList=ts.getHistoryListByTranId(tranId);

        Map<String,String> pMap= (Map<String, String>) this.getServletContext().getAttribute("pMap");

        //将交易历史列表遍历，取可能性
        for(TranHistory th:thList){

            String stage=th.getStage();

            String possibility=pMap.get(stage);

            th.setPossibility(possibility);

        }

        PrintJson.printJsonObj(response, thList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到交易详细信息页");

        String id=request.getParameter("id");

        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran t=ts.detail(id);

        /*
            处理可能性
            阶段t 与对应关系pMap
         */
        String stage=t.getStage();
        //获得全局作用域对象
        Map<String,String> pMap= (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possibility=pMap.get(stage);

        t.setPossibility(possibility);

        request.setAttribute("t",t);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("进入到提交交易表单操作");

        Tran t=new Tran();

        String id=UUIDUtil.getUUID();
        String owner=request.getParameter("owner");
        String money=request.getParameter("money");
        String name=request.getParameter("name");
        String expectedDate=request.getParameter("expectedDate");
        String customerName=request.getParameter("customerName");//此处只有名称，没有id
        String stage=request.getParameter("stage");
        String type=request.getParameter("type");
        String source=request.getParameter("source");
        String activityId=request.getParameter("activityId");
        String contactsId=request.getParameter("contactsId");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description=request.getParameter("description");
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");

        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag=ts.save(t,customerName);

        if(flag){

            //交易成功，跳转到列表页
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
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

