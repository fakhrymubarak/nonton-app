package com.fakhry.nonton.home.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//UNTUK MENGAMBIL FILM DARI DATA BASE
@Parcelize
data class RiwayatTransaksi (
    var user: String ?="",
    var pemasukan: String ?="",
    var pengeluaran: String ?=""
): Parcelable