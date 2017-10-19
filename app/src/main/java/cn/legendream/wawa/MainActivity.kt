package cn.legendream.wawa

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import butterknife.ButterKnife
import cn.legendream.wawa.app.WaWaApplication
import cn.legendream.wawa.app.model.User
import cn.legendream.wawa.app.user.UserManager
import cn.legendream.wawa.login.LoginActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.MainView {

    @Inject
    lateinit var mainPresenter: MainPresenterImpl
    private var pageLoadFinish = false
    private var checkUserFinish = false
    private var loginFinish = true
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerMainComponent.builder().appComponent((application as WaWaApplication).getAppComponent())
                .mainModule(MainModule(this)).build().inject(this)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        initView()
        mainPresenter.checkLogin()
    }

    private fun initView() {
        web_view.webViewClient = WaWaWebViewClient()
        web_view.webChromeClient = WaWaWebChromeClient()
        web_view.settings.javaScriptEnabled = true
        web_view.settings.domStorageEnabled = true
        web_view.settings.setAppCacheEnabled(true)
        web_view.loadUrl("http://wawa.legendream.cn/#/home")
    }

    override fun haveLogin(user: User) {
        checkUserFinish = true
        this.user = user
        if (pageLoadFinish) {
            callJsTransformUser(user)
        }
    }

    override fun noLogin() {
        checkUserFinish = true
        if (loginFinish) {
            loginFinish = false
            startLogin()
        }
    }

    private fun callJsTransformUser(user: User) {
        val gson = Gson()
        web_view.loadUrl("javascript:getUserInfo('${gson.toJson(user)}')")
    }

    private fun startLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100 && data != null) {
            val user = data.getParcelableExtra<User>("user")
            if (user == null) {
                Toast.makeText(applicationContext, "登录失败", Toast.LENGTH_LONG).show()
            } else {
                this.user = user
                UserManager.saveUser(user)
            }
        } else {
            Toast.makeText(applicationContext, "退出", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    inner class WaWaWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            pageLoadFinish = true
            if (checkUserFinish && user != null) {
                callJsTransformUser(user!!)
            } else if (loginFinish) {
                loginFinish = false
                startLogin()
            }
        }
    }

    inner class WaWaWebChromeClient : WebChromeClient()

}
