package cn.legendream.wawa.live

import cn.legendream.wawa.app.model.User
import com.wilddog.video.base.LocalStream
import com.wilddog.video.call.RemoteStream

/**
 * Created by zhao on 2017/10/24.
 */

interface LiveContract {
    interface View {
        fun startGame()
        fun waitGame()
        fun finishWait(waitTime: Int)
        fun crateOrderError(error: String)
        fun showGameVideo1(remoteStream: RemoteStream)
        fun showGameVideo2(remoteStream: RemoteStream)
        fun showLocalVideo(localStream: LocalStream)
        fun movePawSuccess(direction: PawDirection)
        fun movePawFailure(direction: PawDirection, error: String)
        fun pawCatchSuccess()
        fun pawCatchFailure(error: String)
        fun updateUserInfo(user: User)
        fun updateUserInfoFailure(error: String)
    }


    interface Presenter {
        fun refreshUserInfo()
        fun createOrder(machineId: Int, token: String)
        fun startGameVideo(video1: String, video2: String)
        fun movePawTo(pawDirection: PawDirection)
        fun clutch()
        fun switchGameVideo()
        fun destroy()
    }

    enum class PawDirection(val direction: Int) {
        UP(1),
        DOWN(2),
        LEFT(3),
        RIGHT(4),
        CATCH(6)

    }
}