package cn.legendream.wawa.app.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by zhao on 2017/11/8.
 */


data class Order(
    var id: String = "", //918acb274da244a99158211d64f13edc
    var userId: Int = 0, //2181
    var userName: String = "", //西瓜
    var commodityId: Int = 0, //null
    var commodityName: String = "", //null
    var machineId: Int = 0, //2
    var machineName: String = "", //大连 1
    var userAdressId: Int = 0, //null
    var gameMoneyPrice: Int = 0, //0
    var dollName: String = "", //null
    var dollIntroduce: String = "", //null
    var dollImg: String = "", //null
    var gameVideo: String = "", //null
    var status: Int = 0, //1
    var createTime: String = "" //2017-10-25 11:36:31
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(userId)
        parcel.writeString(userName)
        parcel.writeInt(commodityId)
        parcel.writeString(commodityName)
        parcel.writeInt(machineId)
        parcel.writeString(machineName)
        parcel.writeInt(userAdressId)
        parcel.writeInt(gameMoneyPrice)
        parcel.writeString(dollName)
        parcel.writeString(dollIntroduce)
        parcel.writeString(dollImg)
        parcel.writeString(gameVideo)
        parcel.writeInt(status)
        parcel.writeString(createTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
}