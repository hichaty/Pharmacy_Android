package com.bestoffers.enjoeepharmacy.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.R
import com.bestoffers.enjoeepharmacy.SessionManager
import com.bestoffers.enjoeepharmacy.Utils.SharedPreferenceUtility

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        showSplash()

    }

    private fun showSplash() {

//        Log.e("loggedin",
//            SharedPreferenceUtility.getInstance()[AppConstants.PREF_IS_LOGGED_IN, false].toString()
//        )

        Handler().postDelayed({
            if (SharedPreferenceUtility.getInstance()[AppConstants.PREF_IS_LOGGED_IN, false]) {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }, 3000)

    }
}