package com.koraspond.washershub.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.koraspond.washershub.Fragments.LoginFragment
import com.koraspond.washershub.Fragments.SignupFragment
import com.koraspond.washershub.R
import com.koraspond.washershub.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

lateinit var binding:ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_welcome)
        binding.login.setOnClickListener {
           supportFragmentManager.
            beginTransaction().setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .addToBackStack(null) .replace(R.id.root, LoginFragment()).commit()
        }


        binding.signupTv.setOnClickListener {
            supportFragmentManager.
            beginTransaction().setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .addToBackStack(null) .replace(R.id.root, SignupFragment()).commit()
        }
    }
}