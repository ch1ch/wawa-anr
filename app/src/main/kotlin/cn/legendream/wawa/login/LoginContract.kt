package cn.legendream.wawa.login

import cn.legendream.wawa.app.model.User

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/18
 * E-Mail: zhaoxiyuan@kokozu.net
 */

interface LoginContract {
    interface loginView {
        fun loginSuccess(user: User)
        fun loginError(error: String)
    }

    interface LoginPresenter {
        fun weChatLogin()
        fun destroy()
    }
}