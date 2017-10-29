package cn.legendream.wawa.payment.alipay;

import android.app.Activity;
import cn.legendream.wawa.payment.pay.AbsPayer;
import cn.legendream.wawa.payment.pay.PayManager;
import cn.legendream.wawa.payment.pay.Payment;

/**
 * Author: ZhaoXiyuan
 * Date: 2016/11/26
 * E-Mail: zhaoxiyuan@kokozu.net
 */

public class AliPay implements Payment {
    private Activity mActivity;
    private AliPayer mAliPayer;

    public AliPay(Activity activity) {
        super();
        mActivity = activity;
    }

    @Override
    public int paymentCode() {
        return PayManager.PAYMENT_ALI;
    }

    @Override
    public String paymentName() {
        return "AliPay";
    }

    @Override
    public AbsPayer getPayer() {
        if (mAliPayer == null) {
            mAliPayer = new AliPayer(mActivity);
        }
        return mAliPayer;
    }
}
