package com.fakhry.nonton.checkout

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhry.nonton.R
import com.fakhry.nonton.checkout.adapter.CheckoutAdapter
import com.fakhry.nonton.checkout.model.Checkout
import com.fakhry.nonton.home.model.Film
import com.fakhry.nonton.home.tiket.TiketActivity
import com.fakhry.nonton.utils.Preferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.popup_alert.*
import kotlinx.android.synthetic.main.popup_alert.view.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

import kotlin.collections.ArrayList

class CheckoutActivity : AppCompatActivity() {

    private var dataList = ArrayList<Checkout>()
    private var total: Int = 0

    private lateinit var preferences: Preferences
    private lateinit var mDatabase: DatabaseReference

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val data = intent.getParcelableExtra<Film>("data2")
        @Suppress("UNCHECKED_CAST")
        dataList = intent.getSerializableExtra("data1") as ArrayList<Checkout>

        preferences = Preferences(this)
        mDatabase = FirebaseDatabase.getInstance().getReference("User")

        val saldo = preferences.getValues("saldo")

        for (a in dataList.indices) {
            total += dataList[a].harga!!.toInt()
        }

        dataList.add(Checkout("Total Harus Dibayar", total.toString()))

        if (saldo!!.toInt() >= total) {
            tv_warning.visibility = View.INVISIBLE
            btn_tiket.visibility = View.VISIBLE

            btn_tiket.setOnClickListener {
                //Inflate the dialog with custom view
                val mDialogView = LayoutInflater.from(this@CheckoutActivity).inflate(R.layout.popup_alert, null)
                //AlertDialogBuilder
                val mBuilder = AlertDialog.Builder(this@CheckoutActivity)
                    .setView(mDialogView)
                //show dialog
                val mAlertDialog = mBuilder.show()

                mAlertDialog.tv_alert_title.text = "Beli Tiket?"
                mAlertDialog.tv_alert_msg.text = "Kamu akan membeli tiket " + data!!.judul + "?"

                mDialogView.btn_yes.setOnClickListener {
                    val currentTimeId: String = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault()).format(Date())
                    val tiketId : String = "tiket-"+ currentTimeId + "-" +UUID.randomUUID()
                    val saldoTersisa : Int = saldo.toInt() - total

                    val setCurrentTime : String =  SimpleDateFormat("dd MMMM yyyy,  HH:mm:ss", Locale.getDefault()).format(Date())

                    val mDatabaseCredit  = mDatabase.child(preferences.getValues("username").toString())
                        .child("credit")
                        .child(tiketId)

                    mDatabaseCredit.child("id")
                        .setValue(tiketId)

                    mDatabaseCredit.child("price")
                        .setValue(total.toString())

                    mDatabaseCredit.child("detail")
                        .setValue("Tiket : " + data.judul)

                    mDatabaseCredit.child("date")
                        .setValue(setCurrentTime)

                    mDatabase.child(preferences.getValues("username").toString())
                        .child("saldo")
                        .setValue(saldoTersisa.toString())

                    preferences.setValues("saldo", saldoTersisa.toString())

                    showNotif(data)
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
            }
        }

        btn_home.setOnClickListener {
            finish()

        }
        iv_back.setOnClickListener {
            finish()
        }

        rc_checkout.layoutManager = LinearLayoutManager(this)
        rc_checkout.adapter = CheckoutAdapter(dataList) {
        }

        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        @Suppress("UsePropertyAccessSyntax")
        tv_saldo.setText(formatRupiah.format(preferences.getValues("saldo")!!.toDouble()))
    }

    private fun showNotif(data : Film) {
        @Suppress("LocalVariableName")
        val NOTIFICATION_CHANNEL_ID = "channel_bwa_notif"
        val context = this.applicationContext
        var notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelName = "BWAMOV Notif Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }

        val mIntent = Intent(this, TiketActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("data", data)
        mIntent.putExtras(bundle)

        val pendingIntent =
            PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        builder.setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.ic_launcher
                )
            )
            .setTicker("notif bwa starting")
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setLights(Color.RED, 3000, 3000)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setContentTitle("Pembayaran Berhasil!")
            .setContentText("Tiket telah berhasil dibeli. Selamat menonton " + data.judul + "!!")

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(115, builder.build())
    }
}
