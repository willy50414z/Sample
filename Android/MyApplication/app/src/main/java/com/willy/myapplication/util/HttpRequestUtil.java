package com.willy.myapplication.util;

import android.util.Log;

import com.willy.myapplication.constant.Const;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;

public class HttpRequestUtil {
    private static HttpURLConnection con;

    public static String request(String apiUrl) throws ExecutionException, InterruptedException, TimeoutException {
        return request(apiUrl, Const._DEFAULT_REQUEST_METHOD);
    }

    public static String request(String apiUrl, String reqMethod) throws ExecutionException, InterruptedException, TimeoutException {
        FutureTask<String> ft = new FutureTask<>(() -> {
            try {
                URL url = new URL(apiUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod(reqMethod);
                con.setConnectTimeout(5000);
                if (con.getResponseCode() == 200) {
                    InputStream in = con.getInputStream();
                    byte[] data = StreamUtil.read(in);
                    return new String(data, Const._DEFAULT_ENCODE);
                } else {
                    throw new IllegalStateException("response status[" + con.getResponseCode() + "]message[" + con.getResponseMessage() + "]");
                }
            } catch (Exception e) {
                Log.e(HttpRequestUtil.class.getSimpleName(), "http request fail", e);
                throw e;
            }
        });
        try {
            new Thread(ft).start();
            int waitTimes = 0;
            int maxWaitTimes = 10;
            while (!ft.isDone()) {
                Thread.sleep(3000);
                if (++waitTimes == maxWaitTimes) {
                    throw new TimeoutException("fetch data timeout, url[" + apiUrl + "]");
                }
            }
            return ft.get();
        } catch (Exception e) {
            Log.e(HttpRequestUtil.class.getSimpleName(), "http request fail", e);
            throw e;
        }
    }

    public static void download(String apiUrl, String reqMethod, String filePath) throws InterruptedException, TimeoutException {
        FutureTask<Integer> ft = new FutureTask<>(() -> {
            try {
                URL url = new URL(apiUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod(reqMethod);
                con.setConnectTimeout(5000);
                if (con.getResponseCode() == 200) {
                    Log.d(HttpRequestUtil.class.getSimpleName(), "download file["+filePath+"] from ["+reqMethod+"]["+apiUrl+"] success!");
                    StreamUtil.download(con.getInputStream(), filePath);
                    return 1;
                } else {
                    throw new IllegalStateException("response status[" + con.getResponseCode() + "]message[" + con.getResponseMessage() + "]");
                }
            } catch (Exception e) {
                Log.e(HttpRequestUtil.class.getSimpleName(), "http request fail", e);
                throw e;
            }
        });
        try {
            new Thread(ft).start();
            int waitTimes = 0;
            int maxWaitTimes = 10;
            while (!ft.isDone()) {
                Thread.sleep(3000);
                if (++waitTimes == maxWaitTimes) {
                    throw new TimeoutException("fetch data timeout, url[" + apiUrl + "]");
                }
            }
        } catch (Exception e) {
            Log.e(HttpRequestUtil.class.getSimpleName(), "http request fail", e);
            throw e;
        }
    }
}
