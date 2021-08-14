package co.com.ceiba.mobile.pruebadeingreso.splash

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import co.com.ceiba.mobile.pruebadeingreso.R
import co.com.ceiba.mobile.pruebadeingreso.databinding.ActivitySplashBinding
import co.com.ceiba.mobile.pruebadeingreso.father.FatherActivity

class SplashActivity : AppCompatActivity(){

    lateinit var bindingSplash: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSplash = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        this.supportActionBar?.hide()
        iniciarAnimacion()
    }

    fun iniciarAnimacion(){
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                var startIntent = Intent(applicationContext, FatherActivity::class.java)
                startActivity(startIntent)
            }
        })
        bindingSplash.splashContainer.startAnimation(animation)
    }
}