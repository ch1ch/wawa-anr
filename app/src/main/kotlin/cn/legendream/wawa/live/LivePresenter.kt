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
import com.wilddog.video.call.stats.LocalStreamStatsReport
import com.wilddog.video.call.stats.RemoteStreamStatsReport
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
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
    private var conversation2: Conversation? = null
    private val user by lazy {
        UserManager.getUser()
    }

    private var remoteStream: RemoteStream? = null
    private var remoteStream2: RemoteStream? = null
    private var currentRemoteStream: RemoteStream? = null

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
                            startGameDownTime()
                            syncRef?.removeEventListener(this)
                        }
                    }
                } else {
                    waitGame = false
                    startGameDownTime()
                    syncRef?.removeEventListener(this)
                }
            }
        }
    }


    private fun startGameDownTime() {
        Observable.intervalRange(1, 6, 0, 1, TimeUnit.SECONDS).observeOn(
            AndroidSchedulers.mainThread()).subscribe {
            Timber.d(it.toString())
            liveView.finishWait(it.toInt())
        }
    }

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
        }
        )
    }

    override fun refreshUserInfo() {
        netService.userInfo(user?.token ?: "").compose(NetService.ioToMain()).subscribe({
            when (it.code) {
                NetServiceCode.NORMAL.code -> {
                    val user = it.data
                    if (user != null) {
                        liveView.updateUserInfo(user)
                        UserManager.saveUser(user)
                    } else {
                        liveView.updateUserInfoFailure(it.error.toString())
                    }
                }
                else -> {
                    liveView.updateUserInfoFailure("${it.error.toString()} ${it.code}")
                }
            }
        }, {
            liveView.updateUserInfoFailure(it.message.toString())
        })
    }

    override fun startGameVideo(video1: String, video2: String) {
        Timber.d("prepare video by $video1")
        val video = WilddogVideoCall.getInstance()

//        val conversation = video.call(videoId, localStream, "test")
        localStream = _localStream
        conversation = video.call(video1, localStream, "test")
        conversation?.setConversationListener(object : Conversation.Listener {
            override fun onStreamReceived(p0: RemoteStream?) {
                Timber.d("onStreamReceived---1")
                if (p0 != null) {
                    remoteStream = p0
                    remoteStream?.enableAudio(false)
                    liveView.showGameVideo1(p0)
//                    startVideo2(_localStream, video2)
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

        conversation?.setStatsListener(object :Conversation.StatsListener {
            override fun onLocalStreamStatsReport(p0: LocalStreamStatsReport?) {
            }

            override fun onRemoteStreamStatsReport(remoteStreamStatsReport: RemoteStreamStatsReport?) {
                 Timber.d("width " + remoteStreamStatsReport?.width.toString())
                 Timber.d("height "+remoteStreamStatsReport?.height.toString())
                 Timber.d("fps " +remoteStreamStatsReport?.fps.toString())
                 Timber.d("bytesReceived "+remoteStreamStatsReport?.bytesReceived.toString())
                 Timber.d("bitsReceived " + remoteStreamStatsReport?.bitsReceivedRate.toString())
                 Timber.d("delay " + remoteStreamStatsReport?.delay.toString())
            }
        })
    }

    private fun startVideo2(localStream: LocalStream, video2: String) {
        val video = WilddogVideoCall.getInstance()
        conversation2 = video.call(video2, localStream, "test")
        conversation2?.setConversationListener(object : Conversation.Listener {
            override fun onStreamReceived(p0: RemoteStream?) {
                Timber.d("onStreamReceived---2")
                if (p0 != null) {
                    remoteStream2 = p0
                    remoteStream2?.enableAudio(false)
//                    liveView.showGameVideo2(p0)
                }
            }

            override fun onClosed() {
                Timber.d("onClosed 2")
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

    override fun switchGameVideo() {
        if (currentRemoteStream == remoteStream) {
            Timber.d("switch to 2")
            remoteStream?.detach()
            if (remoteStream2 != null) {
                liveView.showGameVideo2(remoteStream2!!)
            }
            currentRemoteStream = remoteStream2
            return
        }

        if (currentRemoteStream == remoteStream2) {
            Timber.d("switch to 1")

            remoteStream2?.detach()
            if (remoteStream != null) {
                liveView.showGameVideo1(remoteStream!!)
            }
            currentRemoteStream = remoteStream
        }
    }

    override fun destroy() {
        Timber.d("Live stop ")
        localStream?.detach()
        remoteStream?.detach()
        remoteStream2?.detach()
        conversation?.close()
        conversation2?.close()
        conversation = null
        conversation2 = null
    }

}