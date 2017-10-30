package cn.legendream.wawa.app.net

import okhttp3.HttpUrl
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/17
 * E-Mail: zhaoxiyuan@kokozu.net
 */

private val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy#MM#ddHH:mm", Locale.getDefault())

class Utils {
    companion object {
        fun sign(url: HttpUrl): String {
            val param = StringBuilder()
            url.queryParameterNames().sorted().forEach {
                param.append(it.toUpperCase())
            }
            return "$param${simpleDateFormat.format(Date(System.currentTimeMillis()))}"
        }
    }
}

