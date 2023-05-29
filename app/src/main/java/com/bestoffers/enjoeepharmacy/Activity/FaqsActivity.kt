package com.bestoffers.enjoeepharmacy.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.R
import kotlinx.android.synthetic.main.activity_faqs.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class FaqsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faqs)

        initView()
        webData()

    }

    private fun webData() {
        webView_faq.webViewClient = WebViewClient()
        webView_faq.loadUrl(
            AppConstants.BASE_DOMAIN+"/faqs")
        webView_faq.settings.javaScriptEnabled = true
        webView_faq.settings.setSupportZoom(true)
    }

    private fun initView() {
        toolbarTitle.setText("Faq's")
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }

        iv_hamburger.visibility=View.GONE
        iv_cart.visibility=View.GONE
    }
}