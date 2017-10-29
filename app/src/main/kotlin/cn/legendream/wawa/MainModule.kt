package cn.legendream.wawa

import cn.legendream.wawa.app.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/18
 * E-Mail: zhaoxiyuan@kokozu.net
 */

@Module
class MainModule(private val mainView: MainContract.MainView) {

    @ActivityScope
    @Provides
    fun provideMainView(): MainContract.MainView {
        return mainView
    }
}