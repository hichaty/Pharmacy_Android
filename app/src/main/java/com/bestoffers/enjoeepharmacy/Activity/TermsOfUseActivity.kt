package com.bestoffers.enjoeepharmacy.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.R
import kotlinx.android.synthetic.main.activity_terms_of_use.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class TermsOfUseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_use)

        initView()
        webData()

    }

    private fun webData() {
        webView.webViewClient = WebViewClient()
        webView.loadUrl(AppConstants.BASE_DOMAIN+"/terms-of-use")
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
    }

    private fun initView() {
        toolbarTitle.setText("Terms Of Use")
        iv_back.visibility= View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        iv_hamburger.visibility=View.GONE
        iv_cart.visibility=View.GONE

    }
}