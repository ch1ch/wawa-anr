package cn.legendream.wawa

import cn.legendream.wawa.app.model.User

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/18
 * E-Mail: zhaoxiyuan@kokozu.net
 */

interface MainContract {
    interface MainView {
        fun haveLogin(user: User)
        fun noLogin()
        fun wildDogSuccess()
        fun wildDogFailure(error: String)
    }

    interface MainPresenter {
        fun checkLogin()
        fun loginWildDog()
    }
}