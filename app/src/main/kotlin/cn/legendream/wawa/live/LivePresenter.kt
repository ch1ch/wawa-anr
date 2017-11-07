package cn.legendream.wawa.live

import android.os.CountDownTimer
import cn.legendream.wawa.app.AppInfo
import cn.legendream.wawa.app.model.Machine
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
    private val user by lazy {
        UserManager.getUser()
    }

    private var remoteStream: RemoteStream? = null
    private var currentVideoUrl: String = ""
    private var video1: String = ""
    private var video2: String = ""
    private var retryVideoEstablishCount = 0
    private var gameTimer: GameDownTime? = null


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
                        val userIdList = p0.value as? ArrayList<Long?>
                        if (userIdList != null && userIdList.size > 1) {
                            if ((userIdList[1] ?: -1) == user?.id?.toLong() ?: 0) {
                                waitGame = false
                                startWaitGameDownTime()
                                syncRef?.removeEventListener(this)
                            }
                        }
                    }
                } else {
                    waitGame = false
                    startWaitGameDownTime()
                    syncRef?.removeEventListener(this)
                }
            }
        }
    }


    private fun startWaitGameDownTime() {
        Observable.intervalRange(1, 6, 0, 1, TimeUnit.SECONDS).observeOn(
            AndroidSchedulers.mainThread()).subscribe {
            Timber.d(it.toString())
            liveView.finishWait(it.toInt())
        }
    }

    private fun startGameDownTime() {
        gameTimer?.cancel()
        gameTimer = GameDownTime()
        gameTimer?.start()
    }


    override fun createOrder(machineId: Int, token: String) {
        netService.createOrder(token, machineId).compose(NetService.ioToMain()).subscribe({
            Timber.d("createOrder: $it")
            when {
                it.code == NetServiceCode.NORMAL.code -> kotlin.run {
                    waitGame = false
                    liveView.startGame()
                    startGameDownTime()
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
        if (!currentVideoUrl.isEmpty()) return

        this.video1 = video1
        this.video2 = video2
        switchGameVideo()
    }


    override fun movePawTo(machine: Machine, pawDirection: LiveContract.PawDirection) {
        netService.movePawTo("http://${machine.ipAddress}/action", pawDirection.direction, 100).compose(
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

    override fun clutch(machine: Machine) {
        netService.pawCatch("http://${machine.ipAddress}/action", LiveContract.PawDirection.CATCH.direction,
            100).compose(NetService.ioToMain()).subscribe({
            if (it.code != NetServiceCode.NORMAL.code) {
                liveView.pawCatchFailure(it.error.toString())
            } else {
                liveView.pawCatchSuccess()
            }
        }, {
            liveView.pawCatchFailure(it.message.toString())
        })
    }

    override fun switchGameVideo() {
        wildDogDestroy()
        currentVideoUrl = when {
            currentVideoUrl.isEmpty() -> video1
            currentVideoUrl == video1 -> video2
            else -> video1
        }
        startVideo(currentVideoUrl)
    }

    private fun startVideo(videoUrl: String) {
        Timber.d("prepare video by $currentVideoUrl")
        val video = WilddogVideoCall.getInstance()

//        val conversation = video.call(videoId, localStream, "test")
        localStream = _localStream
        conversation = video.call(currentVideoUrl, localStream, "test")
        conversation?.setConversationListener(object : Conversation.Listener {
            override fun onStreamReceived(p0: RemoteStream?) {
                Timber.d("onStreamReceived---1")
                if (p0 != null) {
                    remoteStream = p0
                    remoteStream?.enableAudio(false)
                    liveView.showGameVideo(p0)
                }
                retryVideoEstablishCount = 0

            }

            override fun onClosed() {
                Timber.d("onClosed")
                retryVideoEstablishCount = 0

            }

            override fun onCallResponse(p0: CallStatus?) {
                if (p0 == CallStatus.TIMEOUT && retryVideoEstablishCount < 3) {
                    retryVideoEstablishCount++
                    wildDogDestroy()
                    startVideo(videoUrl)
                } else {
                    retryVideoEstablishCount = 0
                }
                Timber.d("onCallResponse ${p0.toString()} $retryVideoEstablishCount")
            }

            override fun onError(p0: WilddogVideoError?) {
                Timber.d("onError")
                retryVideoEstablishCount = 0
            }
        })

        conversation?.setStatsListener(object : Conversation.StatsListener {
            override fun onLocalStreamStatsReport(p0: LocalStreamStatsReport?) {
            }

            override fun onRemoteStreamStatsReport(
                remoteStreamStatsReport: RemoteStreamStatsReport?) {
                Timber.d("width " + remoteStreamStatsReport?.width.toString())
                Timber.d("height " + remoteStreamStatsReport?.height.toString())
                Timber.d("fps " + remoteStreamStatsReport?.fps.toString())
                Timber.d("bytesReceived " + remoteStreamStatsReport?.bytesReceived.toString())
                Timber.d("bitsReceived " + remoteStreamStatsReport?.bitsReceivedRate.toString())
                Timber.d("delay " + remoteStreamStatsReport?.delay.toString())
            }
        })
    }

    override fun wildDogDestroy() {
        Timber.d("Live stop ")
//        localStream?.detach()
        remoteStream?.detach()
        conversation?.close()
        conversation = null
    }

    override fun destroy() {
        gameTimer?.cancel()
        gameTimer = null
    }

    inner class GameDownTime : CountDownTimer(5_000, 1_000) {
        override fun onTick(p0: Long) {
            liveView.updateGameTime(p0 / 1_000)
        }

        override fun onFinish() {
            liveView.gameTimeIsOver()
        }
    }

}