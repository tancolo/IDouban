package com.shrimpcolo.johnnytam.idouban.activity.loginShare;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.activity.home.BaseActivity;
import com.shrimpcolo.johnnytam.idouban.activity.home.HomeActivity;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

public class LoginActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener {

    private ImageView mLoginWeibo;
    private ImageView mLoginWeiChat;
    private ImageView mLoginQQ;

    @Override
    protected void initVariables() {
        //init Share SDK
        ShareSDK.initSDK(this);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);

        mLoginWeibo = (ImageView) findViewById(R.id.login_weibo);
        mLoginWeiChat = (ImageView) findViewById(R.id.login_weichat);
        mLoginQQ = (ImageView) findViewById(R.id.login_qq);

        mLoginWeibo.setOnClickListener(this);
        mLoginWeiChat.setOnClickListener(this);
        mLoginQQ.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_weibo:
                break;
            case R.id.login_weichat:
                break;
            case R.id.login_qq:
                Log.e(HomeActivity.TAG, "LoginActivity---> login qq");
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.SSOSetting(false);
                qq.setPlatformActionListener(this);
                qq.showUser(null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        Log.e(HomeActivity.TAG, "LoginActivity---> onComplete qq");
        if (action == Platform.ACTION_USER_INFOR) {
            PlatformDb platformDb = platform.getDb();
            Log.e(HomeActivity.TAG, "thread id: " + Thread.currentThread().getId() +  ", ===> token: " + platformDb.getToken()
                    + "  UserGender: " + platformDb.getUserGender()
                    + "  UserId: " + platformDb.getUserId()
                    + "  UserName: " + platformDb.getUserName());

            //debug
            final StringBuilder builder = new StringBuilder();
            builder.append("token: ");
            builder.append(platformDb.getToken());
            builder.append("  UserGender: ");
            builder.append(platformDb.getUserGender());
            builder.append("  UserId: ");
            builder.append(platformDb.getUserId());
            builder.append("  UserName: ");
            builder.append(platformDb.getUserName());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, builder.toString(), Toast.LENGTH_LONG).show();
                }
            });
            //end
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Log.e(HomeActivity.TAG, "LoginActivity---> onError: " + throwable.getMessage());
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Log.e(HomeActivity.TAG, "LoginActivity---> onCancel qq");
    }
}
