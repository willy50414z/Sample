package com.willy.myapplication.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class StreamUtil {
    public static byte[] read(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = inStream.read(buffer)) != -1)
        {
            outStream.write(buffer,0,len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    public static void download(InputStream inStream, String filePath) throws Exception{
        FileOutputStream fos = null;
        BufferedInputStream is = null;
        File file = new File(filePath);
        if((file.isDirectory() && !file.exists()) || (file.isFile() && !file.getParentFile().exists())) {
            file.mkdirs();
        }
        try{
            is = new BufferedInputStream(inStream);
            fos = new FileOutputStream(file);
            byte[] b = new byte[8192];
            int l = 0;
            while ((l = is.read(b)) != -1) {
                fos.write(b, 0, l);
            }
        }catch (Exception e) {
            Log.e(StreamUtil.class.getSimpleName(), "download file failed", e);
        } finally {
            fos.close();
            is.close();
        }
    }
}
