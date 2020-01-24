package com.fakhry.nonton.home.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fakhry.nonton.R
import com.fakhry.nonton.utils.Preferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_top_up.*
import java.util.*

class TopUpActivity : AppCompatActivity() {
    private lateinit var preferences: Preferences

    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mDatabase: DatabaseReference

    lateinit var sAmount: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mFirebaseDatabase = mFirebaseInstance.getReference("Riwayat Transaksi")
        mDatabase = FirebaseDatabase.getInstance().getReference()

        btn_top_up.setOnClickListener{

            //INPUT TOP UP VALUE TO DATABASE
            sAmount = et_amount.text.toString()
            mFirebaseDatabase.child(preferences.getValues("username").toString())
                .child("Pemasukan")
                .child("topup-" + UUID.randomUUID().toString()).setValue("sAmount")
            //INPUT TOP UP VALUE TO DATABASE

        }

        btn_batal.setOnClickListener{
            finish()
        }
    }
}
