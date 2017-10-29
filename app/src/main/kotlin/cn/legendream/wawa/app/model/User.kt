package cn.legendream.wawa.app.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/17
 * E-Mail: zhaoxiyuan@kokozu.net
 */

/**
 * id : 1
 * nickName : hexu
 * userLevel : 1
 * phoneNumber : 18610012053
 * userPoint : 1
 * headUrl : null
 * gender : null
 * gameMoney : 900
 * pushUserId : null
 * dollCount : null
 * status : 1
 * token : b76dfb80050e4005b646317f3fd938e1-1
 * createTime : 2017-09-09 23:13:36
 */

data class User(
    var id: Int = 0,
    var nickName: String? = null,
    var userLevel: Int = 0,
    var phoneNumber: String? = null,
    var userPoint: Int = 0,
    var headUrl: String? = null,
    var gender: Int = 0,
    var gameMoney: Int = 0,
    var pushUserId: Int = 0,
    var dollCount: Int = 0,
    var status: Int = 0,
    var token: String? = null,
    var createTime: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nickName)
        parcel.writeInt(userLevel)
        parcel.writeString(phoneNumber)
        parcel.writeInt(userPoint)
        parcel.writeString(headUrl)
        parcel.writeInt(gender)
        parcel.writeInt(gameMoney)
        parcel.writeInt(pushUserId)
        parcel.writeInt(dollCount)
        parcel.writeInt(status)
        parcel.writeString(token)
        parcel.writeString(createTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}

