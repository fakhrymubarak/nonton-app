package com.fakhry.nonton.home.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//UNTUK MENAMPILKAN SIAPA YANG BERMAIN

@Parcelize
data class Plays (
    var nama: String ?="",
    var url: String ?=""
): Parcelable