package cn.legendream.wawa.app.net

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/17
 * E-Mail: zhaoxiyuan@kokozu.net
 */
class SignInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        chain?.let {
            val request = chain.request()
            val url = request.url()
            val sign = Utils.sign(url)
            val newRequest = request.newBuilder().url(url.newBuilder().addQueryParameter("sign", sign).build()).build()
            return chain.proceed(newRequest)
        } ?: kotlin.run {
            throw IllegalStateException("chain is null")
        }
    }
}