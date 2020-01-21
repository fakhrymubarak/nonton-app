package com.fakhry.nonton.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fakhry.nonton.R
import com.fakhry.nonton.sign.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_onboarding_one.*

class OnboardingTwoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_two)

        btn_home.setOnClickListener {
            val intent = Intent(
                this@OnboardingTwoActivity,
                OnboardingThreeActivity::class.java
            )
            startActivity(intent)
        }
        btn_daftar.setOnClickListener {
            finishAffinity()

            val intent = Intent(
                this@OnboardingTwoActivity,
                SignInActivity::class.java
            )
            startActivity(intent)
        }
    }
}
