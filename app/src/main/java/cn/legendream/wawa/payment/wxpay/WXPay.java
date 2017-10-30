package cn.legendream.wawa.payment.wxpay;

import android.app.Activity;
import android.support.annotation.NonNull;
import cn.legendream.wawa.payment.pay.AbsPayer;
import cn.legendream.wawa.payment.pay.PayManager;
import cn.legendream.wawa.payment.pay.Payment;

/**
 * Author: ZhaoXiyuan
 * Date: 2016/11/26
 * E-Mail: zhaoxiyuan@kokozu.net
 */

public class WXPay implements Payment {
    private Activity mActivity;
    private String   mAppId;
    private WXPayer  mWXPayer;

    public WXPay(Activity activity, @NonNull String appId) {
        super();
        mActivity = activity;
        mAppId = appId;
    }

    @Override
    public int paymentCode() {
        return PayManager.PAYMENT_WX;
    }

    @Override
    public String paymentName() {
        return "WXPay";
    }

    @Override
    public AbsPayer getPayer() {
        if (mWXPayer == null) {
            mWXPayer = new WXPayer(mActivity, mAppId);
        }
        return mWXPayer;
    }
}
