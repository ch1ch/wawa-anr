package cn.legendream.wawa.recharge

import cn.legendream.wawa.app.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/29
 * E-Mail: zhaoxiyuan@kokozu.net
 */

@Module
class RechargeModule(private val rechargeView: RechargeContract.View) {

    @ActivityScope
    @Provides
    fun provideRechargeView():RechargeContract.View {
        return rechargeView
    }
}