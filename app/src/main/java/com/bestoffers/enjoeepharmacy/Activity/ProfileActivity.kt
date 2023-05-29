package com.bestoffers.enjoeepharmacy.Activity

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bestoffers.enjoeepharmacy.APIClient
import com.bestoffers.enjoeepharmacy.APIRequestListener
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.R
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.json.JSONException
import org.json.JSONObject

class ProfileActivity : BottomBaseActivity() {
    var profile_url="";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initView()
        getProfie_API()

    }

    public fun getProfie_API() {
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("GetProfile", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(
                            this@ProfileActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                        val data = jsonObject.getJSONObject("data");
                        setViews(data)
                    } else {
                        MyApplication.errorAlert(
                            this@ProfileActivity,
                            jsonObject.optString("message", "Please try again later."),
                            "Profile"
                        )
                    }
                } catch (e: JSONException) {
                    Log.e("get_profile_error", e.message.toString())
                }
            }

        }
        APIClient(this).get(AppConstants.URL_GET_PROFILE, apiRequestListener, true)

    }

    public fun setViews(data: JSONObject) {
        if (!TextUtils.isEmpty(data.getString("profile")) && data.getString("profile") != "null") {
            profile_url= data.getString("profile");
            Log.e("profile", profile_url);
            Picasso.with(applicationContext)
                .load(profile_url).into(iv_profile_img);
        }
        setDataText(tv_username, data.getString("name"))
        setDataText(tv_mobile, data.getString("country_code") + " " + data.getString("mobile"));
        setDataText(tv_emailId, data.getString("email"))
    }

    private fun setDataText(textView: TextView, text: String) {
        if (!TextUtils.isEmpty(text) && !text.equals("null")) {
            textView.setText(text)
        }
    }

    private fun initView() {
        toolbarTitle.setText("Profile")
        iv_hamburger.setOnClickListener {
            fullLayout?.openDrawer(Gravity.LEFT);
        }

        iv_cart.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, CartActivity::class.java));
        }
        iv_edit.setOnClickListener {
            startActivity(
                Intent(this@ProfileActivity, EditProfileActivity::class.java)
                    .putExtra("name", tv_username.text.toString())
                    .putExtra("profile_url", profile_url)
            );
        }
    }

    override fun onResume() {
        super.onResume()
        imgMyOrder.colorFilter = null
        imgProfile.setColorFilter(
            ContextCompat.getColor(
                this@ProfileActivity,
                R.color.pink
            ), PorterDuff.Mode.SRC_IN
        )
        imgHome.setColorFilter(
            ContextCompat.getColor(
                this@ProfileActivity,
                R.color.black
            ), PorterDuff.Mode.SRC_IN
        )
        imgNearStore.setColorFilter(null)
        imgAlert.setColorFilter(null)

//        layoutHome!!.setBackgroundColor(Color.parseColor("#ffffff"))
//        layoutMyOrder!!.setBackgroundColor(Color.parseColor("#ffffff"))
//        layoutPrescription!!.setBackgroundColor(Color.parseColor("#ffffff"))
//        layoutProfile!!.setBackgroundColor(Color.parseColor("#ED8701"))
//        layoutAlert!!.setBackgroundColor(Color.parseColor("#ffffff"))

        tvHome!!.setTextColor(Color.parseColor("#000000"))
        tvMyOrder!!.setTextColor(Color.parseColor("#000000"))
        tvNearStore!!.setTextColor(Color.parseColor("#000000"))
        tvProfile!!.setTextColor(Color.parseColor("#EC99BA"))
        tvAlert!!.setTextColor(Color.parseColor("#000000"))
    }
}