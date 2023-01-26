package com.willy.myapplication.util;

import android.os.Build;
import android.util.Log;

import com.willy.myapplication.constant.Const;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.NoSuchFileException;

public class FileUtil {
    public static String readToString(String path) {
        return readToString(path, Const._DEFAULT_ENCODE);
    }
    public static String readToString(String filePath, String encoding) {
        String strLine;
        StringBuffer resultStr = new StringBuffer();
        FileInputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try{
            if(!new File(filePath).exists()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    throw new FileNotFoundException("filePath["+filePath+"]");
                }
            }
            is = new FileInputStream(filePath);
            isr = new InputStreamReader(is, encoding);
            br = new BufferedReader(isr);
            while ((strLine = br.readLine()) != null) {// 將CSV檔字串一列一列讀入並存起來直到沒有列為止
                resultStr.append(strLine).append("\r\n");
            }
        }catch (Exception e) {
            Log.e(FileUtil.class.getSimpleName(), "read file["+filePath+"] failed", e);
        }finally {
            if (br != null) {
                try {br.close();} catch (IOException e) {}
            }
            if (isr != null) {
                try {isr.close();} catch (IOException e) {}
            }
            if (is != null) {
                try {is.close();} catch (IOException e) {}
            }
        }
        return resultStr.toString();
    }
}
