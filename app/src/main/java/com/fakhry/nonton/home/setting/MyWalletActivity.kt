package com.fakhry.nonton.home.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fakhry.nonton.R
import kotlinx.android.synthetic.main.activity_pilih_bangku.*

class MyWalletActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)

        iv_back.setOnClickListener {
            finish()
        }
    }
}
