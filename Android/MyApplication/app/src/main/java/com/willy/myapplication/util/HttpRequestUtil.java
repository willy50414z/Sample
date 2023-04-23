package com.willy.myapplication.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.willy.myapplication.constant.Const;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    public static void downloadFile(String url, File fileDir, String fileName) {
        if(!fileDir.exists()) {
            fileDir.mkdirs();
        }
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                String fileUrl = strings[0];
                String fileName = strings[1];

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(fileUrl)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        InputStream inputStream = response.body().byteStream();

                        File outputFile = new File(fileDir, fileName);
                        FileOutputStream outputStream = new FileOutputStream(outputFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }

                        outputStream.close();
                        inputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute(url, fileName);
    }

    public static void downloadFile(Context context, String url, String filename) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(filename);
        request.setDescription("Downloading file");

        // 檔案下載目錄為公共下載目錄
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
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
