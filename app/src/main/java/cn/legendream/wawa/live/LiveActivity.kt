package cn.legendream.wawa.live

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import cn.legendream.wawa.R
import cn.legendream.wawa.app.WaWaApplication
import cn.legendream.wawa.app.contract.ExtraKey
import cn.legendream.wawa.app.extra.toast
import cn.legendream.wawa.app.model.Machine
import cn.legendream.wawa.app.user.UserManager
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.wilddog.client.*
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
    private val syncRef by lazy {
        val ref = WilddogSync.getInstance().reference

        ref.child(machine.id.toString())
        ref.apply {
            addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: SyncError?) {
                    Timber.d("onCancelled")
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                    Timber.d("onCancelled")
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                    Timber.d("onChildChanged: ")
                }

                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    Timber.d("onChildAdded: ")
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                    Timber.d("onChildRemoved: ")
                }
            })

            addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: SyncError?) {
                    Timber.d("onCancelled: ")
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    Timber.d("onDataChange: ")
                }
            })
        }
    }

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
            mLivePresenter.createOrder(machine.id ?: -1, UserManager.getUser()?.token ?: "")
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

    override fun crateOrderError(error: String) {
        toast(error)
    }
}