package com.fakhry.nonton.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fakhry.nonton.R
import com.fakhry.nonton.sign.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_onboarding_one.*

class OnboardingThreeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_three)

        btn_home.setOnClickListener {
            finishAffinity()

            val intent = Intent(
                this@OnboardingThreeActivity,
                SignInActivity::class.java
            )
            startActivity(intent)
        }
    }
}
