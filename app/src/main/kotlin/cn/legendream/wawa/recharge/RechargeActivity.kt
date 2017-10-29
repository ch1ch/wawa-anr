package cn.legendream.wawa.recharge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.legendream.wawa.R
import javax.inject.Inject

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/29
 * E-Mail: zhaoxiyuan@kokozu.net
 */


class RechargeActivity:AppCompatActivity() {

    @Inject
    lateinit var rechargePresenter:RechargePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge)
    }
}