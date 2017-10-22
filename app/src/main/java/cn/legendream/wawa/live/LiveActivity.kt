package cn.legendream.wawa.live

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import cn.legendream.wawa.R
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import kotlinx.android.synthetic.main.activity_live.*
import kotlinx.android.synthetic.main.empty_control_video.view.*

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/20
 * E-Mail: zhaoxiyuan@kokozu.net
 */

class LiveActivity :AppCompatActivity() {
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

        video_view.setUp("rtmp://10799.liveplay.myqcloud.com/live/10799_784387bddc", false, "demo")
//        GSYVideoManager.instance().
//        video_view.rotation = 90f
        video_view.surface_container.rotation = 90f
        video_view.startPlayLogic()

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
}