package cn.ahabox.utils;

import android.os.Handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.ahabox.config.App;
import cn.ahabox.interfaces.CallBackimpl;

public class Request {
    private static ExecutorService executor = Executors.newFixedThreadPool(3);
    private static Handler handler = new Handler();

    public static void postAsync(final String url,
                                 final HashMap<String, Object> params, final CallBackimpl callback) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(url)
                            .openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Authorization", App.user.getToken());
                    conn.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");

                    OutputStream os = conn.getOutputStream();
                    os.write(getParams(params).getBytes());
                    conn.setConnectTimeout(8*1000);
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        byte[] buffer = new byte[1024 * 10];
                        int len = -1;

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        while ((len = is.read(buffer)) != -1) {
                            baos.write(buffer, 0, len);
                        }
                        final byte[] bytes = baos.toByteArray();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    callback.callBack(new String(bytes,
                                            "utf-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static String getParams(HashMap<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        Set<Entry<String, Object>> set = params.entrySet();

        for (Entry<String, Object> entry : set) {
            sb.append(entry.getKey()).append("=").append(entry.getValue())
                    .append("&");
        }
        return sb.substring(0, sb.length() - 1).toString();
    }

}
