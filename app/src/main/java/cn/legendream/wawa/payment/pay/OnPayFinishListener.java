package cn.legendream.wawa.payment.pay;

/**
 * Author: ZhaoXiyuan
 * Date: 2016/11/26
 * E-Mail: zhaoxiyuan@kokozu.net
 */

public interface OnPayFinishListener {
    void onPayFinish(PayStatus payStatus, String message);
}
