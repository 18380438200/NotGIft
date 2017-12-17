package cn.ahabox.model;

import java.io.Serializable;

/**
 * Created by libo on 2016/4/8.
 *
 * 预支付返回结果实体类
 */
public class PrepayEntity implements Serializable{

    /**
     * appid : wx8cecd3b8eafd586d
     * partnerid : 1243859102
     * timestamp : 1460085745
     * prepayid : null
     * nonceStr : aad6ffeaef5219083d940854e9da14c9
     * sign : F2008D2922508C4FB5E41AB38AE9E91F
     */
    private String appid;
    private String partnerid;
    private String timestamp;
    private String prepayid;
    private String nonceStr;
    private String sign;

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public String getSign() {
        return sign;
    }
}
