package com.fakhry.nonton.home.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//UNTUK MENGAMBIL FILM DARI DATA BASE
@Parcelize
data class Debit (
    var detail: String ?= "",
    var amount: String ?="",
    var id: String ?="",
    var date : String ?=""
): Parcelable