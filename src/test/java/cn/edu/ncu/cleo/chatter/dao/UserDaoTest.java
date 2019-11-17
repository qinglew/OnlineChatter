package cn.edu.ncu.cleo.chatter.dao;

import cn.edu.ncu.cleo.chatter.entity.User;
import cn.edu.ncu.cleo.chatter.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserDaoTest {
    private static Logger logger = Logger.getLogger(UserDaoTest.class);

    @Test
    public void findAll() {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            List<User> users = userDao.findAll();
            for (User user : users) {
                logger.debug(user);
            }
            sqlSession.commit();
            logger.debug("SUCCESS");
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            if (sqlSession != null)
                sqlSession.close();
        }
    }

    @Test
    public void findFriendsByUser() {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            User user = new User("13177737888", "刘清", 1, "cleo0625");
            List<User> users = userDao.findFriendsByUser(user);
            for (User user1 : users) {
                logger.debug(user1);
            }
            sqlSession.commit();
            logger.debug("SUCCESS");
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            if (sqlSession != null)
                sqlSession.close();
        }
    }

    @Test
    public void findUserByUsername() {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            User user = new User("13177737888", "刘清", 1, "cleo0625");
            userDao.findUserByUsername("刘清");
            sqlSession.commit();
            logger.debug("SUCCESS");
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            if (sqlSession != null)
                sqlSession.close();
        }
    }

    @Test
    public void findUserByPhone() {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            User user = new User("13177737888", "刘清", 1, "cleo0625");
            userDao.findUserByPhone("13177737888");
            sqlSession.commit();
            logger.debug("SUCCESS");
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            if (sqlSession != null)
                sqlSession.close();
        }
    }

    @Test
    public void addUser() {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            User user = new User("18579100756", "王雨明", 25, "123456");
            userDao.addUser(user);
            sqlSession.commit();
            logger.debug("SUCCESS");
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            if (sqlSession != null)
                sqlSession.close();
        }
    }

    @Test
    public void deleteUser() {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            User user = new User("18579100756", "王雨明", 25, "123456");
            userDao.deleteUser(user);
            sqlSession.commit();
            logger.debug("SUCCESS");
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            if (sqlSession != null)
                sqlSession.close();
        }
    }

    @Test
    public void updateUser() {SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            User user = new User("13177737888", "刘清", 3, "123456");
            userDao.updateUser(user);
            sqlSession.commit();
            logger.debug("SUCCESS");
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            if (sqlSession != null)
                sqlSession.close();
        }
    }
}