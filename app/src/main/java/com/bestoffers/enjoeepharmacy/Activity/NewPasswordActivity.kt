package com.bestoffers.enjoeepharmacy.Activity

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_new_password.*
import kotlinx.android.synthetic.main.activity_otpactivity.*
import org.json.JSONException
import org.json.JSONObject

class NewPasswordActivity : AppCompatActivity() {

    var temp_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)

        val intentt = intent.extras;
        temp_id = intentt?.getString("temp_id").toString();
        initView()

    }

    private fun initView() {
        btnReset?.setOnClickListener() {
            if (etPassword.text.isNullOrBlank()) {
                setError(etPassword, "Enter valid mobile number");
            } else if (etConfirmPassword.text.isNullOrBlank()) {
                setError(etConfirmPassword, "Enter valid otp");
            }else if (!etConfirmPassword.text.toString().equals(etPassword.text.toString())) {
                setError(etConfirmPassword, "Password doesn't match");
            } else {
                resetPassword_API(temp_id, etConfirmPassword.text.toString())
            }
        }
    }

    private fun resetPassword_API(temp_id: String, password: String) {

        val body = APIClient.createBuilder(
            arrayOf("id", "password"),
            arrayOf(temp_id, password)
        )
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("SetPassword", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(
                            this@NewPasswordActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this@NewPasswordActivity, LoginActivity::class.java));
                    } else {
                        MyApplication.errorAlert(
                            this@NewPasswordActivity,
                            jsonObject.optString("message", "Invalid password"),
                            "Reset Password"
                        )
                    }
                } catch (e: JSONException) {

                }
            }

        }
        APIClient(this).post(AppConstants.URL_SET_PASSWORD, body, apiRequestListener, true)
    }

    private fun setError(editText: EditText?, message: String) {
        editText?.setError(message);
        editText?.requestFocus();
    }

}