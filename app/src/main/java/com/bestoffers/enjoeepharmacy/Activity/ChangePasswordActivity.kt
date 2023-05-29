package com.bestoffers.enjoeepharmacy.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.bestoffers.enjoeepharmacy.APIClient
import com.bestoffers.enjoeepharmacy.APIRequestListener
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.R
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import org.json.JSONException
import org.json.JSONObject

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        initView()

    }

    private fun initView() {
        btnDone.setOnClickListener {
            if (etOldPassword.text.isNullOrBlank()) {
                setError(etOldPassword, "Enter valid password");
            } else if (etNewPassword.text.isNullOrBlank()) {
                setError(etNewPassword, "Enter valid password");
            } else if (etNewCPassword.text.isNullOrBlank()) {
                setError(etNewCPassword, "Enter valid password");
            } else if (!etNewPassword.text.toString().trim()
                    .equals(etNewCPassword.text.toString().trim())
            ) {
                setError(etNewCPassword, "Password doesn't match");
            } else {
                changePassword_Api(
                    etOldPassword.text.toString().trim(),
                    etNewPassword.text.toString().trim(),
                    etNewCPassword.text.toString().trim(),
                )
            }
        }
    }

    private fun changePassword_Api(
        old_password: String,
        new_password: String,
        c_new_password: String
    ) {

        val body = APIClient.createBuilder(
            arrayOf(
                "current_password",
                "new_password",
                "confirm_password"
            ),
            arrayOf(old_password, new_password, c_new_password)
        )
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("ChangePassword", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(
                            this@ChangePasswordActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()

                        finish();
                    } else {
                        MyApplication.errorAlert(
                            this@ChangePasswordActivity,
                            jsonObject.optString("message", "Invalid Details"),
                            "Change Password"
                        )
                    }
                } catch (e: JSONException) {

                }
            }

        }
        APIClient(this).post(AppConstants.URL_CHANGE_PASSWORD, body, apiRequestListener, true)

    }

    private fun setError(editText: EditText?, message: String) {
        editText?.setError(message);
        editText?.requestFocus();
    }

}