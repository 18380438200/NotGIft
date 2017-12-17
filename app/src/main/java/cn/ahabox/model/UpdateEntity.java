package cn.ahabox.model;

/**
 * Created by libo on 2016/5/10.
 *
 * 版本升级实体类
 */
public class UpdateEntity {

    /**
     * id : 1
     * version :
     * notice : 初始版本
     * url : http://demo.assets.img.feiliwu.com.cn/67d73f506c1df0051fc84ac76fee1594.apk
     * platform : Android
     * created_at : 2016-05-10T10:53:57.000+08:00
     * updated_at : 2016-05-10T10:53:57.000+08:00
     * active : true
     * version_number : 000000000000001
     */
    private int id;
    private String version;
    private String notice;
    private String url;
    private String platform;
    private String created_at;
    private String updated_at;
    private boolean active;
    private String version_number;
    private String notify_type;

    public void setId(int id) {
        this.id = id;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setVersion_number(String version_number) {
        this.version_number = version_number;
    }

    public String getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(String notify_type) {
        this.notify_type = notify_type;
    }

    public int getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getNotice() {
        return notice;
    }

    public String getUrl() {
        return url;
    }

    public String getPlatform() {
        return platform;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public boolean isActive() {
        return active;
    }

    public String getVersion_number() {
        return version_number;
    }
}
