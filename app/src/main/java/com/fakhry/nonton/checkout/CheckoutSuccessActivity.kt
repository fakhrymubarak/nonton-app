package com.fakhry.nonton.checkout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fakhry.nonton.R
import com.fakhry.nonton.home.HomeActivity
import com.fakhry.nonton.home.model.Film
import com.fakhry.nonton.home.tiket.TiketActivity
import kotlinx.android.synthetic.main.activity_checkout_success.*


class CheckoutSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_success)

        val data = intent.getParcelableExtra<Film>("data")

        tv_checkout.text = "Tiket telah berhasil dibeli\nSelamat menonton " + data.judul + "!!"

        btn_tiket.setOnClickListener {
            finishAffinity()

            val mIntent = Intent(this, TiketActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("data", data)
            mIntent.putExtras(bundle)
            startActivity(mIntent)

        }


        btn_home.setOnClickListener {
            finishAffinity()
            val intent = Intent(
                this,
                HomeActivity::class.java
            )
            startActivity(intent)
        }
    }
}
