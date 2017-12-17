package cn.ahabox.network;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ahabox.interfaces.Api;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.AddressEntity;
import cn.ahabox.model.DesignerDetailEntity;
import cn.ahabox.model.DesignerFocusEntity;
import cn.ahabox.model.GiftCollEntity;
import cn.ahabox.model.OrderEntity;
import cn.ahabox.model.OtherAddressEntity;
import cn.ahabox.model.ShipEntity;
import cn.ahabox.model.ShopcartEntity;
import cn.ahabox.model.SubPageEntity;
import cn.ahabox.model.VoiceEntiy;
import cn.ahabox.model.WaitAcceptEntity;
import cn.ahabox.model.WaitSendEntity;

/**
 * Created by libo on 2015/11/23.
 *
 * 数据解析实现层
 */
public class DataParserImpl implements Api {
    private Context context;
    private CallBackimpl callBack;

    public void setCallBack(Context context, CallBackimpl callBack){
        this.context = context;
        this.callBack = callBack;
    }

    @Override
    public void parseVersion() {
        NetUtil.getJsonObjectRequest(context, Api.VERSIONCODE, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("data");
                    callBack.callBack(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    @Override
    public void parseFirstPage() {
        NetUtil.getTokenStringRequest(context, Api.FIRSTPAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response).getJSONObject("data");
                    callBack.callBack(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },null);
    }

    @Override
    public void parseSubPage(int id,int page) {
        NetUtil.getJsonObjectRequest(context, String.format(Api.SUBPAGE, id,page), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONObject("data").getJSONArray("list");
                    boolean isEnd = response.getJSONObject("data").getBoolean("is_end");
                    List datas = JSON.parseArray(array.toString(), SubPageEntity.class);
                    callBack.callBack(datas,isEnd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },null);

    }

    @Override
    public void parseTopic() {
        NetUtil.getJsonObjectRequest(context, Api.SPECIALTOPIC, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.callBack(response);
            }
        },null);
    }

    @Override
    public void parseBrand() {
        NetUtil.getJsonObjectRequest(context, Api.BRAND, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("data");
                    callBack.callBack(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    @Override
    public void parseDesignerDetail(int id) {
        NetUtil.getTokenStringRequest(context, String.format(Api.DESIGNER_DETAIL, id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response).getJSONObject("data");
                    callBack.callBack(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    @Override
    public void parseDesignerFocus() {
        NetUtil.getTokenStringRequest(context, Api.DESIGNER_FOCUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("data");
                    List list = JSON.parseArray(array.toString(), DesignerFocusEntity.class);
                    callBack.callBack(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },null);
    }

    @Override
    public void parseProductDetail(int id) {
        NetUtil.getTokenStringRequest(context, String.format(Api.PRODUCT_DETAIL, id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response).getJSONObject("data");
                    callBack.callBack(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    @Override
    public void parseSearch(String keyWords,int page) {
        try {
            NetUtil.getTokenStringRequest(context, String.format(Api.SEARCH, URLEncoder.encode(keyWords,"utf-8"),page), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        if (null != response) {
                            JSONObject obj = new JSONObject(response).getJSONObject("data");
                            JSONArray array = obj.getJSONArray("products");
                            boolean isEnd = obj.getBoolean("is_end");
                            List list = JSON.parseArray(array.toString(), DesignerDetailEntity.ProductsEntity.class);
                            callBack.callBack(list,isEnd);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseProductTag() {
        NetUtil.getJsonObjectRequest(context, Api.FASTCHOOSETAG, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("data");
                    callBack.callBack(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    @Override
    public void parseTagProducts(int productTag, int page) {
        NetUtil.getTokenStringRequest(context, String.format(Api.TAGPRODUCTS, productTag, page), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response).getJSONObject("data");
                    JSONArray array = obj.getJSONArray("products");
                    boolean isEnd = obj.getBoolean("is_end");
                    List list = JSON.parseArray(array.toString(), DesignerDetailEntity.ProductsEntity.class);
                    callBack.callBack(list, isEnd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    @Override
    public void parseKeyWords() {
        NetUtil.getJsonObjectRequest(context, Api.KEYWORDS, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.callBack(response);
            }
        }, null);
    }

    @Override
    public void parseCodes(Map<String,String> params) {
        NetUtil.postJsonStringRequest(context, Api.CODES, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        }, null);
    }

    @Override
    public void parseTestCodes(Map<String, String> params) {
        NetUtil.postJsonStringRequest(context, Api.VERIFICATION_CODES, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        }, null);
    }

    @Override
    public void parsePostRegister(HashMap params,HashMap headers) {
        NetUtil.postHeadersStringRequest(context, Api.POST_REGISTER, params, headers, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONObject userObj = obj.getJSONObject("data").getJSONObject("user");
                    callBack.callBack(userObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    @Override
    public void parseModifyPassword(Map<String, String> params,Map<String,String> headers) {
        NetUtil.postHeadersStringRequest(context, Api.MODIFI_PASSWORD, params, headers, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        }, null);
    }

    @Override
    public void parseLogin(Map<String, String> params) {
        NetUtil.postJsonStringRequest(context, Api.LOGIN, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        }, null);
    }

    @Override
    public void parseWxLogin(HashMap<String, String> params) {
        NetUtil.postJsonStringRequest(context, Api.WXLOGIN, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        }, null);
    }

    @Override
    public void parseAddAdd(HashMap<String, String> params) {
        NetUtil.postTokenStringRequest(context, Api.ADD_ADDRESS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        }, null);
    }

    @Override
    public void parseEditAdd(HashMap<String, String> params,int id) {
        NetUtil.postTokenStringRequest(context, String.format(Api.EDIT_ADDRESS, id), params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        }, null);
    }

    @Override
    public void parseMyAddress() {
        NetUtil.getTokenStringRequest(context, Api.MYADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONObject(response).getJSONArray("data");
                    List list = JSON.parseArray(array.toString(), AddressEntity.class);
                    callBack.callBack(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    @Override
    public void parseOnceSend() {
        NetUtil.getTokenStringRequest(context, Api.ONCESEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONObject(response).getJSONArray("data");
                    List list = JSON.parseArray(array.toString(), OtherAddressEntity.class);
                    callBack.callBack(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    @Override
    public void parseDelAddress(int id) {
        NetUtil.postTokenStringRequest(context, String.format(Api.DEL_ADDRESS, id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        }, null);
    }

    @Override
    public void parseCollList() {
        NetUtil.getTokenStringRequest(context, Api.COLLECTION_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONObject(response).getJSONArray("data");
                    List list = JSON.parseArray(array.toString(), GiftCollEntity.class);
                    callBack.callBack(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    @Override
    public void parseUploadToken() {
        NetUtil.getTokenStringRequest(context, Api.UPLOAD_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        },null);
    }

    @Override
    public void parseVoiceList() {
        NetUtil.getTokenStringRequest(context, Api.VOICE_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONObject(response).getJSONArray("data");
                    List list = JSON.parseArray(array.toString(), VoiceEntiy.class);
                    callBack.callBack(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    @Override
    public void parseVoiceDel(int voiceId) {
        NetUtil.postTokenStringRequest(context, String.format(Api.VOICE_DELETE, voiceId), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        }, null);
    }

    @Override
    public void parseVoiceRename(int voiceId,HashMap<String,String> params) {
        NetUtil.postTokenStringRequest(context, String.format(Api.VOICE_RENAME, voiceId), params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        }, null);
    }

    @Override
    public void parsePromoCode(String id, String promoCode) {
        NetUtil.getTokenStringRequest(context, String.format(Api.PROMO_CODE, id, promoCode), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        },null);
    }

    @Override
    public void parsePaySuccess(String code) {
        NetUtil.getTokenStringRequest(context, String.format(Api.PAY_SUCCESS, code), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        }, null);
    }

    @Override
    public void parseFeedBack(HashMap params) {
        NetUtil.postJsonStringRequest(context, Api.FEED_BACK, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        }, null);
    }

    @Override
    public void parseOrders() {
        NetUtil.getTokenStringRequest(context, Api.TRANSAVTION_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONObject(response).getJSONArray("data");
                    List list = JSON.parseArray(array.toString(), OrderEntity.class);
                    callBack.callBack(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },null);
    }

    @Override
    public void parseOrderDetail(String orderCode) {
        NetUtil.getTokenStringRequest(context, String.format(Api.ORDER_DETAIL, orderCode), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response).getJSONObject("data");
                    callBack.callBack(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },null);
    }

    @Override
    public void parseOrderDel(String orderId) {
        NetUtil.postTokenStringRequest(context, String.format(Api.ORDER_DEL, orderId), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        },null);
    }

    @Override
    public void parseShip(String code) {
        NetUtil.getTokenStringRequest(context, String.format(Api.SHIP_INFO, code), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONObject(response).getJSONArray("data");
                    List list = JSON.parseArray(array.toString(), ShipEntity.class);
                    callBack.callBack(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    @Override
    public void parseRefund(String code,HashMap<String, String> params) {
        NetUtil.postTokenStringRequest(context, String.format(Api.REQUEST_REFUND, code), params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        }, null);
    }

    @Override
    public void parseWaitSend() {
        NetUtil.getTokenStringRequest(context, Api.WAIT_SEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONObject(response).getJSONArray("data");
                    List list = JSON.parseArray(array.toString(), WaitSendEntity.class);
                    callBack.callBack(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },null);
    }

    @Override
    public void parseWaitAccept() {
        NetUtil.getTokenStringRequest(context, Api.WAIT_ACCEPT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONObject(response).getJSONArray("data");
                    List list = JSON.parseArray(array.toString(), WaitAcceptEntity.class);
                    callBack.callBack(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    @Override
    public void parseCommentsList(String className, int id, int page) {
        NetUtil.getTokenStringRequest(context, String.format(Api.COMMENTS, className, id, page), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        },null);
    }

    @Override
    public void parseLike(int commentId) {
        NetUtil.postTokenStringRequest(context, String.format(Api.LIKE, commentId), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        },null);
    }

    @Override
    public void parseDislike(int commentId) {
        NetUtil.postTokenStringRequest(context, String.format(Api.DISLIKE, commentId), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        },null);
    }

    @Override
    public void parseCommentDel(int commentId) {
        NetUtil.postTokenStringRequest(context, String.format(Api.COMMENT_DEL, commentId), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        },null);
    }

    @Override
    public void parseShopcart() {
        NetUtil.getTokenStringRequest(context, Api.SHOP_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONObject(response).getJSONArray("data");
                    List list = JSON.parseArray(array.toString(), ShopcartEntity.class);
                    callBack.callBack(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },null);
    }

    @Override
    public void parseShopcartDel(int productId) {
        NetUtil.postTokenStringRequest(context, String.format(Api.SHOPCART_DEL, productId), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        },null);
    }

    @Override
    public void parseShopcartPay(String code) {
        NetUtil.getTokenStringRequest(context, String.format(Api.SHOPCART_PAY, code), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.callBack(response);
            }
        },null);
    }

}
