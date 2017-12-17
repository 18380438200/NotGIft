package cn.ahabox.model;

/**
 * Created by libo on 2016/7/1.
 *
 * 评论实体类
 */
public class CommentEntity {

    /**
     * id : 42
     * object_name : child_category
     * object_id : 1
     * user_id : 52
     * reply_user_id : 1
     * content : 3
     * official_reply_content : ok
     * likes : 0
     * created_at : 1461142588
     * user_name : 朱宇超
     * reply_user_name : desiner
     * avatar_url : http://wx.qlogo.cn/mmopen/ajNVdqHZLLANAnx54oR9ia3EfrHA6KiaI1zK8mQmXJuQemicMy85BWWRPqgn11k6pkwQjib7lnGyLGVUhBmsL7lP0w/0
     */
    private int id;
    private String object_name;
    private int object_id;
    private int user_id;
    private int reply_user_id;
    private String content;
    private String official_reply_content;
    private int likes;
    private int created_at;
    private String user_name;
    private String reply_user_name;
    private String avatar_url;
    private boolean is_like;

    public void setId(int id) {
        this.id = id;
    }

    public void setObject_name(String object_name) {
        this.object_name = object_name;
    }

    public void setObject_id(int object_id) {
        this.object_id = object_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setReply_user_id(int reply_user_id) {
        this.reply_user_id = reply_user_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setOfficial_reply_content(String official_reply_content) {
        this.official_reply_content = official_reply_content;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setReply_user_name(String reply_user_name) {
        this.reply_user_name = reply_user_name;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public boolean is_like() {
        return is_like;
    }

    public void setIs_like(boolean is_like) {
        this.is_like = is_like;
    }

    public int getId() {
        return id;
    }

    public String getObject_name() {
        return object_name;
    }

    public int getObject_id() {
        return object_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getReply_user_id() {
        return reply_user_id;
    }

    public String getContent() {
        return content;
    }

    public String getOfficial_reply_content() {
        return official_reply_content;
    }

    public int getLikes() {
        return likes;
    }

    public int getCreated_at() {
        return created_at;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getReply_user_name() {
        return reply_user_name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }
}
