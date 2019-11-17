package cn.edu.ncu.cleo.chatter.entity;

/**
 * @description 好友关系
 */
public class Friend {
    private String phone;
    private String friendPhone;

    public Friend() {
    }

    public Friend(String phone, String friendPhone) {
        this.phone = phone;
        this.friendPhone = friendPhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFriendPhone() {
        return friendPhone;
    }

    public void setFriendPhone(String friendPhone) {
        this.friendPhone = friendPhone;
    }
}
