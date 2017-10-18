package cn.legendream.wawa.app

import cn.legendream.wawa.app.net.NetService
import dagger.Component
import javax.inject.Singleton

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/18
 * E-Mail: zhaoxiyuan@kokozu.net
 */

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(application: WaWaApplication)

    fun provideWaWaApplication(): WaWaApplication
    fun provideNetService(): NetService
}