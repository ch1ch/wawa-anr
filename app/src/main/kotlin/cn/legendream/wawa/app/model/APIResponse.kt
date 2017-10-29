package cn.legendream.wawa.app.model

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/17
 * E-Mail: zhaoxiyuan@kokozu.net
 */
data class APIResponse<T>(
        var code: Int = 0,
        var error: String?,
        var data: T? = null)