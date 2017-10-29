package cn.legendream.wawa

import cn.legendream.wawa.app.AppComponent
import cn.legendream.wawa.app.scope.ActivityScope

import dagger.Component

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/18
 * E-Mail: zhaoxiyuan@kokozu.net
 */


@ActivityScope
@Component(modules = arrayOf(MainModule::class), dependencies = arrayOf(AppComponent::class))
interface MainComponent {
    fun inject(mainActivity: MainActivity)
}