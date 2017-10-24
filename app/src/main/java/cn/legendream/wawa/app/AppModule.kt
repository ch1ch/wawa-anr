package cn.legendream.wawa.app

import cn.legendream.wawa.app.net.NetService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/18
 * E-Mail: zhaoxiyuan@kokozu.net
 */


@Module
class AppModule(private val application: WaWaApplication) {

    @Provides
    @Singleton
    fun provideWaWaApplication(): WaWaApplication {
        return application
    }

    @Provides
    @Singleton
    fun provideNetService(): NetService {
        return NetService.INSTANCE
    }

}