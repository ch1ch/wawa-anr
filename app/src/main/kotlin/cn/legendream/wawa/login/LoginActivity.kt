package cn.legendream.wawa.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import cn.legendream.wawa.R
import cn.legendream.wawa.app.WaWaApplication
import cn.legendream.wawa.app.model.User
import cn.legendream.wawa.web.WebActivity
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LoginContract.loginView {

    @Inject
    lateinit var mLoginPresenter: LoginPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerLoginComponent.builder().appComponent((application as WaWaApplication).getAppComponent())
                .loginModule(LoginModule(this)).build().inject(this)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.btn_wx_login)
    fun wxLogin() {
        mLoginPresenter.weChatLogin()
    }

    @OnClick(R.id.btn_term)
    fun gotoTerm() {
        val intent = Intent(this, WebActivity::class.java)
        startActivity(intent)
    }

    override fun loginSuccess(user: User) {
        val intent = Intent()
        intent.putExtra("user", user)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun loginError(error: String) {
        Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}
