package com.fakhry.nonton.home.setting

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import com.fakhry.nonton.R
import com.fakhry.nonton.home.tiket.TiketActivity
import com.fakhry.nonton.utils.Preferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_top_up.*
import kotlinx.android.synthetic.main.popup_alert.*
import kotlinx.android.synthetic.main.popup_alert.view.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TopUpActivity : AppCompatActivity() {
    private lateinit var preferences: Preferences
    private lateinit var mDatabase: DatabaseReference

    lateinit var sAmount: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)

        preferences = Preferences(applicationContext)
        mDatabase = FirebaseDatabase.getInstance().getReference("User")

        btn_top_up.setOnClickListener{
            sAmount = et_amount.text.toString()
            alertDialog(sAmount)
        }

        iv_back.setOnClickListener{
            finish()
        }
        btn_batal.setOnClickListener{
            finish()
        }
    }

    private fun showNotif() {
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

//        val mIntent = Intent(this, CheckoutSuccessActivity::class.java)
//        val bundle = Bundle()
//        bundle.putString("id", "id_film")
//        mIntent.putExtras(bundle)

        val mIntent = Intent(this, TiketActivity::class.java)
        val bundle = Bundle()
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
            .setContentTitle("Top Up Berhasil Dilakukan!")
            .setContentText("Top up sebanyak " + currency(sAmount.toDouble()) + " berhasil dilakukan!")

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(115, builder.build())
    }

    private fun alertDialog(amount : String) {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this@TopUpActivity).inflate(R.layout.popup_alert, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this@TopUpActivity)
            .setView(mDialogView)
        //show dialog
        val mAlertDialog = mBuilder.show()


        mAlertDialog.tv_alert_title.text = "Top Up?"
        mAlertDialog.tv_alert_msg.text = "Kamu akan top up sebesar " + currency(amount.toDouble()) + "?"

        mDialogView.btn_yes.setOnClickListener {
            val saldo = preferences.getValues("saldo")
            val saldoTersisa : Int = saldo!!.toInt() + amount.toInt()
            val currentTimeId: String = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault()).format(Date())
            val topUpId : String = "topup-"+ currentTimeId + "-" +UUID.randomUUID()

            val setCurrentTime : String =  SimpleDateFormat("dd MMMM yyyy,  HH:mm:ss", Locale.getDefault()).format(Date())

            val mDatabaseDebit = mDatabase.child(preferences.getValues("username").toString())
                .child("debit")
                .child(topUpId)

            mDatabaseDebit.child("id")
                .setValue(topUpId)

            mDatabaseDebit.child("amount")
                .setValue(amount)

            mDatabaseDebit.child("date")
                .setValue(setCurrentTime)

            mDatabaseDebit.child("detail")
                .setValue("Top Up")

            mDatabase.child(preferences.getValues("username").toString())
                .child("saldo")
                .setValue(saldoTersisa.toString())

            preferences.setValues("saldo", saldoTersisa.toString())

            Toast.makeText(this, "Top Up Berhasil Dilakukan", Toast.LENGTH_SHORT).show()

            showNotif()

            finish()
            mAlertDialog.dismiss()
        }
        mDialogView.btn_no.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun currency(amount: Double) : String {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(amount)
    }
}
