package cn.ahabox.utils;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ahabox.config.Config;

/**
 * Created by libo on 2015/11/24.
 *
 * 字符串、数字操作工具类
 */
public class StrUtils {

	private static String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
	private static String phonePatter = "^1\\d{10}$";

	/** 获得当前时间 */
	public static CharSequence currentTime(CharSequence inFormat) {
		return DateFormat.format(inFormat, System.currentTimeMillis());
	}

	/**
	 * 根据手机分辨率将dp数值转为px数值
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dpTopx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 操作返回结果提示
	 * @param str
	 * @param successTip
	 * @param context
	 */
	public static void handleStatus(String str,String successTip,Context context){
		try {
			JSONObject obj = new JSONObject(str);
			if(obj.getInt("status") == Config.STATUS_SUCCESSED){
				Toast.makeText(context,successTip,Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(context,"操作失败",Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 时间戳转年月日时分秒时间
	 * @param longTime
	 * @return
	 */
	public static String getDateTime(String longTime){
		long time = Long.valueOf(longTime)*1000;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		return  sdf.format(new Date(time));
	}

	/**
	 * 带有秒的日期时间
	 * @param longTime
	 * @return
	 */
	public static String getDateSec(String longTime){
		long time = Long.valueOf(longTime)*1000;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		return  sdf.format(new Date(time));
	}

	/**
	 * 时间戳转月日时分格式
	 * @param longTime
	 * @return
	 */
	public static String getDateMinite(String longTime){
		long time = Long.valueOf(longTime)*1000;
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
		return  sdf.format(new Date(time));
	}

	/**
	 * 时间戳转日期
	 * @param longTime
	 * @return
	 */
	public static String getTime(String longTime){
		long time = Long.valueOf(longTime)*1000;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return  sdf.format(new Date(time));
	}

	/**
	 * 时间戳转日期
	 * @param longTime
	 * @return
	 */
	public static String getdate(String longTime){
		long time = Long.valueOf(longTime)*1000;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return  sdf.format(new Date(time));
	}

	/**
	 * 日期转时间戳
	 * @return
	 */
	public static String getTimeStamp(String dateTime,String format){
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			return String.valueOf(simpleDateFormat.parse(dateTime).getTime()/1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 从短信中截取验证码
	 * @param patternContent
	 * @return
	 */
	public static String patternCode(String patternContent) {
		if (TextUtils.isEmpty(patternContent)) {
			return null;
		}
		Pattern p = Pattern.compile(patternCoder);
		Matcher matcher = p.matcher(patternContent);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	/**
	 * 检测手机号码
	 * @param patternContent
	 * @return
	 */
	public static boolean checkPhone(String patternContent){
		Pattern pattern = Pattern.compile(phonePatter);
		Matcher matcher =  pattern.matcher(patternContent);
		return matcher.matches();
	}

	/**
	 * 根据数值缩小100倍得到实际的精度
	 * @param num
	 * @return
	 */
	public static String getNum(int num){
		return doubleRound((double) num / 100, "#.##");
	}

	public static String doubleRound(double num,String format){
		DecimalFormat df = new DecimalFormat(format);
		return df.format(num);
	}

	/**
	 * 判断单个字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isStr(String str){
		if(null != str && str.length() != 0) return true;
		return false;
	}

	/**
	 * 判断多个字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isArrStr(String... str){
		if(null == str) return false;
		for(String s : str){
			if(!isStr(s)) return false;
		}
		return true;
	}
	
}
