package cn.legendream.wawa.start

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.legendream.wawa.MainActivity
import cn.legendream.wawa.R
import kotlinx.android.synthetic.main.activity_start.*

/**
 * Author: ZhaoXiyuan
 * Date: 2017/11/11
 * E-Mail: zhaoxiyuan@kokozu.net
 */

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        iv_start.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3_000)
    }
}