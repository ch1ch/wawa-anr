package cn.legendream.wawa.recharge

import cn.legendream.wawa.app.model.GameCoinPackage

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/29
 * E-Mail: zhaoxiyuan@kokozu.net
 */

interface RechargeContract {

    interface View {
        fun showPackageList(packageList: List<GameCoinPackage>)
        fun fetchPackageListFailure(error: String)
        fun startPay(payInfo: String)
        fun fetchPayInfoFailure(error: String)
    }

    interface Presenter {
        fun packageList()
        fun fetchPayInfo(packageId: String)
    }
}