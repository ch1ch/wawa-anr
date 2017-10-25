package cn.legendream.wawa.live

import com.wilddog.video.base.LocalStream
import com.wilddog.video.call.RemoteStream

/**
 * Created by zhao on 2017/10/24.
 */

interface LiveContract {
    interface View {
        fun startGame()
        fun waitGame()
        fun finishWait()
        fun crateOrderError(error: String)
        fun showGameVideo(remoteStream: RemoteStream)
        fun showLocalVideo(localStream: LocalStream)
    }


    interface Presenter {
        fun createOrder(machineId: Int, token: String)
        fun startGameVideo(videoId:String)
        fun destroy()
    }
}