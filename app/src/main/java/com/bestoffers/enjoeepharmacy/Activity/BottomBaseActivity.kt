package com.bestoffers.enjoeepharmacy.Activity

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bestoffers.enjoeepharmacy.R
import com.bestoffers.enjoeepharmacy.Utils.SharedPreferenceUtility
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.internal.NavigationMenu
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_bottom_base.*
import kotlinx.android.synthetic.main.nav_header_dashboard.*

open class BottomBaseActivity : AppCompatActivity()
//    ,NavigationView.OnNavigationItemSelectedListener
{
    var fullLayout: DrawerLayout? = null
    var navigationView: RelativeLayout? = null
    var activityContainer: FrameLayout? = null
    var layoutAlert: LinearLayout? = null
    var layoutNearStore: LinearLayout? = null
    var layoutHome: LinearLayout? = null
    var layoutMyOrder: LinearLayout? = null
    var layoutProfile: LinearLayout? = null

    lateinit var imgAlert: ImageView
    lateinit var imgNearStore: ImageView
    lateinit var imgHome: ImageView
    lateinit var imgMyOrder: ImageView
    lateinit var imgProfile: ImageView
    lateinit var nav_view: NavigationView

    var tvAlert: TextView? = null
    var tvNearStore: TextView? = null
    var tvHome: TextView? = null
    var tvMyOrder: TextView? = null
    var tvProfile: TextView? = null

    //    lateinit var tvPrescription: TextView;
//    lateinit var tvLanguage: TextView;
    lateinit var btnLogout: Button;

    override fun setContentView(layoutResID: Int) {
        val layoutInflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        fullLayout = layoutInflater.inflate(R.layout.activity_bottom_base, null) as DrawerLayout?
        activityContainer = fullLayout!!.findViewById(R.id.container)
        layoutInflater.inflate(layoutResID, activityContainer, true)
        super.setContentView(fullLayout)

        nav_view = fullLayout!!.findViewById(R.id.nav_view)
        nav_view.setNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (item.itemId == R.id.nav_settings) {
                    Log.e("navigation", "settings");
                } else if(item.itemId==R.id.tvPrescription){
                   startActivity(Intent(this@BottomBaseActivity,PrescriptionActivity::class.java))
                } else if(item.itemId==R.id.nav_privacy_policy){
                   startActivity(Intent(this@BottomBaseActivity,PrivacyPolicyActivity::class.java))
                }else if(item.itemId==R.id.nav_terms_of_use){
                   startActivity(Intent(this@BottomBaseActivity,TermsOfUseActivity::class.java))
                }else if(item.itemId==R.id.nav_faq){
                   startActivity(Intent(this@BottomBaseActivity,FaqsActivity::class.java))
                }
                return true;
            }

        })


//        fullLayout?.closeDrawer(Gravity.LEFT)

        val header = nav_view.getHeaderView(0);
        val tvPrescription = header.findViewById(R.id.tvPrescription) as TextView

        navigationView = findViewById(R.id.bottom_navigation)

        imgAlert = findViewById(R.id.imgAlert)
        imgNearStore = findViewById(R.id.imgNearStore)
        imgHome = findViewById(R.id.imgHome)
        imgMyOrder = findViewById(R.id.imgMyOrder)
        imgProfile = findViewById(R.id.imgProfile)

        layoutAlert = findViewById(R.id.layoutAlert)
        layoutNearStore = findViewById(R.id.layoutNearStore)
        layoutHome = findViewById(R.id.layoutHome)
        layoutMyOrder = findViewById(R.id.layoutMyOrder)
        layoutProfile = findViewById(R.id.layoutProfile)

        tvAlert = findViewById(R.id.tvAlert)
        tvNearStore = findViewById(R.id.tvNearStore)
        tvHome = findViewById(R.id.tvHome)
        tvMyOrder = findViewById(R.id.tvMyOrder)
        tvProfile = findViewById(R.id.tvProfile)

        btnLogout = findViewById(R.id.btnLogout)


        imgAlert.setOnClickListener {
            startActivity(Intent(this@BottomBaseActivity, AlertActivity::class.java))
        }
        imgNearStore.setOnClickListener {
            startActivity(Intent(this@BottomBaseActivity, NearStoreActivity::class.java))
        }
        imgHome.setOnClickListener {
            startActivity(Intent(this@BottomBaseActivity, HomeActivity::class.java))
        }
        imgMyOrder.setOnClickListener {
            startActivity(Intent(this@BottomBaseActivity, MyOrderActivity::class.java))
        }
        imgProfile.setOnClickListener {
            startActivity(Intent(this@BottomBaseActivity, ProfileActivity::class.java))
        }

        tvPrescription.setOnClickListener {
            Log.e("bn", "vbvb");
            startActivity(Intent(this@BottomBaseActivity, PrescriptionActivity::class.java))
        }

        btnLogout.setOnClickListener {
            Log.e("kflfg","gkgjfig")
            SharedPreferenceUtility.getInstance().clearSharedPreferences()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

//        container.setOnClickListener {
//            fullLayout?.closeDrawer(Gravity.LEFT)
//        }

/*
        tvLanguage.setOnClickListener {
            Log.e("bn", "vbvfdfb");
            startActivity(Intent(this@BottomBaseActivity, PrescriptionActivity::class.java))
        }
*/
//        tv_test.setOnClickListener {
//            Log.e("bn", "vbvfdfb");
//            startActivity(Intent(this@BottomBaseActivity, PrescriptionActivity::class.java))
//        }

    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.nav_settings) {
//            Log.e("beer", "ssds");
//        }
//        return true;
//    }

}