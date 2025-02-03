package com.resieasy.rezirent.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.resieasy.rezirent.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = FirebaseAuth.getInstance()

        lifecycleScope.launch {
            delay(1000)
            navigateToNextScreen()
        }
    }

    private fun navigateToNextScreen() {
        val intent = if (auth.currentUser != null) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, SignInActivity::class.java).apply {
                putExtra("onlysignin", "1234")
            }
        }
        startActivity(intent)
        finishAffinity()
    }
}
