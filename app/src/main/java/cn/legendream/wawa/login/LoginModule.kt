package cn.legendream.wawa.login

import cn.legendream.wawa.app.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/18
 * E-Mail: zhaoxiyuan@kokozu.net
 */

@Module
class LoginModule(private val loginView: LoginContract.loginView) {

    @ActivityScope
    @Provides
    fun provideMainView(): LoginContract.loginView {
        return loginView
    }
}