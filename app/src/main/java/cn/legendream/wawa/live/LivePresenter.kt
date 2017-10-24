package cn.legendream.wawa.live

import cn.legendream.wawa.app.net.NetService
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by zhao on 2017/10/24.
 */


class LivePresenter @Inject constructor(private val liveView: LiveContract.View,
                                        private val netService: NetService) : LiveContract.Presenter {

    override fun createOrder(machineId: Int, token: String) {
        netService.createOrder(token, machineId).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Timber.d("createOrder: $it")
            if (it.code == 200) {
                liveView.startGame()
            } else {
                liveView.waitGame()
            }
        }, {
            it.printStackTrace()
            liveView.crateOrderError(it.message.toString())
        }
        )
    }
}