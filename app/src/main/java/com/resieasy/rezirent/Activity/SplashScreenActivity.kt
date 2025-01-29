package com.resieasy.rezirent.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.resieasy.rezirent.R

class SplashScreenActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        auth = FirebaseAuth.getInstance()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ }, 100)


        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    if (auth!!.currentUser != null) {
                        val intent1 = Intent(
                            this@SplashScreenActivity,
                            MainActivity::class.java
                        )
                        startActivity(intent1)
                        finishAffinity()
                    } else {
                        val intent11 = Intent(
                            this@SplashScreenActivity,
                            SignInActivity::class.java
                        )
                        intent11.putExtra("onlysignin", "1234")
                        startActivity(intent11)
                        finishAffinity()
                    }
                }
            }
        }
        thread.start()
    }
}