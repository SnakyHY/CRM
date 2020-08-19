package com.snakyhy.crm.web.listener;

import com.snakyhy.crm.settings.domain.DicValue;
import com.snakyhy.crm.settings.service.DicService;
import com.snakyhy.crm.settings.service.impl.DicServiceImpl;
import com.snakyhy.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

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



    }


   /* public void contextDestroyed(ServletContextEvent event) {


    }*/
}
