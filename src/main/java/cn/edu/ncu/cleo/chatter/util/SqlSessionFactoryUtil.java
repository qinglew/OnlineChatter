package cn.edu.ncu.cleo.chatter.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Carlos Leo
 * @author qinglew@outlook.com
 * @description MyBatis连接工具
 */
public class SqlSessionFactoryUtil {
    private static final Class<SqlSessionFactoryUtil> LOCK = SqlSessionFactoryUtil.class;
    private static SqlSessionFactory sqlSessionFactory = null;

    private SqlSessionFactoryUtil() {
    }

    /**
     * 加载配置文件获取SqlSessionFactory
     * @return SqlSessionFactory
     */
    private static SqlSessionFactory getSqlSessionFactory() {
        synchronized (LOCK) {
            if (sqlSessionFactory != null) {
                return sqlSessionFactory;
            }
            String resource = "mybatis-config.xml";
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream(resource);
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return sqlSessionFactory;
        }
    }

    /**
     * 获取Mybatis的SqlSession对象，相当于获取数据库连接
     * @return SqlSession
     */
    public static SqlSession openSqlSession() {
        if (sqlSessionFactory == null) {
            sqlSessionFactory = getSqlSessionFactory();
        }
        return sqlSessionFactory.openSession();
    }
}
