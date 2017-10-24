package cn.legendream.wawa.app.net

/**
 * Created by zhao on 2017/10/24.
 */


enum class NetServiceCode(val code: Int, val message: String) {
    NORMAL(200, "正常"),
    PARAMS_ERROR(1001, "参数错误"),
    SIGN_ERROR(1002, "签名错误"),
    WX_USER_NULL(2001, "微信没有找到此用户"),
    USER_GOME_MONEY_NULL(2002, "余额不足，请充值后再试"),
    MACHINE_USED(3001, "娃娃机游戏中，稍后再试"),
    CREATE_ORDER_ERROR(4001, "创建订单失败"),
    BACK_ORDER_ERROR(4002, "回调订单失败"),
    PACKAGE_ERROR(4003, "无效的充值套餐"),
    PUT_USER_LINE(4004, "已经加入当前机器排位，前位玩家结束后再游戏下单"),
    LTS_ERROR(5001, "LTS错误");
}