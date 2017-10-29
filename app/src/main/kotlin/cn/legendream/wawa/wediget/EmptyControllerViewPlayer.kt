package cn.legendream.wawa.wediget

import android.content.Context
import android.util.AttributeSet
import cn.legendream.wawa.R
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/21
 * E-Mail: zhaoxiyuan@kokozu.net
 */
class EmptyControllerViewPlayer : StandardGSYVideoPlayer {
    constructor(context: Context) : super(context)
    constructor(context: Context, fullFlag: Boolean) : super(context, fullFlag)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    init {

    }
    override fun getLayoutId(): Int {
        return R.layout.empty_control_video
    }

    override fun touchSurfaceMoveFullLogic(absDeltaX: Float, absDeltaY: Float) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY)
        mChangePosition = false
        mChangeVolume = false
        mBrightness = false
    }

    override fun touchDoubleUp() {
    }


    override fun prepareVideo() {
        super.prepareVideo()
    }

    override fun setRotation(rotation: Float) {
        super.setRotation(rotation)
    }


    override fun onInfo(what: Int, extra: Int) {
        super.onInfo(what, extra)
    }

}