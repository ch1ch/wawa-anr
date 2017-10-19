package cn.legendream.wawa.login

import cn.legendream.wawa.app.AppInfo
import cn.legendream.wawa.app.WaWaApplication
import cn.legendream.wawa.app.net.NetService
import cn.legendream.wawa.app.scope.ActivityScope
import com.hwangjr.rxbus.Bus
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/18
 * E-Mail: zhaoxiyuan@kokozu.net
 */

@ActivityScope
class LoginPresenterImpl @Inject constructor(private val application: WaWaApplication,
        private val loginView: LoginContract.loginView,
        private val netService: NetService) : LoginContract.LoginPresenter {

    companion object {
        const val SUCCESS_TAG = "success"
        const val FAILURE_TAG = "failure"
    }

    private val rxbus: Bus = RxBus.get()
    private var loginDisposable: Disposable? = null

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

    override fun destroy() {
        RxBus.get().unregister(this)
        if (loginDisposable?.isDisposed == false) {
            loginDisposable?.dispose()
        }
    }

    @Subscribe(tags = arrayOf(Tag(LoginPresenterImpl.SUCCESS_TAG)),
            thread = EventThread.MAIN_THREAD)
    fun login(code: String) {
        Timber.d(code)
        loginDisposable = netService.wxlogin(code).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            if (it.code == 200 && it.data != null) {
                val user = it.data!!
                loginView.loginSuccess(user)
            } else {
                loginView.loginError(it.error ?: "未知错误${it.code}")
            }
        }, {
            loginView.loginError("APP异常")
        })
    }

    @Subscribe(tags = arrayOf(Tag(LoginPresenterImpl.FAILURE_TAG)),
            thread = EventThread.MAIN_THREAD)
    fun loginFaiure(string: String) {
        Timber.d(string)
        loginView.loginError(string)
    }


    private fun startWeChat() {
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = "wechat_sdk_demo_test"
        wxApi.sendReq(req)
    }
}