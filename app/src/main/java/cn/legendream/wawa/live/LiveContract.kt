package cn.legendream.wawa.live

/**
 * Created by zhao on 2017/10/24.
 */

interface LiveContract {
    interface View {
        fun startGame()
        fun waitGame()
        fun crateOrderError(error:String)
    }

    interface Presenter {
        fun createOrder(machineId: Int, token: String)
    }
}