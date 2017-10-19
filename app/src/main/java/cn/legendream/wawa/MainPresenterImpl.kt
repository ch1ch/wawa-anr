package cn.legendream.wawa

import cn.legendream.wawa.app.AppInfo
import cn.legendream.wawa.app.WaWaApplication
import cn.legendream.wawa.app.net.NetService
import cn.legendream.wawa.app.scope.ActivityScope
import cn.legendream.wawa.login.LoginPresenterImpl
import com.hwangjr.rxbus.Bus
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import timber.log.Timber
import javax.inject.Inject

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/18
 * E-Mail: zhaoxiyuan@kokozu.net
 */

@ActivityScope
class MainPresenterImpl @Inject constructor(private val application: WaWaApplication,
                                            private val mainView: MainContract.MainView,
                                            private val netService: NetService) : MainContract.MainPresenter {
    private val rxbus: Bus = RxBus.get()

    init {
        rxbus.register(this)
    }

    private val wxApi: IWXAPI by lazy {
        val api = WXAPIFactory.createWXAPI(application, AppInfo.WX_APP_ID)
        api.apply {
            registerApp(AppInfo.WX_APP_ID)
        }
    }


    override fun weChatLogin() {
        startWeChat()
    }


    @Subscribe(tags = arrayOf(Tag(LoginPresenterImpl.SUCCESS_TAG)),
        thread = EventThread.MAIN_THREAD)
    fun login(string: String) {
        Timber.d(string)
//        rxbus.unregister(this)
    }

    @Subscribe(tags = arrayOf(Tag(LoginPresenterImpl.FAILURE_TAG)),
        thread = EventThread.MAIN_THREAD)
    fun loginFaiure(string: String) {
        Timber.d(string)
//        rxbus.unregister(this)
    }

    private fun startWeChat() {
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = "wechat_sdk_demo_test"
        Timber.d(wxApi.sendReq(req).toString())
    }
}