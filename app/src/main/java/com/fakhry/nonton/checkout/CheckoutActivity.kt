package com.fakhry.nonton.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhry.nonton.R
import com.fakhry.nonton.checkout.adapter.CheckoutAdapter
import com.fakhry.nonton.checkout.model.Checkout
import com.fakhry.nonton.home.model.Film
import com.fakhry.nonton.utils.Preferences
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.popup_alert.*
import kotlinx.android.synthetic.main.popup_alert.view.*
import java.text.NumberFormat
import java.util.*

import kotlin.collections.ArrayList

class CheckoutActivity : AppCompatActivity() {

    private var dataList = ArrayList<Checkout>()
    private var total: Int = 0

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        val data = intent.getParcelableExtra<Film>("data2")

        preferences = Preferences(this)
        dataList = intent.getSerializableExtra("data1") as ArrayList<Checkout>

        // MENGHITUNG TOTAL HARGA SETELAH MEMILIH BANGKU
        for (a in dataList.indices) {
            total += dataList[a].harga!!.toInt()
        }

        var saldo = preferences.getValues("saldo")


        dataList.add(Checkout("Total Harus Dibayar", total.toString()))

        btn_tiket.setOnClickListener {
            if (saldo!!.toInt() >= total) {
                //Inflate the dialog with custom view
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.popup_alert, null)
                //AlertDialogBuilder
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                //show dialog
                val mAlertDialog = mBuilder.show()

                mAlertDialog.tv_alert_msg.text = "Kamu akan membeli tiket " + data.judul + "?"

                mDialogView.btn_yes.setOnClickListener {
                    finishAffinity()

                    val intent = Intent(
                        this@CheckoutActivity,
                        CheckoutSuccessActivity::class.java
                    ).putExtra("data", data)
                    startActivity(intent)

                    mAlertDialog.dismiss()
                }
                mDialogView.btn_no.setOnClickListener {
                    mAlertDialog.dismiss()
                }
            } else {
                //STILL CAN DEVELOP, POP ALERT THAT CAN INTENT TO TOP UP ACTIVITY
                Toast.makeText(
                    this@CheckoutActivity,
                    "Saldomu tidak cukup, silakan top up terlebih dahulu",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        iv_back.setOnClickListener {
            finish()
        }

        rc_checkout.layoutManager = LinearLayoutManager(this)
        rc_checkout.adapter = CheckoutAdapter(dataList) {
        }

        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        tv_saldo.setText(formatRupiah.format(preferences.getValues("saldo")!!.toDouble()))
    }
}
