package cn.legendream.wawa.app.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/22
 * E-Mail: zhaoxiyuan@kokozu.net
 */

data class Machine(
        @SerializedName("id") var id: Int?, //1
        @SerializedName("machineName") var machineName: String?, //小叮当
        @SerializedName("introduce") var introduce: String?, //好吧，这个名字，所以，都是小叮当
        @SerializedName("dollName") var dollName: String?, //null
        @SerializedName("dollIntroduce") var dollIntroduce: String?, //null
        @SerializedName("dollImg") var dollImg: String?, //null
        @SerializedName("ipAddress") var ipAddress: String?, //47.94.236.45:9001
        @SerializedName("machineImg") var machineImg: String?, //null
        @SerializedName("video1") var video1: String?, //null
        @SerializedName("video2") var video2: String?, //null
        @SerializedName("video3") var video3: String?, //null
        @SerializedName("gameMoney") var gameMoney: Int?, //20
        @SerializedName("commodityIds") var commodityIds: String?, //null
        @SerializedName("status") var status: Int? //2
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(machineName)
        parcel.writeString(introduce)
        parcel.writeString(dollName)
        parcel.writeString(dollIntroduce)
        parcel.writeString(dollImg)
        parcel.writeString(ipAddress)
        parcel.writeString(machineImg)
        parcel.writeString(video1)
        parcel.writeString(video2)
        parcel.writeString(video3)
        parcel.writeValue(gameMoney)
        parcel.writeString(commodityIds)
        parcel.writeValue(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Machine> {
        override fun createFromParcel(parcel: Parcel): Machine {
            return Machine(parcel)
        }

        override fun newArray(size: Int): Array<Machine?> {
            return arrayOfNulls(size)
        }
    }
}