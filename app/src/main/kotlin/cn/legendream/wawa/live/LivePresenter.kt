package cn.legendream.wawa.live

import cn.legendream.wawa.app.AppInfo
import cn.legendream.wawa.app.net.NetService
import cn.legendream.wawa.app.net.NetServiceCode
import cn.legendream.wawa.app.user.UserManager
import com.wilddog.client.*
import com.wilddog.video.base.LocalStream
import com.wilddog.video.base.LocalStreamOptions
import com.wilddog.video.base.WilddogVideoError
import com.wilddog.video.call.CallStatus
import com.wilddog.video.call.Conversation
import com.wilddog.video.call.RemoteStream
import com.wilddog.video.call.WilddogVideoCall
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by zhao on 2017/10/24.
 */


class LivePresenter @Inject constructor(private val liveView: LiveContract.View,
                                        private val netService: NetService) : LiveContract.Presenter {

    private val _localStream by lazy {
        val localStreamOptionsBuilder = LocalStreamOptions.Builder()
        localStreamOptionsBuilder.captureAudio(true).captureVideo(true)
        val opt = localStreamOptionsBuilder.build()
        LocalStream.create(opt)
    }

    private var waitGame = false
    private var localStream: LocalStream? = null
    private var syncRef: SyncReference? = null
    private var conversation: Conversation? = null
    private val user by lazy {
        UserManager.getUser()
    }

    private val valueEventListener by lazy {
        object : ValueEventListener {
            override fun onCancelled(p0: SyncError?) {
                Timber.d("onCancelled: ")
            }

            override fun onDataChange(p0: DataSnapshot?) {
                Timber.d("onDataChange: ${p0.toString()}")
                if (p0?.exists() == true) {
                    if (waitGame) {
                        @Suppress("UNCHECKED_CAST")
                        val userIdList = p0.value as ArrayList<Long?>
                        val userId = userIdList.find {
                            user?.id == it?.toInt() ?: -1
                        }
                        if (userId == null) {
                            waitGame = false
                            liveView.finishWait()
                            syncRef?.removeEventListener(this)
                        }
                    }
                }
            }
        }
    }

    private var remoteStream: RemoteStream? = null

    override fun createOrder(machineId: Int, token: String) {
        netService.createOrder(token, machineId).compose(NetService.ioToMain()).subscribe({
            Timber.d("createOrder: $it")
            when {
                it.code == NetServiceCode.NORMAL.code -> kotlin.run {
                    waitGame = false
                    liveView.startGame()
                }
                it.code == NetServiceCode.PUT_USER_LINE.code -> {
                    waitGame = true
                    liveView.waitGame()
                    syncRef = WilddogSync.getInstance().getReference("$machineId")
                    syncRef?.addValueEventListener(valueEventListener)
                    SyncReference.goOnline()
                }
                else -> liveView.crateOrderError(it.error.toString())
            }
        }, {
            it.printStackTrace()
            liveView.crateOrderError(it.message.toString())
            Double
        }
        )
    }

    override fun startGameVideo(videoId: String) {
        Timber.d("prepare video by $videoId")
        val video = WilddogVideoCall.getInstance()

//        val conversation = video.call(videoId, localStream, "test")
        localStream = _localStream
        conversation = video.call(videoId, localStream, "test")
        conversation?.setConversationListener(object : Conversation.Listener {
            override fun onStreamReceived(p0: RemoteStream?) {
                Timber.d("onStreamReceived")
                if (p0 != null) {
                    remoteStream = p0
                    remoteStream?.enableAudio(false)
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

    override fun movePawTo(pawDirection: LiveContract.PawDirection) {
        netService.movePawTo(AppInfo.GAME_URL + "/action", pawDirection.direction, 100).compose(
            NetService.ioToMain()).subscribe({
            if (it.code != NetServiceCode.NORMAL.code) {
                liveView.movePawFailure(pawDirection, it.error.toString())
            } else {
                liveView.movePawSuccess(pawDirection)
            }
        }, {
            liveView.movePawFailure(pawDirection, it.message.toString())
        })
    }

    override fun clutch() {
        netService.pawCatch(AppInfo.GAME_URL + "/action", LiveContract.PawDirection.CATCH.direction,
            100).compose(NetService.ioToMain()).subscribe({
            if (it.code != NetServiceCode.NORMAL.code) {
                liveView.movePawFailure(LiveContract.PawDirection.CATCH, it.error.toString())
            } else {
                liveView.pawCatchSuccess()
            }
        }, {
            liveView.pawCatchFailure(it.message.toString())
        })
    }

    override fun destroy() {
        Timber.d("Live stop ")
        localStream?.detach()
        remoteStream?.detach()
        conversation?.close()
        conversation = null
    }

}