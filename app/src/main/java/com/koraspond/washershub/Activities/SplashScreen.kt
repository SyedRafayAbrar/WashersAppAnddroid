package com.koraspond.washershub.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.koraspond.washershub.R
import com.koraspond.washershub.Utils.UserInfoPreference

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (!UserInfoPreference(this).getStr("role").equals("DNF")) {
            if (UserInfoPreference(this).getStr("role").equals("c")) {
                val inetnt = Intent(this, HomeActivity::class.java)
                startActivity(inetnt)
                finish()
            } else {
                var intent = Intent(this, VendorHome::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            var intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}