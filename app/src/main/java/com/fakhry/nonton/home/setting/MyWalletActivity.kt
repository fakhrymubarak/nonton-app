package com.fakhry.nonton.home.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhry.nonton.R
import com.fakhry.nonton.home.model.Debit
import com.fakhry.nonton.home.model.Credit
import com.fakhry.nonton.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_my_wallet.*
import java.text.NumberFormat
import java.util.*

class MyWalletActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences

    private lateinit var mDatabaseDebit: DatabaseReference
    private lateinit var mDatabaseKredit: DatabaseReference

    private var dataListDebit = ArrayList<Debit>()
    private var dataListCredit = ArrayList<Credit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)

        preferences = Preferences(applicationContext)

        mDatabaseDebit = FirebaseDatabase.getInstance().getReference("Riwayat Transaksi")
            .child(preferences.getValues("username").toString())
            .child("Pemasukan")
        mDatabaseKredit = FirebaseDatabase.getInstance().getReference("Riwayat Transaksi")
            .child(preferences.getValues("username").toString())
            .child("Pengeluaran")


        rv_transaction_debit.layoutManager =
            LinearLayoutManager(applicationContext)
        rv_transaction_kredit.layoutManager =
            LinearLayoutManager(applicationContext)
        getData()

        //SHOW SALDO
        preferences = Preferences(applicationContext)
        if (!preferences.getValues("saldo").equals("")) {
            currency(preferences.getValues("saldo")!!.toDouble(), tv_saldo)
        }
        //SHOW SALDO


        iv_back.setOnClickListener {
            finish()
        }
        btn_top_up.setOnClickListener{
            val intent = Intent(
                this@MyWalletActivity,
                TopUpActivity::class.java
            )
            startActivity(intent)
        }
    }

    private fun currency(harga: Double, textView: TextView) {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        textView.setText(formatRupiah.format(harga))

    }

    private fun getData() {
        mDatabaseDebit.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataListDebit.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()) {

                    val pemasukan = getdataSnapshot.getValue(Debit::class.java)
                    dataListDebit.add(pemasukan!!)
                }

                rv_transaction_debit.adapter = DebitAdapter(dataListDebit) {
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MyWalletActivity, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })
        mDatabaseKredit.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataListCredit.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()) {

                    val pengeluaran = getdataSnapshot.getValue(Credit::class.java)
                    dataListCredit.add(pengeluaran!!)
                }


                rv_transaction_kredit.adapter = CreditAdapter(dataListCredit) {
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MyWalletActivity, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}
