package com.android.searchimage.views.ui.fragment.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.android.searchimage.databinding.ActivitySplashBinding
import com.android.searchimage.views.ui.activities.MainActivity
import com.mikhaellopez.rxanimation.RxAnimation
import com.mikhaellopez.rxanimation.fadeIn
import com.mikhaellopez.rxanimation.resize

class Splash : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val animationTimeOut: Long = 2000
    private val delay: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        changeActivity(MainActivity::class.java)
    }

    private fun changeActivity(activityClass: Class<*>) {

        RxAnimation.together(
            binding.imgLogo.fadeIn(animationTimeOut),
            binding.imgLogo.resize(100, 100, animationTimeOut)
        ).subscribe()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            startActivity(Intent(this@Splash, activityClass))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, delay)
    }

}