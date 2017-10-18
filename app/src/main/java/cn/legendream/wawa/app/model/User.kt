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

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(nickName)
        writeInt(userLevel)
        writeString(phoneNumber)
        writeInt(userPoint)
        writeString(headUrl)
        writeInt(gender)
        writeInt(gameMoney)
        writeInt(pushUserId)
        writeInt(dollCount)
        writeInt(status)
        writeString(token)
        writeString(createTime)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}
