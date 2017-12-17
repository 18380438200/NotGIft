package cn.ahabox.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by libo on 2016/3/3.
 *
 * 快速选择标签实体类
 */
public class FastChooseTagEntity implements Serializable {

    /**
     * name : 节日送
     * subtags : [{"tag_id":15,"tag_name":"元旦礼物"},{"tag_id":16,"tag_name":"新年礼物"},{"tag_id":24,"tag_name":"情人节礼物"},
     * {"tag_id":19,"tag_name":"儿童节礼物"},{"tag_id":26,"tag_name":"七夕节礼物"},{"tag_id":20,"tag_name":"建军节礼物"},{"tag_id":14,"tag_name":"圣诞节礼物"}]
     */
    private String name;
    /**
     * tag_id : 15
     * tag_name : 元旦礼物
     */
    private List<SubtagsEntity> subtags;

    public void setName(String name) {
        this.name = name;
    }

    public void setSubtags(List<SubtagsEntity> subtags) {
        this.subtags = subtags;
    }

    public String getName() {
        return name;
    }

    public List<SubtagsEntity> getSubtags() {
        return subtags;
    }

    public static class SubtagsEntity implements Serializable{
        private int tag_id;
        private String tag_name;

        public void setTag_id(int tag_id) {
            this.tag_id = tag_id;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }

        public int getTag_id() {
            return tag_id;
        }

        public String getTag_name() {
            return tag_name;
        }
    }
}
