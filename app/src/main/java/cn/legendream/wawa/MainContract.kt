package cn.legendream.wawa

import cn.legendream.wawa.app.model.User

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/18
 * E-Mail: zhaoxiyuan@kokozu.net
 */

interface MainContract {
    interface MainView {
        fun loginSuccess(user: User)
        fun loginError(error: String)
    }

    interface MainPresenter {
        fun weChatLogin()
    }
}