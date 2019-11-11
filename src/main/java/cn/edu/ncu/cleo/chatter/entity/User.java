package cn.edu.ncu.cleo.chatter.entity;

/**
 * @description 用户
 */
public class User {
    private String username;
    private int image;

    public User() {
    }

    public User(String username, int image) {
        this.username = username;
        this.image = image;
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
