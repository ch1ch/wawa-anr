package cn.legendream.wawa.app

import android.app.Application
import cn.legendream.wawa.BuildConfig
import cn.legendream.wawa.app.user.UserManager
import com.wilddog.video.base.WilddogVideoInitializer
import com.wilddog.wilddogauth.WilddogAuth
import com.wilddog.wilddogcore.WilddogApp
import com.wilddog.wilddogcore.WilddogOptions
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
        initDillDog()

    }

    private fun initDillDog() {
        val options = WilddogOptions.Builder().setSyncUrl(
                "https://wd2620361786fgzrcs.wilddogio.com").build()
        WilddogApp.initializeApp(this, options)
        WilddogAuth.getInstance().signInAnonymously().addOnCompleteListener { task ->
            kotlin.run {
                if (task.isSuccessful) {
                    val wilddogUser = task.result.wilddogUser
                    val token = wilddogUser.getToken(false).result.token
                    WilddogVideoInitializer.initialize(applicationContext, "wd0062598867lwtpxz", token)
                    Timber.d("Token:$token , ${wilddogUser.uid}")
                } else {
                    Timber.d("wilddog login failure")
                }
            }
        }
    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }
}