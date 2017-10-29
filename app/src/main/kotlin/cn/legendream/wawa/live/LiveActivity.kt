package cn.legendream.wawa.live

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import cn.legendream.wawa.R
import cn.legendream.wawa.app.AppInfo
import cn.legendream.wawa.app.WaWaApplication
import cn.legendream.wawa.app.contract.ExtraKey
import cn.legendream.wawa.app.extra.toast
import cn.legendream.wawa.app.model.Machine
import cn.legendream.wawa.app.user.UserManager
import cn.legendream.wawa.dolldetail.DollDetailActivity
import cn.legendream.wawa.payment.pay.OnPayFinishListener
import cn.legendream.wawa.payment.pay.PayStatus
import cn.legendream.wawa.payment.wxpay.WXPay
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

class LiveActivity : AppCompatActivity(),
                     LiveContract.View, OnPayFinishListener {

    @Inject
    lateinit var mLivePresenter: LivePresenter

    private lateinit var machine: Machine
    private val wxPay by lazy {
        WXPay(this, AppInfo.WX_APP_ID)
    }

    private val pawDirectionKeyListener by lazy {
        View.OnClickListener { keyView ->
            when (keyView.id) {
                R.id.move_up -> mLivePresenter.movePawTo(LiveContract.PawDirection.UP)
                R.id.move_down -> mLivePresenter.movePawTo(LiveContract.PawDirection.DOWN)
                R.id.move_left -> mLivePresenter.movePawTo(LiveContract.PawDirection.LEFT)
                R.id.move_right -> mLivePresenter.movePawTo(LiveContract.PawDirection.RIGHT)
            }
        }
    }


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
//            video_view.release()
//            showGameControllerPanel()
            mLivePresenter.createOrder(machine.id ?: -1, UserManager.getUser()?.token ?: "")

            // TODO: 此处为演示切换视频源 请注意检查实际逻辑
//            mLivePresenter.startGameVideo(machine.video1 ?: "")
        }

        btn_catch.setOnClickListener {
            Timber.d("start game video")
            if (wild_dog_view.visibility == View.VISIBLE) { //游戏中 切换至 直播

                mLivePresenter.clutch()

            } else { //  直播 切换至 游戏中
                video_view.release()
                showGameControllerPanel()
                mLivePresenter.startGameVideo(machine.video1 ?: "")
            }
        }

        move_up.setOnClickListener(pawDirectionKeyListener)
        move_down.setOnClickListener(pawDirectionKeyListener)
        move_left.setOnClickListener(pawDirectionKeyListener)
        move_right.setOnClickListener(pawDirectionKeyListener)

        btn_doll_detail.setOnClickListener{
            val intent = Intent(this@LiveActivity, DollDetailActivity::class.java)
            intent.putExtra(ExtraKey.EXTRA_DOLL_IMAGE_URL, machine.dollImg)
            startActivity(intent)
        }

        btn_doll_record.setOnClickListener {

        }

    }

    override fun onPayFinish(payStatus: PayStatus?, message: String?) {

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
        video_view.release()
        showGameControllerPanel()
        mLivePresenter.startGameVideo(machine.video1 ?: "")
    }

    override fun waitGame() {
        Timber.d("waitGame: ")
        toast("排队中...   请稍后.....", Toast.LENGTH_LONG)
    }

    override fun finishWait() {
        Timber.d("wait end. startGame: ")
        startGame()
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

    override fun movePawSuccess(direction: LiveContract.PawDirection) {
        Timber.d("$direction Success")
    }

    override fun movePawFailure(direction: LiveContract.PawDirection, error: String) {
        Timber.d("$direction Failure. $error")

    }

    override fun pawCatchSuccess() {
        Timber.d("Paw clutch success. ")
        showLiveControllerPanel()
        mLivePresenter.destroy()
        video_view.startPlayLogic()
    }

    override fun pawCatchFailure(error: String) {
        Timber.d("Paw clutch Failure. $error")

    }

    override fun onStop() {
        super.onStop()
        mLivePresenter.destroy()
    }

}