package cn.legendream.wawa.wxapi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import cn.legendream.wawa.app.AppInfo
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

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

    override fun onResp(p0: BaseResp?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onReq(p0: BaseReq?) {

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            wxApi.handleIntent(intent, this)
        }
    }
}