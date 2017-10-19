package cn.legendream.wawa.wxapi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.legendream.wawa.app.AppInfo
import cn.legendream.wawa.login.LoginPresenterImpl
import com.hwangjr.rxbus.RxBus
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import timber.log.Timber

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/17
 * E-Mail: zhaoxiyuan@kokozu.net
 */
class WXEntryActivity : AppCompatActivity(), IWXAPIEventHandler {


    private val wxApi: IWXAPI by lazy {
        val api = WXAPIFactory.createWXAPI(application, AppInfo.WX_APP_ID, true)
        api.apply {
            registerApp(AppInfo.WX_APP_ID)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.let {
            wxApi.handleIntent(intent, this)
        }

    }

    override fun onResp(p0: BaseResp?) {
        Timber.d("resq:" + p0)
        val sendAuthResp = p0 as? SendAuth.Resp
        if (sendAuthResp != null && sendAuthResp.errCode == BaseResp.ErrCode.ERR_OK) {
            RxBus.get().post(LoginPresenterImpl.SUCCESS_TAG, sendAuthResp.code)
        } else {
            var error = "未知错误"
            if (sendAuthResp != null) {
                error = when (sendAuthResp.errCode) {
                    BaseResp.ErrCode.ERR_USER_CANCEL -> "已取消登录"
                    else -> "授权失败"
                }
            }
            RxBus.get().post(LoginPresenterImpl.FAILURE_TAG, error)
        }
        finish()
    }

    override fun onReq(p0: BaseReq?) {
        Timber.d("req:" + p0)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent?.let {
            wxApi.handleIntent(intent, this)
        }
    }
}