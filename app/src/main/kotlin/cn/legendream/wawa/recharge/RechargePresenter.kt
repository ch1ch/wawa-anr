package cn.legendream.wawa.recharge

import cn.legendream.wawa.app.net.NetService
import cn.legendream.wawa.app.net.NetServiceCode
import cn.legendream.wawa.app.user.UserManager
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/29
 * E-Mail: zhaoxiyuan@kokozu.net
 */

class RechargePresenter @Inject constructor(private val netService: NetService,
                                            private val rechargeView: RechargeContract.View) :
    RechargeContract.Presenter {

    private val user by lazy {
        UserManager.getUser()
    }

    override fun packageList() {
        netService.rechargePackageList().compose(NetService.ioToMain()).subscribe({ response ->
            kotlin.run {
                if (response.code != NetServiceCode.NORMAL.code) {
                    rechargeView.fetchPackageListFailure(response.error.toString())
                } else if (response.data != null) {
                    rechargeView.showPackageList(response.data!!)
                }
            }
        }, {
            rechargeView.fetchPackageListFailure(it.message.toString())
        })
    }

    override fun fetchPayInfo(packageId: String) {
        netService.rechargePayInfo(user?.token ?: "-1", packageId, 2, getOutTradeNo()).compose(
            NetService.ioToMain()).subscribe({ response ->
            kotlin.run {
                if (response.code != NetServiceCode.NORMAL.code) {
                    rechargeView.fetchPayInfoFailure(response.error.toString())
                } else if (response.data != null) {
                    rechargeView.startPay(response.data!!)
                }
            }
        }, {
            rechargeView.fetchPayInfoFailure(it.message.toString())
        })
    }

    /**
     * 要求外部订单号必须唯一。
     * @return
     */
    private fun getOutTradeNo(): String {
        val format = SimpleDateFormat("MMddHHmmss", Locale.getDefault())
        val date = Date()
        var key = format.format(date)

        val r = Random()
        key += r.nextInt()
        key = key.substring(0, 15)
        return key
    }
}