package com.bestoffers.enjoeepharmacy.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bestoffers.enjoeepharmacy.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initView()

    }

    private fun initView() {
        rl_terms_of_use.setOnClickListener {
            startActivity(Intent(this, TermsOfUseActivity::class.java))
        }
        rl_faq.setOnClickListener {
            startActivity(Intent(this, FaqsActivity::class.java))
        }
        rl_privacy_policy.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }
    }
}