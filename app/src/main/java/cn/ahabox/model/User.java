package cn.ahabox.model;

import java.io.Serializable;

/**
 * Created by libo on 2016/3/11.
 *
 * 用户登录成功返回对象
 */
public class User implements Serializable{

    /**
     * id : 51
     * nickname : 李博
     * avatar_url :
     * token :
     */
    private int id;
    private String nickname;
    private String avatar_url;
    private String token;

    public void setId(int id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getToken() {
        return token;
    }
}
