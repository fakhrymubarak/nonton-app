package com.fakhry.nonton.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fakhry.nonton.R
import com.fakhry.nonton.sign.signin.SignInActivity
import com.fakhry.nonton.utils.Preferences
import kotlinx.android.synthetic.main.activity_onboarding_one.*

class OnboardingOneActivity : AppCompatActivity() {

    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        preferences = Preferences(this)

        if (preferences.getValues("onboarding").equals("1")) {
            finishAffinity()

            val intent = Intent(this@OnboardingOneActivity,
                SignInActivity::class.java)
            startActivity(intent)
        }

        btn_home.setOnClickListener {
            val intent = Intent(
                this@OnboardingOneActivity,
                OnboardingTwoActivity::class.java
            )
            startActivity(intent)
        }
        btn_daftar.setOnClickListener {
            //ketika menekan tombol daftar, maka riwayat activity yang ini tidak tersimpan(langsung ter close)
            finishAffinity()

            val intent = Intent(
                this@OnboardingOneActivity,
                SignInActivity::class.java
            )
            startActivity(intent)
        }
    }
}