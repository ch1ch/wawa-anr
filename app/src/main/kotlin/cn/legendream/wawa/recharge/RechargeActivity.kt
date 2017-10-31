package cn.legendream.wawa.recharge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import cn.legendream.wawa.R
import cn.legendream.wawa.app.WaWaApplication
import cn.legendream.wawa.app.extra.toast
import cn.legendream.wawa.app.model.GameCoinPackage
import cn.legendream.wawa.payment.alipay.AliPay
import cn.legendream.wawa.payment.pay.OnPayFinishListener
import cn.legendream.wawa.payment.pay.PayManager
import cn.legendream.wawa.payment.pay.PayStatus
import kotlinx.android.synthetic.main.activity_recharge.*
import javax.inject.Inject

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/29
 * E-Mail: zhaoxiyuan@kokozu.net
 */


class RechargeActivity : AppCompatActivity(),
                         RechargePackagetAdapter.OnClickRechargePackageListener,
                         RechargeContract.View,
                         OnPayFinishListener {

    @Inject
    lateinit var rechargePresenter: RechargePresenter

    private val rechargePackageAdapter by lazy {
        RechargePackagetAdapter().apply {
            setOnClickRechargePackageListener(this@RechargeActivity)
        }
    }

    private val aliPay by lazy {
        AliPay(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge)
        DaggerRechargeComponent.builder().appComponent(
            (application as WaWaApplication).getAppComponent()).rechargeModule(
            RechargeModule((this))).build().inject(this)
        rv_package_list.layoutManager = LinearLayoutManager(application)
        rv_package_list.adapter = rechargePackageAdapter
        rechargePresenter.packageList()
    }

    override fun onClickPackage(gameCoinPackage: GameCoinPackage) {
        rechargePresenter.fetchPayInfo(gameCoinPackage.id)
    }

    override fun showPackageList(packageList: List<GameCoinPackage>) {
        rechargePackageAdapter.setRechargePackageList(packageList)
    }

    override fun fetchPackageListFailure(error: String) {
        toast(error)
    }

    override fun startPay(payInfo: String) {
        PayManager.pay(aliPay, payInfo, this)
    }

    override fun fetchPayInfoFailure(error: String) {
        toast(error)
    }


    override fun onPayFinish(payStatus: PayStatus, message: String?) {
        when (payStatus) {
            PayStatus.SUCCESS -> kotlin.run {
                toast("支付成功")
                finish()
            }
            PayStatus.CANCEL -> toast("取消支付")
            PayStatus.FAILURE -> toast("支付失败")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PayManager.clear()
    }
}