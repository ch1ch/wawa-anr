package cn.legendream.wawa.recharge

import cn.legendream.wawa.app.net.NetService
import javax.inject.Inject

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/29
 * E-Mail: zhaoxiyuan@kokozu.net
 */

class RechargePresenter @Inject constructor(private val netService: NetService, private val rechargeView: RechargeContract.View): RechargeContract.Presenter {

    override fun packageList() {

    }

    override fun fetchPayInfo() {
        TODO(
            "not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}