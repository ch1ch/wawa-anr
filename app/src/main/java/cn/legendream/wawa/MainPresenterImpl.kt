package cn.legendream.wawa

import cn.legendream.wawa.app.AppInfo
import cn.legendream.wawa.app.WaWaApplication
import cn.legendream.wawa.app.net.NetService
import cn.legendream.wawa.app.scope.ActivityScope
import com.hwangjr.rxbus.Bus
import com.hwangjr.rxbus.RxBus
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import javax.inject.Inject

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/18
 * E-Mail: zhaoxiyuan@kokozu.net
 */

@ActivityScope
class MainPresenterImpl @Inject constructor(private val application: WaWaApplication, private val mainView: MainContract.MainView, private val netService: NetService) : MainContract.MainPresenter {

    private val wxApi: IWXAPI by lazy {
        val api = WXAPIFactory.createWXAPI(application, AppInfo.WX_APP_ID)
        api.apply {
            registerApp(AppInfo.WX_APP_ID)
        }
    }

    private val rxbus: Bus = RxBus.get()

    override fun weChatLogin() {
//        rxbus.register(this)
        startWeChat()
//        netService.wxlogin("123")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ Timber.d(it.data.toString()) }, { Timber.d(it) })
    }


//    @Subscribe(thread = EventThread.MAIN_THREAD)
//    fun login(string: String) {
//        rxbus.unregister(this)
//    }


    private fun startWeChat() {
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = "wechat_sdk_demo_test"
        wxApi.sendReq(req)
    }
}