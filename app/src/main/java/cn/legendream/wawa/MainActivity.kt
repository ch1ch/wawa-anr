package cn.legendream.wawa

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import cn.legendream.wawa.app.WaWaApplication
import cn.legendream.wawa.app.model.User
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.MainView {

    @Inject
    lateinit var mainPresenter: MainPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerMainComponent.builder().appComponent((application as WaWaApplication).getAppComponent())
                .mainModule(MainModule(this)).build().inject(this)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.btn_wx_login)
    fun wxLogin() {
        mainPresenter.weChatLogin()
    }

    override fun loginSuccess(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loginError(error: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
