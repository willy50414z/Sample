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

//        String url = ((EditText) this.activity.findViewById(R.id.urlTxt)).getText().toString();
//        String fileName = ((EditText) this.activity.findViewById(R.id.fileNameTxt)).getText().toString();
//
//        String filePath = Const._APP_DOWNLOAD_FILE_DIR_INDEX_TRACKER + fileName;
//
////        try {
////            HttpRequestUtil.download(url, Const._REQUEST_METHOD_GET, filePath);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        } catch (TimeoutException e) {
////            e.printStackTrace();
////        }
//        HttpRequestUtil.downloadFile(this.ctx, url, fileName);

//        SelendroidCapabilities capa = new SelendroidCapabilities("com.android.browser");
//        capa.setPlatformVersion(DeviceTargetPlatform.ANDROID18);
//
//        // 建立 SelendroidDriver
//        WebDriver driver = new SelendroidDriver(capa);
//
//        // 前往元大銀行網銀登入頁面
//        driver.get("https://ebank.yuantabank.com.tw/");
//
//        // 輸入帳號
//        WebElement usernameInput = driver.findElement(By.id("idn"));
//        usernameInput.sendKeys("YOUR_USERNAME");
//
//        // 輸入密碼
//        WebElement passwordInput = driver.findElement(By.id("pass"));
//        passwordInput.sendKeys("YOUR_PASSWORD");
//
//        // 輸入驗證碼
//        WebElement captchaInput = driver.findElement(By.id("captcha"));
//        String captchaValue = getCaptchaValue(); // 實作這個方法以取得驗證碼
//        captchaInput.sendKeys(captchaValue);
//
//        // 點擊登入按鈕
//        WebElement loginButton = driver.findElement(By.id("login"));
//        loginButton.click();
//
//        // 檢查是否成功登入
//        WebElement successMsg = driver.findElement(By.xpath("//div[@class='login-msg success']"));
//        if (successMsg.isDisplayed()) {
//            System.out.println("登入成功");
//        } else {
//            System.out.println("登入失敗");
//        }
//
//        // 關閉瀏覽器
//        driver.quit();
    }
}
