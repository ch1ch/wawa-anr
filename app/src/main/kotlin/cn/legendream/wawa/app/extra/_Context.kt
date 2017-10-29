package cn.legendream.wawa.app.extra

import android.content.Context
import android.widget.Toast
import javax.inject.Inject

/**
 * Created by zhao on 2017/10/24.
 */

/**
 * @param  message for display
 * @param  duration for display time. [duration] only use [Toast.LENGTH_LONG] or [Toast.LENGTH_SHORT]
 */
fun Context.toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, message.toString(), duration).show()
}
