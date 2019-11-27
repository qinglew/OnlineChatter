package cn.edu.ncu.cleo.chatter.service;

import cn.edu.ncu.cleo.chatter.dao.UserDao;
import cn.edu.ncu.cleo.chatter.entity.User;
import cn.edu.ncu.cleo.chatter.util.SqlSessionFactoryUtil;
import cn.edu.ncu.cleo.chatter.util.UserException;
import org.apache.ibatis.session.SqlSession;

/**
 * @author Carlos Leo
 * @author qinglew@outlook.com
 * @description 用户服务
 */
public class UserService {
    /**
     * 注册服务
     * @param user 用户信息
     * @throws UserException 自定义异常，用于记录错误信息
     */
    public void register(User user) throws UserException {
        SqlSession session = null;
        try {
            if (!user.getPhone().matches("^[1][3|5|7|8][0-9]{9}$"))
                throw new UserException("手机号码格式错误!");
            if (user.getUsername().contains(" ") ||
                    (user.getUsername().length() < 1 || user.getUsername().length() > 20))
                throw new UserException("用户名不能包含空格且长度不超过20个字符!");
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
            if (session != null)
                session.rollback();
            throw e;
        } catch (Exception e) {
            if (session != null)
                session.rollback();
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
    }

    /**
     * 登陆服务
     * @param user 用户信息
     * @throws UserException 自定义异常，用于记录错误信息
     */
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
                    user.setDesc(exist.getDesc());
                }
            } else {
                throw new UserException("账户不存在!");
            }
            session.commit();
        } catch (UserException e) {
            session.rollback();
            throw e;
        } catch (Exception e) {
            if (session != null)
                session.rollback();
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
    }

    /**
     * 更新用户信息服务
     * @param user 用户信息
     * @throws UserException 自定义异常，用于记录错误信息
     */
    public void update(User user) throws UserException {
        SqlSession session = null;
        try {
            if (user.getUsername().contains(" ") ||
                    (user.getUsername().length() < 1 || user.getUsername().length() > 20))
                throw new UserException("用户名不能包含空格且长度不超过20个字符!");
            if (!user.getPassword().matches("[0-9a-zA-Z]{8,18}"))
                throw new UserException("密码必须为8-18位字母数字组合!");
            session = SqlSessionFactoryUtil.openSqlSession();
            UserDao userDao = session.getMapper(UserDao.class);
            userDao.updateUser(user);
            session.commit();
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            if (session != null)
                session.rollback();
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
    }
}
