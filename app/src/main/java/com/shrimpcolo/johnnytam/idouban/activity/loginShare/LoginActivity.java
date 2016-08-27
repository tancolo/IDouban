package com.shrimpcolo.johnnytam.idouban.activity.loginShare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.activity.home.BaseActivity;
import com.shrimpcolo.johnnytam.idouban.activity.home.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.entity.QQEntity;
import com.shrimpcolo.johnnytam.idouban.entity.UserInfo;
import com.shrimpcolo.johnnytam.idouban.interfaces.OnLoginListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

public class LoginActivity extends BaseActivity implements PlatformActionListener {

//    private Callbacks callbacks;
//
//    public interface Callbacks {
//        void onSetupProfile(String imageUrl, String name);
//    }

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
                    } else if(platform.equals(SinaWeibo.NAME)) {
                    }else {//wechat
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


    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        Log.e(HomeActivity.TAG, "LoginActivity---> onComplete qq, action = " + action);
        //debug
        //遍历Map
        QQEntity qqEntity = new QQEntity();

//        Iterator ite = hashMap.entrySet().iterator();
//        while (ite.hasNext()) {
//            Map.Entry entry = (Map.Entry) ite.next();
//            Object key = entry.getKey();
//            Object value = entry.getValue();
//            Log.e(HomeActivity.TAG, " " + key + "： " + value);
//            if (key.equals("nickname")) {
//                qqEntity.setNickname((String) value);
//                //Log.e(HomeActivity.TAG, "nicename = " + nicename);
//            } else if (key.equals("figureurl_qq_2")) {
//                qqEntity.setFigureurl_qq_2((String) value);
//                //Log.e(HomeActivity.TAG, "url = " + url);
//            } else if(key.equals("gender")) {
//                qqEntity.setGender((String)value);
//            }
//        }
        //send to HomeActivity
//        Intent intent = new Intent();
////        intent.putExtra("url", qqEntity.getFigureurl_qq_2());
////        intent.putExtra("name", qqEntity.getNickname());
//        intent.putExtra("qqentity", qqEntity);
//        setResult(RESULT_OK, intent);
//        finish();

        //end

        if (action == Platform.ACTION_USER_INFOR) {
            PlatformDb platformDb = platform.getDb();
            Log.e(HomeActivity.TAG, "thread id: " + Thread.currentThread().getId() + ", ===> token: " + platformDb.getToken()
                            + "  UserGender: " + platformDb.getUserGender()
                            + "  UserId: " + platformDb.getUserId()
                            + "  UserName: " + platformDb.getUserName()
                            + "  UserIcon: " + platformDb.getUserIcon()
                            + "   ExpiresIn: " + platformDb.getExpiresIn()
            );

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
