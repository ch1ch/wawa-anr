package cn.legendream.wawa.app

import android.app.Application
import cn.legendream.wawa.BuildConfig
import cn.legendream.wawa.app.user.UserManager
import timber.log.Timber

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/18
 * E-Mail: zhaoxiyuan@kokozu.net
 */

class WaWaApplication : Application() {
    private lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        UserManager.init(this)
    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }
}