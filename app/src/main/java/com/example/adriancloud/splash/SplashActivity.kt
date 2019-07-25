package com.example.adriancloud.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import com.example.adriancloud.R
import com.example.adriancloud.login.LoginActivity
import kotlin.concurrent.thread

class SplashActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions


        val logo = findViewById<ImageView>(R.id.splash_ImageView_icon)
        val transicion = AnimationUtils.loadAnimation(this, R.anim.transicion)
        logo.startAnimation(transicion)

        transicion.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                thread (start = true){
                    Thread.sleep(750)
                    siguienteActivity()
                }

            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }
    fun siguienteActivity() {
        val intento = Intent(this, LoginActivity::class.java) // Lanzamos SiguienteActivity
        startActivity(intento)
        finish() //Finalizamos este activity
    }
}
