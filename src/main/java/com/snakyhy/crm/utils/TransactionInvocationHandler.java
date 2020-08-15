package com.snakyhy.crm.utils;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TransactionInvocationHandler implements InvocationHandler {
    private Object target;

    public TransactionInvocationHandler(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //普通功能
        SqlSession session=null;
        //执行普通方法的返回值
        Object obj=null;

        try {
            session=SqlSessionUtil.getSqlSession();
            //执行基本方法
            obj=method.invoke(target, args);
            //整合事务功能
            session.commit();
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
            //继续上抛异常
            throw e.getCause();
        }finally {
            SqlSessionUtil.sessionClose(session);
        }
        return obj;
    }

    public Object getProxy(){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                this);
    }
}
