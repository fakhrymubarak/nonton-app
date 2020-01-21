package com.fakhry.nonton.home.tiket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhry.nonton.R
import com.fakhry.nonton.checkout.model.Checkout
import com.fakhry.nonton.home.dashboard.ComingSoonAdapter
import com.fakhry.nonton.home.model.Film
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_history.iv_back
import kotlinx.android.synthetic.main.fragment_tiket.*

class HistoryActivity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference

    private var dataList = ArrayList<Film>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        mDatabase = FirebaseDatabase.getInstance().getReference("Film")

        dataList = intent.getSerializableExtra("data") as ArrayList<Film>


        rc_tiket_history.layoutManager = LinearLayoutManager(applicationContext)
        getData()

        iv_back.setOnClickListener {
            finish()
        }

    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataList.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()) {

                    val film = getdataSnapshot.getValue(Film::class.java!!)
                    dataList.add(film!!)
                }

                rc_tiket_history.adapter = ComingSoonAdapter(dataList) {
                    val intent = Intent(
                        this@HistoryActivity,
                        TiketActivity::class.java
                    ).putExtra("data", it)
                    startActivity(intent)
                }

//                tv_total.setText(dataList.size.toString() +" Movies")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HistoryActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}
