package com.snakyhy.crm.web.listener;

import com.snakyhy.crm.settings.domain.DicValue;
import com.snakyhy.crm.settings.service.DicService;
import com.snakyhy.crm.settings.service.impl.DicServiceImpl;
import com.snakyhy.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    /*
        用来监听上下文域对象
        当对象创建完毕后，监听到后，立即执行该方法

        event:获得监听的对象
     */

    public void contextInitialized(ServletContextEvent event) {

        System.out.println("上下文域对象创建了");

        ServletContext application=event.getServletContext();

        //取数据字典
        DicService ds= (DicService) ServiceFactory.getService(new DicServiceImpl());

        Map<String, List<DicValue>> map=ds.getAll();
        /*
            map
                {"字典属性名":属性名对应的字典值列表..}
         */
        
        //将map中保存的键值对解析成上下文域对象中的键值对
        Set<String> set= map.keySet();
        for(String key:set){

            //将数据字典放入上下文域对象
            application.setAttribute(key, map.get(key));

        }


        //----------------------------------------------------------------
        //处理Stage2Possibility配置文件
        //处理成java中的键值对文件map

        Map<String,String> pMap=new HashMap<>();
        //解析properties文件
        ResourceBundle rb=ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rb.getKeys();

        while (e.hasMoreElements()){

            //阶段
            String key=e.nextElement();
            //可能性
            String value=rb.getString(key);

            pMap.put(key, value);

        }

        //放入到服务器缓存中
        application.setAttribute("pMap",pMap);


    }


   /* public void contextDestroyed(ServletContextEvent event) {


    }*/
}
