package com.snakyhy.crm.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionUtil {
    private SqlSessionUtil() {
    }

    private static SqlSessionFactory factory;

    static{
        String resource="mybatis.xml";
        InputStream in=null;
        try {
            in= Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        factory=new SqlSessionFactoryBuilder().build(in);
    }
    private static ThreadLocal<SqlSession> t=new ThreadLocal<>();

    public static SqlSession getSqlSession(){
        SqlSession session=t.get();

        if (session == null) {
            session=factory.openSession();
            t.set(session);
        }
        return session;
    }

    public static void sessionClose(SqlSession session){
        if (session != null) {
            session.close();
            t.remove();
        }
    }
}
