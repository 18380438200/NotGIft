package cn.ahabox.model;

import java.io.Serializable;

/**
 * Created by libo on 2016/3/24.
 */
public class VoiceEntiy implements Serializable{

    /**
     * id : 227
     * name : null
     * created_at : 1459838635
     * voice_url : http://demo.assets.voice.tar.feiliwu.com.cn/a584a93ff82834fba9180cd40c3e54b3.mp3?e=1459860945&token=02oUIUw9AMj7iyQJoYw76A_RmiNWxXrWMhLXKvIE:1Raud7v4XuRkNoltiJ-yH6B9C0o=
     */
    private int id;
    private Object name;
    private int created_at;
    private String voice_url;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public void setVoice_url(String voice_url) {
        this.voice_url = voice_url;
    }

    public int getId() {
        return id;
    }

    public Object getName() {
        return name;
    }

    public int getCreated_at() {
        return created_at;
    }

    public String getVoice_url() {
        return voice_url;
    }
}
