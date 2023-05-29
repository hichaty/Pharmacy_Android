package com.bestoffers.enjoeepharmacy.Activity

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_faqs.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class AlertActivity : BottomBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        initView()
        webData()

    }

    private fun initView() {
        iv_cart.setOnClickListener {
            startActivity(Intent(this@AlertActivity, CartActivity::class.java));
        }
        toolbarTitle.setText("Alert")
        iv_hamburger.setOnClickListener {
            fullLayout?.openDrawer(Gravity.LEFT)

        }

//        var nav_view = fullLayout?.findViewById<NavigationView>(R.id.nav_view)
//        nav_view?.setNavigationItemSelectedListener(object :
//            NavigationView.OnNavigationItemSelectedListener {
//            override fun onNavigationItemSelected(item: MenuItem): Boolean {
//                if (item.itemId==R.id.nav_settings) {
//                    Log.e("beer", "ssds");
//                    Toast.makeText(
//                        this@AlertActivity,
//                        "settings clicked",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }else{
//                    Log.e("beer", "ssfsfsfsds");
//                    Toast.makeText(
//                        this@AlertActivity,
//                        "other clicked",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//                return false
//            }
//
//        })

//        var log_btn=nav_view?.findViewById<Button>(R.id.btnLogout)
//        log_btn?.setOnClickListener {
//            Toast.makeText(
//                this@AlertActivity,
//                "logout clicked",
//                Toast.LENGTH_LONG
//            ).show()
//        }

    }

    private fun webData() {
        webView_faq.webViewClient = WebViewClient()
        webView_faq.loadUrl(AppConstants.BASE_DOMAIN+"/alert")
        webView_faq.settings.javaScriptEnabled = true
        webView_faq.settings.setSupportZoom(true)
    }

    override fun onResume() {
        super.onResume()
        imgNearStore.colorFilter = null
        imgAlert.setColorFilter(
            ContextCompat.getColor(
                this@AlertActivity,
                R.color.pink
            ), PorterDuff.Mode.SRC_IN
        )
        imgHome.setColorFilter(
            ContextCompat.getColor(
                this@AlertActivity,
                R.color.black
            ), PorterDuff.Mode.SRC_IN
        )
        imgProfile.setColorFilter(null)
        imgMyOrder.setColorFilter(null)

//        layoutHome!!.setBackgroundColor(Color.parseColor("#ffffff"))
//        layoutPrescription!!.setBackgroundColor(Color.parseColor("#ffffff"))
//        layoutProfile!!.setBackgroundColor(Color.parseColor("#ffffff"))
//        layoutAlert!!.setBackgroundColor(Color.parseColor("#ED8701"))
//        layoutMyOrder!!.setBackgroundColor(Color.parseColor("#ffffff"))

        tvHome!!.setTextColor(Color.parseColor("#000000"))
        tvNearStore!!.setTextColor(Color.parseColor("#000000"))
        tvProfile!!.setTextColor(Color.parseColor("#000000"))
        tvAlert!!.setTextColor(Color.parseColor("#EC99BA"))
        tvMyOrder!!.setTextColor(Color.parseColor("#000000"))
    }
}