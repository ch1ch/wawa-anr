package cn.legendream.wawa.web

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import cn.legendream.wawa.R
import cn.legendream.wawa.app.AppInfo
import kotlinx.android.synthetic.main.activty_web.*

/**
 * Author: ZhaoXiyuan
 * Date: 2017/11/12
 * E-Mail: zhaoxiyuan@kokozu.net
 */

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activty_web)
        web_view.webChromeClient = WebChromeClient()
        web_view.webViewClient = WebViewClient()
        web_view.loadUrl(AppInfo.TERM_URL)
    }
}