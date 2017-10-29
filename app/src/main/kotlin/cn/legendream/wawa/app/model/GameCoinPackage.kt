package cn.legendream.wawa.app.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/28
 * E-Mail: zhaoxiyuan@kokozu.net
 */


data class GameCoinPackage(
    @SerializedName("id") var id: Int = 0, //1
    @SerializedName("packageName") var packageName: String = "", //100 游戏币
    @SerializedName("packageUrl") var packageUrl: String = "", //null
    @SerializedName("price") var price: Int = 0, //10
    @SerializedName("gameMoney") var gameMoney: Int = 0, //100
    @SerializedName("status") var status: Int = 0, //1
    @SerializedName("createTime") var createTime: String = "" //2017-09-11 22:06:18
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(packageName)
        parcel.writeString(packageUrl)
        parcel.writeInt(price)
        parcel.writeInt(gameMoney)
        parcel.writeInt(status)
        parcel.writeString(createTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GameCoinPackage> {
        override fun createFromParcel(parcel: Parcel): GameCoinPackage {
            return GameCoinPackage(parcel)
        }

        override fun newArray(size: Int): Array<GameCoinPackage?> {
            return arrayOfNulls(size)
        }
    }
}