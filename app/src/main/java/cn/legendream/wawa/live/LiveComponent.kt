package cn.legendream.wawa.live

import cn.legendream.wawa.app.AppComponent
import cn.legendream.wawa.app.scope.ActivityScope
import dagger.Component

/**
 * Created by zhao on 2017/10/24.
 */

@ActivityScope
@Component(modules = arrayOf(LiveModule::class), dependencies = arrayOf(AppComponent::class))
interface LiveComponent {
    fun inject(liveActivity: LiveActivity)
}