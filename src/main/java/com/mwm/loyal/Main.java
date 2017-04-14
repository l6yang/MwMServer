package com.mwm.loyal;


import com.mwm.loyal.model.AccountBean;
import com.mwm.loyal.utils.StreamUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
       /* String resources = "mybatis-config.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resources);
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);
            SqlSession sqlSession = factory.openSession();
            AccountBean bean = sqlSession.selectOne("getUserByAccount", "1");
            sqlSession.commit();
            System.out.println(bean.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("catch:" + e.toString());
        } finally {
            StreamUtil.close(reader);
        }*/
    }
}
