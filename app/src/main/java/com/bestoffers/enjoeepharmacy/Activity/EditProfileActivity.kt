package com.bestoffers.enjoeepharmacy.Activity

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.bestoffers.enjoeepharmacy.APIClient
import com.bestoffers.enjoeepharmacy.APIRequestListener
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.R
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import com.bestoffers.enjoeepharmacy.helper.ImagePicker
import com.bestoffers.enjoeepharmacy.helper.PermissionHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.etFullName
import kotlinx.android.synthetic.main.activity_edit_profile.iv_profile_img
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.json.JSONException
import org.json.JSONObject

class EditProfileActivity : AppCompatActivity(), PermissionHelper.PermissionCallback,
    ImagePicker.Picker {
    var selectedImagePath = ""
    var permissionHelper: PermissionHelper? = null
    val REQUEST_WRITE_STORAGE = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initView()
        val intent = intent.extras;
        if (intent != null) {
            val name = intent.getString("name").toString();
            etFullName.setText(name);
            val profile_url = intent.getString("profile_url").toString();
            if (!TextUtils.isEmpty(profile_url) && profile_url != "null") {
                Log.e("profile_edit",profile_url);
                Picasso.with(applicationContext)
                    .load(profile_url).into(iv_profile_img);
            }
        }

    }

    public fun getProfie_API() {
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("GetProfile", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(
                            this@EditProfileActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                        val data = jsonObject.getJSONObject("data");
                        setViews(data)
                    } else {
                        MyApplication.errorAlert(
                            this@EditProfileActivity,
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
            Picasso.with(applicationContext)
                .load(AppConstants.BASE_DOMAIN + "/" + data.getString("profile"))
                .into(iv_profile_img);
        }
        setDataText(etFullName, data.getString("name"))
    }

    private fun setDataText(textView: EditText, text: String) {
        if (!TextUtils.isEmpty(text) && !text.equals("null")) {
            textView.setText(text)
        }
    }

    private fun initView() {
        iv_hamburger.visibility = View.GONE;
        iv_cart.visibility = View.GONE;
        iv_back.visibility = View.VISIBLE;
        toolbarTitle.setText("Edit Profile")

        iv_back.setOnClickListener {
            onBackPressed()
        }

        btnSave.setOnClickListener {
            if (etFullName.text.isNullOrBlank()) {
                setError(etFullName, "Enter valid name");
            } else {
                updateProfile_Api(
                    etFullName.text.toString().trim()
                )
            }
        }

        iv_profile_img.setOnClickListener {
            permissionHelper = PermissionHelper(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), REQUEST_WRITE_STORAGE
            )
            permissionHelper?.request(this)
        }

    }

    private fun updateProfile_Api(name: String) {
        var params =
            arrayOf(
                "name"
            )
        var values =
            arrayOf(name)

        val paramList = params.toCollection(ArrayList())
        val valueList = values.toCollection(ArrayList())

        if (!TextUtils.isEmpty(selectedImagePath)) {
            paramList.add("profile")
            valueList.add(selectedImagePath)
        }

        val finalParams1 = arrayOfNulls<String>(paramList.size)
        val finalValues1 = arrayOfNulls<String>(valueList.size)
        paramList.toArray(finalParams1)
        valueList.toArray(finalValues1)

        val finalParams = finalParams1 as Array<String>
        val finalValues = finalValues1 as Array<String>

        val body = APIClient.createMultiPartBuilder(finalParams, finalValues)

        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("UpdateProfile", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(
                            this@EditProfileActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        MyApplication.errorAlert(
                            this@EditProfileActivity,
                            jsonObject.optString("message", "Invalid Details"),
                            "Update Profile"
                        )
                    }
                } catch (e: JSONException) {

                }
            }

        }
        APIClient(this).post(AppConstants.URL_UPDATE_PROFILE, body, apiRequestListener, true)

    }

    private fun setError(editText: EditText?, message: String) {
        editText?.setError(message);
        editText?.requestFocus();
    }

    override fun onImagePicked(bitmap: Bitmap?, imagePath: String?) {
        selectedImagePath = imagePath!!
        iv_profile_img.setImageBitmap(bitmap)
    }

    override fun onPermissionGranted(requestCode: Int) {
        ImagePicker.picker = this
        startActivity(Intent(this, ImagePicker::class.java))
    }

    override fun onIndividualPermissionGranted(
        grantedPermission: Array<out String>?,
        requestCode: Int
    ) {

    }

    override fun onPermissionDenied(requestCode: Int) {
    }

    override fun onPermissionDeniedBySystem(requestCode: Int) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionHelper != null) {
            permissionHelper?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onResume() {
        super.onResume()
//        getProfie_API()
    }

}