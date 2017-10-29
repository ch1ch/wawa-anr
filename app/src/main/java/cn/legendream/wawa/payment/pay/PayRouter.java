package cn.legendream.wawa.payment.pay;

import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: ZhaoXiyuan
 * Date: 2016/11/26
 * E-Mail: zhaoxiyuan@kokozu.net
 */

class PayRouter {

    private static final PayRouter INSTANCE = new PayRouter();
    private AtomicBoolean       isPaying;
    private OnPayFinishListener mOnPayFinishListener;
    private ArraySet<Payment>   mPayments;

    private PayRouter() {
        isPaying = new AtomicBoolean(false);
        mPayments = new ArraySet<>();
    }

    public static PayRouter getInstance() {
        return INSTANCE;
    }

    void startPay(@NonNull Payment payment, String payInfo,
        OnPayFinishListener onPayFinishListener) {

        if (isPaying.compareAndSet(false, true)) {
            AbsPayer payer = payment.getPayer();
            if (payer != null) {
                if (!payer.isDetached()) {
                    payer.attach();
                }
                mPayments.add(payment);
                mOnPayFinishListener = onPayFinishListener;
                payment.getPayer().pay(payInfo);
            }

        }
    }

    void payFinish(PayStatus payStatus, String message) {
        isPaying.set(false);
        if (mOnPayFinishListener != null) {
            mOnPayFinishListener.onPayFinish(payStatus, message);
        }
    }

    void clear() {
        for (Payment payment : mPayments) {
            AbsPayer payer = payment.getPayer();
            if (payer != null) {
                payer.detach();
            }
        }
    }
}
