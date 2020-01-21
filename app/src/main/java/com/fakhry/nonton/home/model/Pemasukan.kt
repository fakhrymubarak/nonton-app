package com.fakhry.nonton.home.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//UNTUK MENGAMBIL FILM DARI DATA BASE
@Parcelize
data class Pemasukan (
    var cashback: String ?="",
    var topup: String ?=""
): Parcelable