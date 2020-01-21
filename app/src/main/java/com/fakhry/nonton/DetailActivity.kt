package com.fakhry.nonton

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fakhry.nonton.checkout.PilihBangkuActivity
import com.fakhry.nonton.home.dashboard.PlaysAdapter
import com.fakhry.nonton.home.model.Film
import com.fakhry.nonton.home.model.Plays
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    private var dataList = ArrayList<Plays>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val data = intent.getParcelableExtra<Film>("data")

        mDatabase = FirebaseDatabase.getInstance().getReference("Film")
            .child(data.judul.toString())
            .child("play")

        tv_judul.text = data.judul
        tv_waktu.text = data.genre
        tv_desc.text = data.desc
        tv_rating.text = data.rating

        Glide.with(this)
            .load(data.poster)
            .into(iv_poster)

        //START - PINDAH DARI DETAIL KE MENU PILIH BANGKU
        btn_pilih_bangku.setOnClickListener {
            val intent = Intent(
                this@DetailActivity,
                //MEMBAWA DATA JUDUL KE PILIH BANGKU
                PilihBangkuActivity::class.java).putExtra("data", data)
            startActivity(intent)
        }

        iv_back.setOnClickListener {
            finish()
        }

        rv_who_play.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        getData()
    }
    //END - PINDAH DARI DETAIL KE MENU PILIH BANGKU

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataList.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()) {

                    val film = getdataSnapshot.getValue(Plays::class.java!!)
                    dataList.add(film!!)
                }

                rv_who_play.adapter = PlaysAdapter(dataList) {
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailActivity, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}

