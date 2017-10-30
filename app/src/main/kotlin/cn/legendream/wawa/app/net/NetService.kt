package cn.legendream.wawa.app.net

import cn.legendream.wawa.BuildConfig
import cn.legendream.wawa.app.AppInfo
import cn.legendream.wawa.app.model.APIResponse
import cn.legendream.wawa.app.model.GameCoinPackage
import cn.legendream.wawa.app.model.User
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/17
 * E-Mail: zhaoxiyuan@kokozu.net
 */

interface NetService {

    @GET("user/wxLogin")
    fun wxLogin(@Query("code") code: String): Observable<APIResponse<User>>

    @GET("order/createOrder")
    fun createOrder(@Query("token") token: String, @Query(
        "machineId") int: Int): Observable<APIResponse<Any>>

    @GET
    fun movePawTo(@Url url: String, @Query("action") action: Int, @Query(
        "time") time: Int): Observable<APIResponse<Any>>

    @GET
    fun pawCatch(@Url url: String, @Query("action") action: Int, @Query(
        "time") time: Int): Observable<APIResponse<Any>>

    @GET("order/getRechargePackage")
    fun rechargePackageList(): Observable<APIResponse<List<GameCoinPackage>>>


    @GET("order/recharge")
    fun rechargePayInfo(@Query("token") token: String, @Query(
        "packageId") packageId: String, @Query("payType") payType: Int, @Query(
        "outPayOrder") outPayOrder: String): Observable<APIResponse<String>>


    companion object {
        val INSTANCE: NetService by lazy {
            getNetServiceInstance()
        }


        fun <T> ioToMain(): ObservableTransformer<in APIResponse<T>, out APIResponse<T>> {
            return ObservableTransformer { upstream ->
                upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            }
        }

        private fun getNetServiceInstance(): NetService {
            val okHttp = OkHttpClient.Builder().addInterceptor(SignInterceptor())
            if (BuildConfig.DEBUG) {
                okHttp.addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(AppInfo.BASE_URL).client(okHttp.build())
                .build()
            return retrofit.create(NetService::class.java)
        }
    }

}