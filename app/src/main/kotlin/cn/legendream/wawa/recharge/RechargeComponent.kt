package cn.legendream.wawa.recharge

import cn.legendream.wawa.app.AppComponent
import cn.legendream.wawa.app.scope.ActivityScope
import dagger.Component

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/29
 * E-Mail: zhaoxiyuan@kokozu.net
 */

@ActivityScope
@Component(modules = arrayOf(RechargeModule::class), dependencies = arrayOf(AppComponent::class))
interface RechargeComponent {
    fun inject(rechargeActivity: RechargeActivity)
}