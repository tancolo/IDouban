package com.shrimpcolo.johnnytam.idouban.activity.loginShare;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mob.tools.FakeActivity;
import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.activity.home.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.interfaces.OnLoginListener;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class ThirdPartyLogin extends FakeActivity implements View.OnClickListener, Handler.Callback, PlatformActionListener {
    private static final int MSG_AUTH_CANCEL = 2;
    private static final int MSG_AUTH_ERROR = 3;
    private static final int MSG_AUTH_COMPLETE = 4;

    private OnLoginListener signupListener;
    private Handler handler;

    /**
     * 设置授权回调，用于判断是否进入注册
     */
    public void setOnLoginListener(OnLoginListener l) {
        this.signupListener = l;
    }

    public void onCreate() {
        // 初始化ui
        handler = new Handler(this);
        activity.setContentView(R.layout.tpl_login_page);
        (activity.findViewById(R.id.tvWeixin)).setOnClickListener(this);
        (activity.findViewById(R.id.tvWeibo)).setOnClickListener(this);
        (activity.findViewById(R.id.tvQq)).setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvWeixin: {
                //微信登录
                //测试时，需要打包签名；sample测试时，用项目里面的demokey.keystore
                //打包签名apk,然后才能产生微信的登录
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
            }
            break;
            case R.id.tvWeibo: {
                //新浪微博
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(sina);
            }
            break;
            case R.id.tvQq: {
                //QQ
                Platform qzone = ShareSDK.getPlatform(QQ.NAME);
                authorize(qzone);
            }
            break;
            default:
                break;
        }
    }

    //执行授权,获取用户信息
    //文档：http://wiki.mob.com/Android_%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99
    private void authorize(Platform plat) {
        if (plat == null) {
            return;
        }

        plat.setPlatformActionListener(this);
        //关闭SSO授权
        plat.SSOSetting(false);
        plat.showUser(null);
    }

    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = new Object[]{platform.getName(), res};
//            Log.e(HomeActivity.TAG, "====> " + platform.getName());
            handler.sendMessage(msg);
        }
    }

    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_ERROR);
        }
        t.printStackTrace();
    }

    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_CANCEL);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_AUTH_CANCEL: {
                //取消授权
                Toast.makeText(activity, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_ERROR: {
                //授权失败
                Toast.makeText(activity, R.string.auth_error, Toast.LENGTH_SHORT).show();
            }
            break;
                case MSG_AUTH_COMPLETE: {
                //授权成功
                Toast.makeText(activity, R.string.auth_complete, Toast.LENGTH_SHORT).show();
                Object[] objs = (Object[]) msg.obj;
                String platform = (String) objs[0];
                HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
                if (signupListener != null) {
                    //shoud do SignupPage, but don't do this at present!

                    //这里没什么可以操作的，直接关闭登录TPL界面
                    signupListener.onSignin(platform, res);
                    finish();
                }
            }
            break;
        }
        return false;
    }

    public void show(Context context) {
        initSDK(context);
        super.show(context, null);
    }

    private void initSDK(Context context) {
        //初始化sharesdk,具体集成步骤请看文档：
        //http://wiki.mob.com/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
        ShareSDK.initSDK(context);
    }

}
