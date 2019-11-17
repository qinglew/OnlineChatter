package cn.edu.ncu.cleo.chatter.entity;

import java.io.Serializable;

/**
 * @description 客户端和服务器传递的消息对象
 */
public class Transportation implements Serializable {
    private static final long serialVersionUID = -1162260040864773760L;

    /**
     * REGISTER_MESSAGE      注册
     * LOGIN_MESSAGE         登陆
     * LOGOUT_MESSAGE        登出
     * CHAT_MESSAGE          聊天
     * ADD_FRIEND_MESSAGE    添加好友
     * DELETE_FRIEND_MESSAGE 删除好友
     * FRIENDS_MESSAGE       好友列表
     */
    private static final int REGISTER_MESSAGE = 0;
    private static final int LOGIN_MESSAGE = 1;
    private static final int LOGOUT_MESSAGE = 2;
    private static final int CHAT_MESSAGE = 3;
    private static final int ADD_FRIEND_MESSAGE = 4;
    private static final int DELETE_FRIEND_MESSAGE = 5;
    private static final int FRIENDS_MESSAGE = 6;

    /**
     * 消息类型
     */
    private int messageType;

    /**
     * 消息实体
     */
    private Object object;

    public Transportation() {
    }

    public Transportation(int messageType, Object object) {
        this.messageType = messageType;
        this.object = object;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "Transportation{" +
                "messageType=" + messageType +
                ", object=" + object +
                '}';
    }
}
