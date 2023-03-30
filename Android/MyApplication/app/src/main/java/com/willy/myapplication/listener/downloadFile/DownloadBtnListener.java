package com.willy.myapplication.listener.downloadFile;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.myapplication.R;
import com.willy.myapplication.constant.Const;
import com.willy.myapplication.util.HttpRequestUtil;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeoutException;

public class DownloadBtnListener  implements View.OnClickListener{
    private Context ctx;
    private AppCompatActivity activity;

    public DownloadBtnListener(Context ctx) {
        this.ctx = ctx;
        this.activity = (AppCompatActivity) ctx;
    }

    @Override
    public void onClick(View view) {

        String url = ((EditText) this.activity.findViewById(R.id.urlTxt)).getText().toString();
        String fileName = ((EditText) this.activity.findViewById(R.id.fileNameTxt)).getText().toString();

        String filePath = Const._APP_DOWNLOAD_FILE_DIR_INDEX_TRACKER + fileName;

        try {
            HttpRequestUtil.download(url, Const._REQUEST_METHOD_GET, filePath);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
