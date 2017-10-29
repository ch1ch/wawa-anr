package cn.legendream.wawa.dolldetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.legendream.wawa.R
import cn.legendream.wawa.app.contract.ExtraKey
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_doll_detail.*

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/26
 * E-Mail: zhaoxiyuan@kokozu.net
 */

class DollDetailActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doll_detail)
        val dollDetailUrl = intent.getStringExtra(ExtraKey.EXTRA_DOLL_IMAGE_URL)
        dollDetailUrl?.let {
            Glide.with(this).load(dollDetailUrl).into(iv_doll_detail)
        }
    }
}
