package com.mwm.loyal;


import com.mwm.loyal.imp.Contact;
import com.mwm.loyal.model.AccountBean;
import com.mwm.loyal.utils.GsonUtil;
import com.mwm.loyal.utils.TimeUtil;

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
        //System.out.println(TimeUtil.getDate(new Date(), Contact.Str.TIME_ALL));
        String xml=
               "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                       "<root>\n" +
                       "    <head>\n" +
                       "        <code>2</code>\n" +
                       "        <message></message>\n" +
                       "    </head>\n" +
                       "    <body>\n" +
                       "        <vehisinfo>\n" +
                       "            <jylsh>Q02718062100041</jylsh>\n" +
                       "            <jycs>1</jycs>\n" +
                       "            <zpyq></zpyq>\n" +
                       "            <jyxmyq></jyxmyq>\n" +
                       "        </vehisinfo>\n" +
                       "    </body>\n" +
                       "</root>";
        System.out.println(xml);
    }
}
