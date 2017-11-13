package cn.legendream.wawa.live

import android.Manifest
import android.content.Intent
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
import cn.legendream.wawa.app.model.User
import cn.legendream.wawa.app.user.UserManager
import cn.legendream.wawa.dolldetail.DollDetailActivity
import cn.legendream.wawa.recharge.RechargeActivity
import com.afollestad.materialdialogs.MaterialDialog
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
                     LiveContract.View {

    @Inject
    lateinit var mLivePresenter: LivePresenter

    private lateinit var machine: Machine
    private var orderId: String? = null
    private val user by lazy {
        UserManager.getUser()
    }
//    private val rxPermission by lazy {
//        RxPermissions(this)
//    }


    private val noticeDialog by lazy {
        MaterialDialog.Builder(this).content("等待开始").progress(false, 5, true).positiveText(
            "开始游戏").onPositive({ _, _ ->
            mLivePresenter.createOrder(machine.id ?: -1, UserManager.getUser()?.token ?: "")
        }).cancelable(false).build()
    }

    private val progressDialg by lazy {
        MaterialDialog.Builder(this).content("加载中").progress(true, 0).cancelable(false).build()
    }

    private val pawDirectionKeyListener by lazy {
        View.OnClickListener { keyView ->
            when (keyView.id) {
                R.id.move_up -> mLivePresenter.movePawTo(machine, LiveContract.PawDirection.UP)
                R.id.move_down -> mLivePresenter.movePawTo(machine, LiveContract.PawDirection.DOWN)
                R.id.move_left -> mLivePresenter.movePawTo(machine, LiveContract.PawDirection.LEFT)
                R.id.move_right -> mLivePresenter.movePawTo(machine,
                    LiveContract.PawDirection.RIGHT)
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
        video_view.setUp("rtmp://${machine.video3}", false, "wawa")
//        GSYVideoManager.instance().
//        video_view.rotation = 90f
        video_view.surface_container.rotation = 90f
        video_view.startPlayLogic()


        start_game.setOnClickListener {
            Timber.d("start game")
//            rxPermission.request(Manifest.permission.CAMERA).subscribe({ granted ->
//                kotlin.run {
//                    if (granted) {
//                        mLivePresenter.createOrder(machine.id ?: -1,
//                            UserManager.getUser()?.token ?: "")
//                    } else {
//                        toast("缺少必要的权限。。。")
//                    }
//                }
//            })

//            finishWait(2)
            mLivePresenter.createOrder(machine.id ?: -1,
                UserManager.getUser()?.token ?: "")

            // TODO: 此处为演示切换视频源 请注意检查实际逻辑
//            video_view.release()
//            showGameControllerPanel()
//            mLivePresenter.startGameVideo(machine.video1 ?: "")
        }

        btn_catch.setOnClickListener {
            Timber.d("start game video")
            if (wild_dog_view.visibility == View.VISIBLE) { //游戏中 切换至 直播

                mLivePresenter.catch(machine)

            } else { //  直播 切换至 游戏中
                video_view.release()
                showGameControllerPanel()
                mLivePresenter.startGameVideo(machine.video1 ?: "", machine.video2 ?: "")
            }
        }

        move_up.setOnClickListener(pawDirectionKeyListener)
        move_down.setOnClickListener(pawDirectionKeyListener)
        move_left.setOnClickListener(pawDirectionKeyListener)
        move_right.setOnClickListener(pawDirectionKeyListener)

        btn_doll_detail.setOnClickListener {
            val intent = Intent(this@LiveActivity, DollDetailActivity::class.java)
            intent.putExtra(ExtraKey.EXTRA_DOLL_IMAGE_URL, machine.dollImg)
            startActivity(intent)
        }

        iv_charge.setOnClickListener {
            val intent = Intent(this@LiveActivity, RechargeActivity::class.java)
            startActivity(intent)
        }

        btn_switch_video.setOnClickListener {
            mLivePresenter.switchGameVideo()
        }

        tv_coin_balance.text = user?.gameMoney.toString()
    }


    private fun showLiveControllerPanel() {
        video_view.visibility = View.VISIBLE
        wild_dog_view.visibility = View.GONE
        start_game.visibility = View.VISIBLE
        iv_charge.visibility = View.VISIBLE
        tv_coin_balance.visibility = View.VISIBLE
        lay_game_controller.visibility = View.GONE
        btn_catch.visibility = View.GONE
        tv_game_down_time.visibility = View.GONE
        btn_switch_video.visibility = View.GONE


    }

    private fun showGameControllerPanel() {
        video_view.visibility = View.GONE
        wild_dog_view.visibility = View.VISIBLE
        start_game.visibility = View.GONE
        iv_charge.visibility = View.GONE
        lay_game_controller.visibility = View.VISIBLE
        btn_catch.visibility = View.VISIBLE
        tv_coin_balance.visibility = View.GONE
        tv_game_down_time.visibility = View.VISIBLE
        btn_switch_video.visibility = View.VISIBLE
    }

    override fun updateUserInfo(user: User) {
        tv_coin_balance.text = user.gameMoney.toString()
    }


    override fun updateUserInfoFailure(error: String) {
        toast(error)
    }

    override fun onResume() {
        super.onResume()
        mLivePresenter.refreshUserInfo()
        video_view.onVideoResume()
    }

    override fun onPause() {
        super.onPause()
        video_view.onVideoPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        video_view.release()
        mLivePresenter.destroy()
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

    override fun startGame(orderId: String) {
        this.orderId = orderId
        Timber.d("startGame: ")
        video_view.release()
        showGameControllerPanel()
        mLivePresenter.startGameVideo(machine.video1 ?: "", machine.video2 ?: "")
    }

    override fun updateGameTime(time: Long) {
        tv_game_down_time.text = time.toString()
    }

    override fun gameTimeIsOver() {
        mLivePresenter.catch(machine)
    }

    override fun waitGame() {
        Timber.d("waitGame: ")
        toast("排队中...   请稍后.....", Toast.LENGTH_LONG)
    }

    override fun finishWait(waitTime: Int) {
        Timber.d("wait end. startGame: $waitTime")

        if (!isFinishing && waitTime > 5) {
            noticeDialog.dismiss()
            return
        }

        if (!isFinishing && !noticeDialog.isShowing) {
            noticeDialog.setContent("等待开始游戏")
            noticeDialog.setProgress(waitTime)
            noticeDialog.show()
            return
        }

        if (!isFinishing && noticeDialog.isShowing) {
            noticeDialog.setProgress(waitTime)
        }
    }

    override fun crateOrderError(error: String) {
        toast(error)
    }

    override fun showGameVideo(remoteStream: RemoteStream) {
//        wild_dog_view.visibility = View.VISIBLE
        Timber.d("show game video")
        toast("show game video")
        remoteStream.attach(wild_dog_view)
        btn_switch_video.visibility = View.VISIBLE
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

    override fun pawDownSuccess() {
        toast("请稍等... 即将为你查询结果...")
    }

    override fun pawDownFailure(error: String) {
        toast(error)
    }

    override fun pawCatchFinish() {
        Timber.d("Paw catch success. ")
        showLiveControllerPanel()
        mLivePresenter.wildDogDestroy()
        video_view.startPlayLogic()
        val currentOrder = orderId
        if (currentOrder != null) {
            mLivePresenter.checkGameResult(currentOrder)
        }
    }

    override fun pawCatchFailure(error: String) {
        Timber.d("Paw catch Failure. $error")
        toast(error)
        showLiveControllerPanel()
        mLivePresenter.wildDogDestroy()
        video_view.startPlayLogic()
    }

    override fun showLoading(message: String) {
        if (!isFinishing) {
            progressDialg.setContent(message)
            if (!progressDialg.isShowing) {
                progressDialg.show()
            }
        }
    }

    override fun hideLoading() {
        if (!isFinishing) {
            progressDialg.dismiss()
        }
    }

    override fun clutchDollSuccess(message: String) {
        if (!isFinishing) {
            MaterialDialog.Builder(this).title("本局结果").content(message).positiveText(
                "开心 ^_^").build().show()
        }
    }

    override fun clutchDollFailure(error: String) {
        if (!isFinishing) {
            MaterialDialog.Builder(this).title("本局结果").content(error).positiveText(
                "蓝瘦，香菇").build().show()
        }
    }

    override fun onStop() {
        super.onStop()
        mLivePresenter.wildDogDestroy()
    }

}