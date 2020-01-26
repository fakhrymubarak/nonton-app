package com.fakhry.nonton.home.dashboard


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fakhry.nonton.DetailActivity
import com.fakhry.nonton.R
import com.fakhry.nonton.home.model.Film
import com.fakhry.nonton.home.setting.SettingFragment
import com.fakhry.nonton.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.NumberFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

    private lateinit var preferences: Preferences
    lateinit var mDatabase: DatabaseReference

    private var dataList = ArrayList<Film>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(activity!!.applicationContext)
        mDatabase = FirebaseDatabase.getInstance().getReference("Film")

        val profilePict = preferences.getValues("url")

        tv_nama.setText(preferences.getValues("nama"))
        if (!preferences.getValues("saldo").equals("")) {
            currency(preferences.getValues("saldo")!!.toDouble(), tv_saldo)
        }

        //START - CONDITION FOR SETTING PROFILE PICTURE
        if(profilePict == ""){
            iv_profile.setImageResource(R.drawable.user_pic)
        }else{
            Glide.with(this)
                .load(profilePict)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)
        }
        //END - CONDITION FOR SETTING PROFILE PICTURE
        iv_profile.setOnClickListener {
            //I am planning this to move into setting fragment
        }

        rv_now_playing.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_coming_soon.layoutManager = LinearLayoutManager(context!!.applicationContext)

        getData()
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataList.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()) {
                    val film = getdataSnapshot.getValue(Film::class.java!!)
                    dataList.add(film!!)
                }

                rv_now_playing.adapter = NowPlayingAdapter(dataList) {
                    val intent = Intent(
                        context,
                        DetailActivity::class.java
                    ).putExtra("data", it)
                    startActivity(intent)
                }

                rv_coming_soon.adapter = ComingSoonAdapter(dataList) {
                    val intent = Intent(
                        context,
                        DetailActivity::class.java
                    ).putExtra("data", it)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun currency(amount: Double, textView: TextView) {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        textView.setText(formatRupiah.format(amount))
    }
}
