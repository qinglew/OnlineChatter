package cn.edu.ncu.cleo.chatter.dao;

import cn.edu.ncu.cleo.chatter.entity.Friend;
import cn.edu.ncu.cleo.chatter.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.*;

public class FriendDaoTest {
    private static Logger logger = Logger.getLogger(FriendDaoTest.class);

    @Test
    public void addFriend() {
        SqlSession session = SqlSessionFactoryUtil.openSqlSession();
        FriendDao dao = session.getMapper(FriendDao.class);
        dao.addFriend(new Friend("13177737888", "18579100756"));
        session.commit();
        session.close();
    }

    @Test
    public void deleteFriend() {
        SqlSession session = SqlSessionFactoryUtil.openSqlSession();
        FriendDao dao = session.getMapper(FriendDao.class);
        dao.deleteFriend(new Friend("13177737888", "18579100756"));
        session.commit();
        session.close();
    }
}