package cn.edu.ncu.cleo.chatter.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @description 用户
 */
public class User implements Serializable {
    private static final long serialVersionUID = 2893470397645764782L;

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

    public User() {
    }

    public User(String username, int image) {
        this.username = username;
        this.image = image;
    }

    public User(String phone, String username, int image, String password) {
        this.phone = phone;
        this.username = username;
        this.image = image;
        this.password = password;
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

    @Override
    public String toString() {
        return "User{" +
                "phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", image=" + image +
                ", password='" + password + '\'' +
                '}';
    }
}
