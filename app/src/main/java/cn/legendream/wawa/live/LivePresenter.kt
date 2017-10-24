package cn.legendream.wawa.live

import cn.legendream.wawa.app.net.NetService
import com.wilddog.client.*
import com.wilddog.video.base.LocalStream
import com.wilddog.video.base.LocalStreamOptions
import com.wilddog.video.base.WilddogVideoError
import com.wilddog.video.call.CallStatus
import com.wilddog.video.call.Conversation
import com.wilddog.video.call.RemoteStream
import com.wilddog.video.call.WilddogVideoCall
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by zhao on 2017/10/24.
 */


class LivePresenter @Inject constructor(private val liveView: LiveContract.View, private val netService: NetService) : LiveContract.Presenter {

    private val localStream by lazy {
        val localStreamOptionsBuilder = LocalStreamOptions.Builder()
        localStreamOptionsBuilder.captureAudio(false).captureVideo(true)
        val opt = localStreamOptionsBuilder.build()
        LocalStream.create(opt)
    }

    private var syncRef: SyncReference? = null

    private val valueEventListener by lazy {
        object : ValueEventListener {
            override fun onCancelled(p0: SyncError?) {
                Timber.d("onCancelled: ")
            }

            override fun onDataChange(p0: DataSnapshot?) {
                Timber.d("onDataChange: ")
//                TODO("排队至当前用户删除监听")
                syncRef?.removeEventListener(this)
            }
        }
    }

    private var remoteStream: RemoteStream? = null

    override fun createOrder(machineId: Int, token: String) {
        netService.createOrder(token, machineId).subscribeOn(
                Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Timber.d("createOrder: $it")
            if (it.code == 200) {
                liveView.startGame()
            } else {
                liveView.waitGame()
                syncRef = WilddogSync.getInstance().getReference("$machineId")
                syncRef?.addValueEventListener(valueEventListener)
            }
        }, {
            it.printStackTrace()
            liveView.crateOrderError(it.message.toString())
        }
        )
    }

    override fun startGameVideo(videoId: String) {
        Timber.d("prepare video by $videoId")
//        liveView.showLocalVideo(localStream)
        val conversation = WilddogVideoCall.getInstance().call(videoId, localStream, "")
        conversation.setConversationListener(object : Conversation.Listener {
            override fun onStreamReceived(p0: RemoteStream?) {
                if (p0 != null) {
                    remoteStream = p0
                    liveView.showGameVideo(p0)
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

    override fun destroy() {
        localStream.detach()
        remoteStream?.detach()
    }

}