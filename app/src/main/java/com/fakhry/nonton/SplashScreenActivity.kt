package com.fakhry.nonton

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.fakhry.nonton.onboarding.OnboardingOneActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var handler = Handler()
        handler.postDelayed({
            val intent = Intent (this@SplashScreenActivity,
                OnboardingOneActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
