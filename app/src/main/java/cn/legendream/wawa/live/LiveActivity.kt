package cn.legendream.wawa.live

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import cn.legendream.wawa.R
import cn.legendream.wawa.app.WaWaApplication
import cn.legendream.wawa.app.contract.ExtraKey
import cn.legendream.wawa.app.extra.toast
import cn.legendream.wawa.app.model.Machine
import cn.legendream.wawa.app.user.UserManager
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.wilddog.client.SyncReference
import com.wilddog.video.base.LocalStream
import com.wilddog.video.call.RemoteStream
import kotlinx.android.synthetic.main.activity_live.*
import kotlinx.android.synthetic.main.empty_control_video.view.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/20
 * E-Mail: zhaoxiyuan@kokozu.net
 */

class LiveActivity : AppCompatActivity(), LiveContract.View {

    @Inject
    lateinit var mLivePresenter: LivePresenter


    private lateinit var machine: Machine


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live)
        init()
    }


    private fun init() {
//        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_16_9)
//        video_view.setUp("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4", true, "demo")
//        video_view.setUp("rtmp://10799.liveplay.myqcloud.com/live/10799_10799_e02471ee11", false, "demo")
//        video_view.setUp("http://10799.liveplay.myqcloud.com/live/10799_784387bddc_900.flv", false, "demo")

        machine = intent.getParcelableExtra(ExtraKey.EXTRA_MACHINE)
        DaggerLiveComponent.builder().appComponent(
            (application as WaWaApplication).getAppComponent()).liveModule(
            LiveModule(this)).build().inject(this)
        video_view.setUp("rtmp://10799.liveplay.myqcloud.com/live/10799_784387bddc", false, "demo")
//        GSYVideoManager.instance().
//        video_view.rotation = 90f
        video_view.surface_container.rotation = 90f
        video_view.startPlayLogic()

        start_game.setOnClickListener {
            Timber.d("start game")
            video_view.onVideoPause()
            video_view.visibility = View.INVISIBLE
            wild_dog_view.visibility = View.VISIBLE
            mLivePresenter.createOrder(machine.id ?: -1, UserManager.getUser()?.token ?: "")
//            mLivePresenter.startGameVideo(machine.video1 ?: "")
        }

    }

    override fun onResume() {
        super.onResume()
        video_view.onVideoResume()
    }

    override fun onPause() {
        super.onPause()
        video_view.onVideoPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        video_view.release()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        video_view.setStandardVideoAllCallBack(null)
        GSYVideoPlayer.releaseAllVideos()
        Handler().postDelayed({
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, 500)
    }

    override fun startGame() {
        Timber.d("startGame: ")
    }

    override fun waitGame() {
        Timber.d("waitGame: ")
    }

    override fun finishWait() {
        mLivePresenter.startGameVideo(machine.video1 ?: "")
    }

    override fun crateOrderError(error: String) {
        toast(error)
    }

    override fun showGameVideo(remoteStream: RemoteStream) {
//        wild_dog_view.visibility = View.VISIBLE
        Timber.d("show game video")
        remoteStream.attach(wild_dog_view)
    }

    override fun showLocalVideo(localStream: LocalStream) {
        Timber.d("show local video")
        localStream.attach(wild_dog_view)
    }

    override fun onStop() {
        super.onStop()
        mLivePresenter.destroy()
    }
}