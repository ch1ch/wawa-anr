package cn.legendream.wawa.payment.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import cn.legendream.wawa.payment.pay.AbsPayer;
import com.alipay.sdk.app.PayTask;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Author: ZhaoXiyuan
 * Date: 2016/11/26
 * E-Mail: zhaoxiyuan@kokozu.net
 */

public class AliPayer extends AbsPayer {
    private static final int SDK_PAY_FLAG = 1;
    private Activity mActivity;
    private boolean  mIsDetached;

    public AliPayer(Activity activity) {
        super();
        mActivity = activity;
        mIsDetached = true;
    }

    @Override
    public void attach() {
        mIsDetached = false;
    }

    @Override
    public void detach() {
        mIsDetached = true;
    }

    @Override
    public boolean isDetached() {
        return mIsDetached;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
                        paySuccess("支付成功");
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            //Toast.makeText(mActivity, "支付结果确认中", Toast.LENGTH_SHORT).show();
                            payFailure("支付结果确认中");
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            //Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
                            payCancel("取消支付");
                        } else {
                            payFailure("支付失败" + resultStatus);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void pay(final String payInfo) {
        final String sign;
        try {
            sign = new String(Base64.decode(payInfo, Base64.DEFAULT), "GBK");
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    PayTask payTask = new PayTask(mActivity);
                    Map<String, String> result = payTask.payV2(sign, true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        } catch (UnsupportedEncodingException e) {
            payFailure("支付中心数据错误");
        }

    }
}
