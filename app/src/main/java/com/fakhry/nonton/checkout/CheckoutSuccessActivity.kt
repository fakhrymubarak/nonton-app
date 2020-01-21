package com.fakhry.nonton.checkout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fakhry.nonton.R
import com.fakhry.nonton.home.HomeActivity
import com.fakhry.nonton.home.model.Film
import kotlinx.android.synthetic.main.activity_checkout_success.*


class CheckoutSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_success)

        val data = intent.getParcelableExtra<Film>("data")

        tv_checkout.text = "Tiket telah berhasil dibeli\nSelamat menonton " + data.judul + "!!"

        btn_tiket.setOnClickListener{
            finishAffinity()
//            val newFragment = Fragment()
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragment_tiket, newFragment)
//            transaction.addToBackStack(null)
//            transaction.commit()

//            val intent = Intent(this,
//                TiketFragment::class.java)
//            startActivity(intent)
        }


        btn_home.setOnClickListener {
            finishAffinity()
            val intent = Intent(this,
                HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
