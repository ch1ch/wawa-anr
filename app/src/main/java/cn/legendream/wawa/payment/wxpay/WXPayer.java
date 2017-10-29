package cn.legendream.wawa.payment.wxpay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import cn.legendream.wawa.payment.pay.AbsPayer;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: ZhaoXiyuan
 * Date: 2016/11/26
 * E-Mail: zhaoxiyuan@kokozu.net
 */

public class WXPayer extends AbsPayer {
    private Context mContext;
    private IWXAPI  mAPI;
    private static final String TAG = "WXPayer";
    private boolean mIsDetached;
    private String  mAppId;

    public WXPayer(Context context, @NonNull String appId) {
        super();
        mAppId = appId;
        mContext = context;
        mIsDetached = true;
    }

    @Override
    public void attach() {
        mAPI = WXAPIFactory.createWXAPI(mContext, null);
        mAPI.registerApp(mAppId);
        WXBroadCast.getInstance().registerWXPayer(this);
        mIsDetached = false;
    }

    @Override
    public void pay(String payInfo) {
        JSONObject json = null;
        if (!TextUtils.isEmpty(payInfo)) {
            try {
                json = new JSONObject(payInfo);
                if (!json.has("retcode")) {
                    PayReq req = new PayReq();
                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                    req.appId = json.getString("appid");
                    req.partnerId = json.getString("partnerid");
                    req.prepayId = json.getString("prepayid");
                    req.nonceStr = json.getString("noncestr");
                    req.timeStamp = json.getString("timestamp");
                    req.packageValue = json.getString("package");
                    req.sign = json.getString("sign");
                    //req.extData = "app data"; // optional
                    //Toast.makeText(mActivity, "正常调起支付", Toast.LENGTH_SHORT).show();
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    mAPI.sendReq(req);
                } else {
                    Log.d(TAG, "返回错误" + json.getString("retmsg"));
                    //Toast.makeText(mActivity, "返回错误"+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                    payFailure("返回错误" + json.getString("retmsg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "异常：" + e.getMessage());
                //Toast.makeText(mActivity, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
                payFailure("支付异常");
            }
        } else {
            Log.d(TAG, "服务器请求错误");
            //Toast.makeText(PayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
            payFailure("服务器请求错误");
        }
    }

    @Override
    public void detach() {
        mAPI.unregisterApp();
        WXBroadCast.getInstance().unregisterWXPayer();
        mIsDetached = true;
    }

    @Override
    public boolean isDetached() {
        return mIsDetached;
    }

    static class WXBroadCast extends BroadcastReceiver {
        private static final WXBroadCast sWXBroadCast = new WXBroadCast();
        private WXPayer mWXPayer;

        static WXBroadCast getInstance() {
            return sWXBroadCast;
        }

        private void registerWXPayer(WXPayer wxPayer) {
            mWXPayer = wxPayer;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int errCode = intent.getIntExtra("errCode", -1);
            String errStr = intent.getStringExtra("errStr");

            if (mWXPayer == null) {
                Log.e(TAG, "WxPayer loss");
                context.unregisterReceiver(this);
                return;
            }
            switch (errCode) {
                // 成功
                case 0:
                    mWXPayer.paySuccess(errStr);
                    break;
                //失败
                case -1:
                    mWXPayer.payFailure(errStr);
                    break;
                // 取消
                case -2:
                    mWXPayer.payCancel(errStr);
                    break;
                default:
                    mWXPayer.payFailure(errStr);
                    break;
            }
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
        }

        private void unregisterWXPayer() {
            mWXPayer = null;
        }
    }
}
