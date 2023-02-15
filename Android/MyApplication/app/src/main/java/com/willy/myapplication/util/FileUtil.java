package com.willy.myapplication.util;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.willy.myapplication.constant.Const;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.NoSuchFileException;

public class FileUtil {
    private static BufferedWriter fw;

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

    public static void writeByStr(Context ctx, String fileDir,  String fileName, String str, boolean isAppend) throws IOException {
        FileOutputStream fos = null;
        OutputStreamWriter os = null;
        try {
            if (!ctx.getFilesDir().exists()) {
                ctx.getFilesDir().mkdir();
            }

            File f = new File(fileDir);
            if (!f.exists()) {
                f.mkdir();
            }
//
//            fos = new FileOutputStream(file, isAppend);
//            os = new OutputStreamWriter(fos, Const._DEFAULT_ENCODE);
//            fw = new BufferedWriter(os); // 指點編碼格式，以免讀取時中文字符異常
//            fw.append(str);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(ctx.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(str);
            outputStreamWriter.close();
        }catch(Exception e) {
            e.printStackTrace();
        } finally {
//            if(os != null) {
//                os.flush();
//                os.close();
//            }
//            if(fos != null) {
//                fos.flush();
//                fos.close();
//            }
//            if(fw!=null) {
//                fw.flush();
//                fw.close();
//            }
        }
    }
}
