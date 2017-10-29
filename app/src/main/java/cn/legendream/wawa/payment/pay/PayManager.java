package cn.legendream.wawa.payment.pay;

/**
 * Author: ZhaoXiyuan
 * Date: 2016/11/26
 * E-Mail: zhaoxiyuan@kokozu.net
 */

public class PayManager {
    public static final int PAYMENT_ALI   = 0;
    public static final int PAYMENT_WX    = 1;
    public static final int PAYMENT_UNION = 2;

    public static void pay(Payment payment, String payInfo, OnPayFinishListener listener) {
        PayRouter.getInstance().startPay(payment, payInfo, listener);
    }

    public static void clear() {
        PayRouter.getInstance().clear();
    }
}
