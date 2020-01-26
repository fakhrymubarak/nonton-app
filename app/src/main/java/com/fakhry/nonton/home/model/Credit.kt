package com.fakhry.nonton.home.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//UNTUK MENGAMBIL FILM DARI DATA BASE
@Parcelize
data class Credit (
    var detail: String ?= "",
    var id: String ?="",
    var price: String ?="",
    var date : String ?=""
): Parcelable