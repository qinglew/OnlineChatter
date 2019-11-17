package cn.edu.ncu.cleo.chatter.util;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.*;

public class SqlSessionFactoryUtilTest {

    Logger logger = Logger.getLogger(SqlSessionFactoryUtilTest.class);

    @Test
    public void openSqlSession() {
        SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession();
        sqlSession.close();
        logger.info("success");
    }
}