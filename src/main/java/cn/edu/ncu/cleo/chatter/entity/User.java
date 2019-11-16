package cn.edu.ncu.cleo.chatter.entity;

import java.util.List;

/**
 * @description 用户
 */
public class User {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户名
     */
    private String username;

    /**
     * 图片id
     */
    private int image;

    /**
     * 密码
     */
    private String password;

    /**
     * 好友列表
     */
    private List<User> friends;

    public User() {
    }

    public User(String username, int image) {
        this.username = username;
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
