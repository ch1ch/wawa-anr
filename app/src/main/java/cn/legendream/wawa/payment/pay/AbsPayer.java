package cn.legendream.wawa.payment.pay;

import android.support.annotation.CallSuper;

/**
 * Author: ZhaoXiyuan
 * Date: 2016/11/26
 * E-Mail: zhaoxiyuan@kokozu.net
 */

public abstract class AbsPayer {

    public AbsPayer() {
        super();

    }

    public abstract void attach();

    public abstract void pay(String payInfo);

    @CallSuper
    protected void paySuccess(String message) {
        PayRouter.getInstance().payFinish(PayStatus.SUCCESS, message);
    }

    @CallSuper
    protected void payCancel(String message) {
        PayRouter.getInstance().payFinish(PayStatus.CANCEL, message);
    }

    @CallSuper
    protected void payFailure(String error) {
        PayRouter.getInstance().payFinish(PayStatus.FAILURE, error);
    }

    public abstract void detach();

    public abstract boolean isDetached();

}
