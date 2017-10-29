package cn.legendream.wawa.live

import cn.legendream.wawa.app.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Created by zhao on 2017/10/24.
 */


@Module
class LiveModule(private val liveView: LiveContract.View) {

    @ActivityScope
    @Provides
    fun provideLiveView():LiveContract.View {
        return liveView
    }
}