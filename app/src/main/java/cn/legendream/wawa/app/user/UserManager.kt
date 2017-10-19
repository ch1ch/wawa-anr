package cn.legendream.wawa.app.user

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import cn.legendream.wawa.app.model.User
import com.google.gson.Gson

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/19
 * E-Mail: zhaoxiyuan@kokozu.net
 */

class UserManager private constructor() {

    companion object {
        private lateinit var sp: SharedPreferences
        private val gson: Gson by lazy {
            Gson()
        }

        fun init(context: Context) {
            sp = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        }

        fun saveUser(user: User) {
            val userJson = gson.toJson(user)
            val edit = sp.edit()
            edit.putString("user", userJson)
            edit.apply()
        }

        fun getUser(): User? {
            val userJson = sp.getString("user", null)
            if (!TextUtils.isEmpty(userJson)) {
                return gson.fromJson(userJson, User::class.java)
            }
            return null
        }

        fun isLogin(): Boolean {
            return getUser() == null
        }
    }
}