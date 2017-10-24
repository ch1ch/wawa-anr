package cn.legendream.wawa.app

import android.app.Application
import cn.legendream.wawa.BuildConfig
import cn.legendream.wawa.app.user.UserManager
import timber.log.Timber
import com.wilddog.client.WilddogSync
import com.wilddog.client.SyncReference
import com.wilddog.wilddogcore.WilddogApp
import com.wilddog.wilddogcore.WilddogOptions


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
        initDillDog()

    }

    private fun initDillDog() {
        val options = WilddogOptions.Builder().setSyncUrl(
            "https://wd2620361786fgzrcs.wilddogio.com").build()
        WilddogApp.initializeApp(this, options)

    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }
}