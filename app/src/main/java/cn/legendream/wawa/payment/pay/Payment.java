package cn.legendream.wawa.payment.pay;

/**
 * Author: ZhaoXiyuan
 * Date: 2016/11/26
 * E-Mail: zhaoxiyuan@kokozu.net
 */

public interface Payment {

    /**
     * 支付方式编码 确保唯一性
     */
    int paymentCode();

    /**
     * 支付方式
     */
    String paymentName();

    /**
     * 获取支付Task
     *
     * @return 支付Task
     */
    AbsPayer getPayer();

}
