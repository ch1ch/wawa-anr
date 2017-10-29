package cn.legendream.wawa.payment.wxpay;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import cn.legendream.wawa.app.AppInfo;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static android.content.ContentValues.TAG;

/**
 * Author: ZhaoXiyuan
 * Date: 2016/11/27
 * E-Mail: zhaoxiyuan@kokozu.net
 */

public abstract class BaseWXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI mIWXAPI;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIWXAPI = WXAPIFactory.createWXAPI(this, AppInfo.WX_APP_ID);
        mIWXAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mIWXAPI.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d(TAG, "onReq: " + baseReq);
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() != ConstantsAPI.COMMAND_PAY_BY_WX) {
            resp.errStr = "非微信支付功能调用";
            resp.errCode = -1;
        }

        LocalBroadcastManager localBroadcastManager =
            LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter intentFilter = new IntentFilter(getPackageName() + ".WXPAY");
        localBroadcastManager.registerReceiver(WXPayer.WXBroadCast.getInstance(), intentFilter);
        Intent intent = new Intent();
        intent.setAction(getPackageName() + ".WXPAY");
        intent.putExtra("errCode", resp.errCode);
        intent.putExtra("errStr", resp.errStr);
        localBroadcastManager.sendBroadcast(intent);

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIWXAPI != null) {
            mIWXAPI.detach();
        }
    }
}
