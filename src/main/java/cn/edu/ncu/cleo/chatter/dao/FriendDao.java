package cn.edu.ncu.cleo.chatter.dao;

import cn.edu.ncu.cleo.chatter.entity.Friend;

/**
 * @description 好友关系dao层
 */
public interface FriendDao {

    /**
     * 添加好友关系
     * @param friend 好友关系
     * @return
     */
    int addFriend(Friend friend);

    /**
     * 删除好友关系
     * @param friend 好友关系
     * @return
     */
    int deleteFriend(Friend friend);
}
