package cn.edu.ncu.cleo.chatter.dao;

import cn.edu.ncu.cleo.chatter.entity.User;

import java.util.List;

/**
 * @description 用户数据库访问层
 */
public interface UserDao {
    /**
     * 获取所有用户
     * @return 所有用户列表
     */
    List<User> findAll();

    /**
     * 查询某用户的好友
     * @param user 特定用户
     * @return 好友列表
     */
    List<User> findFriendsByUser(User user);

    /**
     * 通过用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    User findUserByUsername(String username);

    /**
     * 通过手机号查询用户
     * @param phone 手机号
     * @return 用户对象
     */
    User findUserByPhone(String phone);

    /**
     * 向数据库中添加用户
     * @param user 特定用户
     */
    void addUser(User user);

    /**
     * 从数据库中删除制定用户
     * @param user 特定用户
     */
    void deleteUser(User user);

    /**
     * 更新用户信息
     * @param user 特定用户
     */
    void updateUser(User user);
}
