package cn.legendream.wawa.live

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import cn.legendream.wawa.R
import cn.legendream.wawa.app.WaWaApplication
import cn.legendream.wawa.app.contract.ExtraKey
import cn.legendream.wawa.app.extra.toast
import cn.legendream.wawa.app.model.Machine
import cn.legendream.wawa.app.user.UserManager
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
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
        machine = intent.getParcelableExtra(ExtraKey.EXTRA_MACHINE)
        DaggerLiveComponent.builder().appComponent(
            (application as WaWaApplication).getAppComponent()).liveModule(
            LiveModule(this)).build().inject(this)
        Timber.d("video3:  ---  rtmp://${machine.video3} ")
        video_view.setUp("rtmp://${machine.video3}", false, "demo")
//        GSYVideoManager.instance().
//        video_view.rotation = 90f
        video_view.surface_container.rotation = 90f
        video_view.startPlayLogic()

        start_game.setOnClickListener {
            Timber.d("start game")
            video_view.release()
            showGameControllerPanel()
            mLivePresenter.createOrder(machine.id ?: -1, UserManager.getUser()?.token ?: "")

            // TODO: 此处为演示切换视频源 请注意检查实际逻辑
            mLivePresenter.startGameVideo(machine.video1 ?: "")
        }

        btn_catch.setOnClickListener {
            Timber.d("start game video")
            if (wild_dog_view.visibility == View.VISIBLE) { //游戏中 切换至 直播
                showLiveControllerPanel()
                mLivePresenter.destroy()
                video_view.startPlayLogic()

            } else { //  直播 切换至 游戏中
                video_view.release()
                showGameControllerPanel()
                mLivePresenter.startGameVideo(machine.video1 ?: "")
            }

        }

    }

    private fun showLiveControllerPanel() {
        video_view.visibility = View.VISIBLE
        wild_dog_view.visibility = View.GONE
        start_game.visibility = View.VISIBLE
        iv_charge.visibility = View.VISIBLE
        lay_game_controller.visibility = View.GONE
        btn_catch.visibility = View.GONE

    }

    private fun showGameControllerPanel() {
        video_view.visibility = View.GONE
        wild_dog_view.visibility = View.VISIBLE
        start_game.visibility = View.GONE
        iv_charge.visibility = View.GONE
        lay_game_controller.visibility = View.VISIBLE
        btn_catch.visibility = View.VISIBLE

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
        toast("排队中...   请稍后.....", Toast.LENGTH_LONG)
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
        toast("show game video")
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