package cn.edu.ncu.cleo.chatter.service;

import cn.edu.ncu.cleo.chatter.dao.UserDao;
import cn.edu.ncu.cleo.chatter.entity.User;
import cn.edu.ncu.cleo.chatter.util.SqlSessionFactoryUtil;
import cn.edu.ncu.cleo.chatter.util.UserException;
import org.apache.ibatis.session.SqlSession;

/**
 * @description 用户服务
 */
public class UserService {
    public void register(User user) throws UserException {
        SqlSession session = null;
        try {
            if (!user.getPhone().matches("^[1][3|5|7|8][0-9]{9}$"))
                throw new UserException("手机号码格式错误!");
            if (!user.getUsername().matches("^\\w[\\w\\d]{5,20}"))
                throw new UserException("用户名必须为字母开头，整体由5-20位字母数字组合而成!");
            if (!user.getPassword().matches("[0-9a-zA-Z]{8,18}"))
                throw new UserException("密码必须为8-18位字母数字组合!");
            session = SqlSessionFactoryUtil.openSqlSession();
            UserDao userDao = session.getMapper(UserDao.class);
            User exist = userDao.findUserByPhone(user.getPhone());
            if (exist != null) {
                throw new UserException("该手机号已被用户注册");
            } else {
                userDao.addUser(user);
            }
            session.commit();
        } catch (UserException e) {
            session.rollback();
            throw e;
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void login(User user) throws UserException {
        SqlSession session = null;
        try {
            session = SqlSessionFactoryUtil.openSqlSession();
            UserDao userDao = session.getMapper(UserDao.class);
            User exist = userDao.findUserByPhone(user.getPhone());
            if (exist != null) {
                if (!user.getPassword().equals(exist.getPassword()))
                    throw new UserException("密码错误!");
                else {
                    user.setImage(exist.getImage());
                    user.setUsername(exist.getUsername());
                }
            } else {
                throw new UserException("账户不存在!");
            }
            session.commit();
        } catch (UserException e) {
            session.rollback();
            throw e;
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
