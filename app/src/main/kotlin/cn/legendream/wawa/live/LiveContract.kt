package cn.legendream.wawa.live

import cn.legendream.wawa.app.model.Machine
import cn.legendream.wawa.app.model.User
import com.wilddog.video.base.LocalStream
import com.wilddog.video.call.RemoteStream

/**
 * Created by zhao on 2017/10/24.
 */

interface LiveContract {
    interface View {
        fun startGame(orderId: String)
        fun waitGame()
        fun finishWait(waitTime: Int)
        fun crateOrderError(error: String)
        fun showGameVideo(remoteStream: RemoteStream)
        fun showLocalVideo(localStream: LocalStream)
        fun movePawSuccess(direction: PawDirection)
        fun movePawFailure(direction: PawDirection, error: String)
        fun pawDownSuccess() //下抓成功
        fun pawDownFailure(error: String) //下抓失败
        fun pawCatchFinish() // 抓取结束
        fun pawCatchFailure(error: String)
        fun updateUserInfo(user: User)
        fun updateUserInfoFailure(error: String)
        fun updateGameTime(time: Long)
        fun gameTimeIsOver()
        fun clutchDollSuccess(message: String) //订单成功
        fun clutchDollFailure(error: String)//订单失败
        fun showLoading(message: String)
        fun hideLoading()
    }


    interface Presenter {
        fun refreshUserInfo()
        fun createOrder(machineId: Int, token: String)
        fun startGameVideo(video1: String, video2: String)
        fun movePawTo(machine: Machine, pawDirection: PawDirection)
        fun catch(machine: Machine)
        fun switchGameVideo()
        fun wildDogDestroy()
        fun destroy()
        fun checkGameResult(orderId: String)
    }

    enum class PawDirection(val direction: Int) {
        UP(1),
        DOWN(2),
        LEFT(3),
        RIGHT(4),
        CATCH(6)

    }
}