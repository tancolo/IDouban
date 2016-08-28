package com.shrimpcolo.johnnytam.idouban.activity.loginShare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.activity.home.BaseActivity;
import com.shrimpcolo.johnnytam.idouban.activity.home.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.entity.UserInfo;
import com.shrimpcolo.johnnytam.idouban.interfaces.OnLoginListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

public class LoginActivity extends BaseActivity {

    @Override
    protected void initVariables() {
    }

    private void showThirdPartLogin() {
        ThirdPartyLogin tpl = new ThirdPartyLogin();
        tpl.setOnLoginListener(new OnLoginListener() {
            public void onSignin(String platform, HashMap<String, Object> hashMap) {

                //解析各种平台的数据，统一放到UserInfo中使用
                Log.e(HomeActivity.TAG, "====> " + platform);
                UserInfo userInfo = new UserInfo();

                Iterator ite = hashMap.entrySet().iterator();
                while (ite.hasNext()) {
                    Map.Entry entry = (Map.Entry) ite.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    //Log.e(HomeActivity.TAG, " " + key + "： " + value);
                    if (platform.equals(QQ.NAME)) {
                        if (key.equals("nickname")) {
                            userInfo.setUserName((String) value);

                        } else if (key.equals("figureurl_qq_2")) {
                            userInfo.setUserIcon((String) value);

                        } else if (key.equals("gender")) {
                            UserInfo.Gender gender = value.equals("男") ? UserInfo.Gender.MALE : UserInfo.Gender.FEMALE;
                            userInfo.setUserGender(gender);

                        }
                    } else if (platform.equals(SinaWeibo.NAME)) {
                    } else {//wechat
                    }
                }//end while


                Intent intent = new Intent();
                intent.putExtra("userInfo", userInfo);

                setResult(RESULT_OK, intent);
                finish();
            }

        });
        tpl.show(this);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        Button loginButton = (Button) findViewById(R.id.btn_tplogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showThirdPartLogin();
            }
        });

    }
}
