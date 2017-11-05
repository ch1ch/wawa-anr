package cn.legendream.wawa.live

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.legendream.wawa.R
import com.wilddog.video.base.LocalStream
import com.wilddog.video.base.LocalStreamOptions
import com.wilddog.video.base.WilddogVideoError
import com.wilddog.video.call.CallStatus
import com.wilddog.video.call.Conversation
import com.wilddog.video.call.RemoteStream
import com.wilddog.video.call.WilddogVideoCall
import kotlinx.android.synthetic.main.activity_test.*
import timber.log.Timber

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/31
 * E-Mail: zhaoxiyuan@kokozu.net
 */

class TestActivity:AppCompatActivity() {

    private val localStream by lazy {
        val localStreamOptionsBuilder = LocalStreamOptions.Builder()
        localStreamOptionsBuilder.captureAudio(false).captureVideo(false)
        val opt = localStreamOptionsBuilder.build()
        LocalStream.create(opt)
    }

    private var conversation:Conversation? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val video = WilddogVideoCall.getInstance()

//        val conversation = video.call("", localStream, "test")
        conversation = video.call("08cb326eccb320ca7c7c202cce43", localStream, "conversationDemo")
        conversation?.setConversationListener(object : Conversation.Listener {
            override fun onStreamReceived(p0: RemoteStream?) {
                Timber.d("onStreamReceived")
                if (p0 != null) {
//                    remoteStream = p0
//                    remoteStream?.enableAudio(false)
//                    liveView.showGameVideo1(p0)
                    p0.attach(wild_dog_view)
                }
            }

            override fun onClosed() {
                Timber.d("onClosed")
            }

            override fun onCallResponse(p0: CallStatus?) {
                Timber.d("onCallResponse ${p0.toString()}")
            }

            override fun onError(p0: WilddogVideoError?) {
                Timber.d("onError")
            }
        })
    }

    override fun onStop() {
        super.onStop()
        conversation?.close()
    }
}