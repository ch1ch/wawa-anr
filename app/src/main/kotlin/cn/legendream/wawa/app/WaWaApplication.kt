package cn.legendream.wawa.app

import android.app.Application
import cn.legendream.wawa.BuildConfig
import cn.legendream.wawa.app.user.UserManager
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.wilddog.wilddogcore.WilddogApp
import com.wilddog.wilddogcore.WilddogOptions
import timber.log.Timber
import tv.danmaku.ijk.media.player.IjkMediaPlayer


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
            GSYVideoManager.instance().setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG)
        } else {
            GSYVideoManager.instance().setLogLevel(IjkMediaPlayer.IJK_LOG_WARN)
        }

        UserManager.init(this)
        val options = WilddogOptions.Builder().setSyncUrl(
            "http://wd2620361786fgzrcs.wilddogio.com").build()
        WilddogApp.initializeApp(this, options)


    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }
}