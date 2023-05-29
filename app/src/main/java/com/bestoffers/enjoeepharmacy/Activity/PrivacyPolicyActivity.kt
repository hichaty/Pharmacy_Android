package com.bestoffers.enjoeepharmacy.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.R
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class PrivacyPolicyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        initView()
        webData()

    }

    private fun webData() {
        webView.webViewClient = WebViewClient()
        webView.loadUrl(AppConstants.BASE_DOMAIN+"/privacy-policy")
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
    }

    private fun initView() {
        toolbarTitle.setText("Privacy Policy")
        iv_back.visibility= View.VISIBLE
        iv_back.setOnClickListener {
//            if (webView.canGoBack())
//                webView.goBack()
//            else
//                super.
                onBackPressed()
        }

        iv_hamburger.visibility=View.GONE
        iv_cart.visibility=View.GONE

    }
}