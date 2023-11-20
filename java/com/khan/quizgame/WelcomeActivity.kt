package com.khan.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.khan.quizgame.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    lateinit var WelcomeActivityBinding:ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        WelcomeActivityBinding=ActivityWelcomeBinding.inflate(layoutInflater)
        val view = WelcomeActivityBinding.root
        setContentView(view)

        val alphaAnimation = AnimationUtils.loadAnimation(applicationContext,R.anim.splash_anim)
        WelcomeActivityBinding.textViewSplash.startAnimation(alphaAnimation)

        val handlers=Handler(Looper.getMainLooper())
        handlers.postDelayed(object :Runnable{
            override fun run() {
                val intent =Intent(this@WelcomeActivity,LoginActivity   ::class.java)
                startActivity(intent)
                finish()
            }
        },5000)
    }
}