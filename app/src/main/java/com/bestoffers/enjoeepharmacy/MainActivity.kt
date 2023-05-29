package com.bestoffers.enjoeepharmacy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.bestoffers.enjoeepharmacy.Activity.*

class MainActivity : AppCompatActivity() {
    var fullLayout: RelativeLayout? = null
    var navigationView: RelativeLayout? = null
    var activityContainer: FrameLayout? = null
    var layoutAlert: LinearLayout? = null
    var layoutPrescription: LinearLayout? = null
    var layoutHome: LinearLayout? = null
    var layoutMyOrder: LinearLayout? = null
    var layoutProfile: LinearLayout? = null

    lateinit var imgAlert: ImageView
    lateinit var imgPrescription: ImageView
    lateinit var imgHome: ImageView
    lateinit var ivMyOrder: ImageView
    lateinit var imgProfile: ImageView

    var tvAlert: TextView? = null
    var tvPrescription: TextView? = null
    var tvHome: TextView? = null
    var tvMyOrder: TextView? = null
    var tvProfile: TextView? = null
    var screen="";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({
//            if(!TextUtils.isEmpty(screen)){
                imgHome.performClick();
//                Log.e("clicked","yes");
//            }else{
//                Log.e("clicked","no");
//            }
        },0)
        initView();

    }

    private fun initView() {
        navigationView = findViewById(R.id.bottom_navigation)

        imgAlert = findViewById(R.id.imgAlert)
        imgPrescription = findViewById(R.id.imgPrescription)
        imgHome = findViewById(R.id.imgHome)
        ivMyOrder = findViewById(R.id.ivMyOrder)
        imgProfile = findViewById(R.id.imgProfile)

        layoutAlert = findViewById(R.id.layoutAlert)
        layoutPrescription = findViewById(R.id.layoutPrescription)
        layoutHome = findViewById(R.id.layoutHome)
        layoutMyOrder = findViewById(R.id.layoutMyOrder)
        layoutProfile = findViewById(R.id.layoutProfile)

        tvAlert = findViewById(R.id.tvAlert)
        tvPrescription = findViewById(R.id.tvPrescription)
        tvHome = findViewById(R.id.tvHome)
        tvMyOrder = findViewById(R.id.tvMyOrder)
        tvProfile = findViewById(R.id.tvProfile)

        imgAlert.setOnClickListener{
            startActivity(Intent(this@MainActivity, AlertActivity::class.java))
        }
        imgPrescription.setOnClickListener{
            startActivity(Intent(this@MainActivity, NearStoreActivity::class.java))
        }
        imgHome.setOnClickListener{
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        }
        ivMyOrder.setOnClickListener{
            startActivity(Intent(this@MainActivity, MyOrderActivity::class.java))
        }
        imgProfile.setOnClickListener{
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
        }
    }
}