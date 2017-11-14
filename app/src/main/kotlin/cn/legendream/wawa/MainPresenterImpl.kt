package cn.legendream.wawa

import cn.legendream.wawa.app.WaWaApplication
import cn.legendream.wawa.app.scope.ActivityScope
import cn.legendream.wawa.app.user.UserManager
import javax.inject.Inject

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/18
 * E-Mail: zhaoxiyuan@kokozu.net
 */

@ActivityScope
class MainPresenterImpl @Inject constructor(private val application: WaWaApplication,
                                            private val mainView: MainContract.MainView) :
    MainContract.MainPresenter {
    override fun checkLogin() {
        val user = UserManager.getUser()
        if (user == null) {
            mainView.noLogin()
        } else {
            mainView.haveLogin(user)
        }
    }

    override fun loginWildDog() {

//        WilddogAuth.getInstance().signInAnonymously().addOnCompleteListener { task ->
//            kotlin.run {
//                if (task.isSuccessful) {
//                    val wilddogUser = task.result.wilddogUser
//                    val token = wilddogUser.getToken(false).result.token
//                    WilddogVideoInitializer.initialize(application, "wd0062598867lwtpxz", token)
//                    Timber.d("Token:$token , ${wilddogUser.uid}")
                    mainView.wildDogSuccess()
//                } else {
//                    Timber.d("wilddog login failure")
//                    mainView.wildDogFailure("服务器连接失败")
//                }
//            }
//        }
    }
}