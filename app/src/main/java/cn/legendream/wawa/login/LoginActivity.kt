package cn.legendream.wawa.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import cn.legendream.wawa.R
import cn.legendream.wawa.app.WaWaApplication
import cn.legendream.wawa.app.model.User
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

    override fun loginSuccess(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loginError(error: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
