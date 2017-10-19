package cn.legendream.wawa.login

import cn.legendream.wawa.app.AppComponent
import cn.legendream.wawa.app.scope.ActivityScope
import dagger.Component

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/18
 * E-Mail: zhaoxiyuan@kokozu.net
 */


@ActivityScope
@Component(modules = arrayOf(LoginModule::class), dependencies = arrayOf(AppComponent::class))
interface LoginComponent {
    fun inject(loginActivity: LoginActivity)
}