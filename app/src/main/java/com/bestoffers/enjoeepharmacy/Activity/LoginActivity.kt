package com.bestoffers.enjoeepharmacy.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import com.bestoffers.enjoeepharmacy.*
import com.bestoffers.enjoeepharmacy.Utils.SharedPreferenceUtility
import com.bestoffers.enjoeepharmacy.Utils.Utils
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import com.hbb20.CCPCountry
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    var texts = "0";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()
        setCountryCodeSpinner()

    }

    private fun setCountryCodeSpinner() {
        val array = ArrayList<String>()
        val array_code = ArrayList<String>()
        val array_flag = ArrayList<String>()
        val array_cc = ArrayList<String>()

        for (i in CCPCountry.getLibraryMasterCountriesEnglish()) {
            array_code.add(i.phoneCode);
            array.add(i.name);
            array_flag.add(i.flagID.toString())
            array_cc.add(i.phoneCode + "       " + i.name);
        }
        array_cc.add(0, "CC")
        searchSpinnerCountry_login.setTitle("Country Code");
        Utils.setSpinnerCCAdapter(
            this@LoginActivity,
            searchSpinnerCountry_login,
            array_cc
        )
    }

    private fun initView() {
        searchSpinnerCountry_login.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                texts = adapterView.getItemAtPosition(i).toString().split(" ")[0].toString();
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })

        tvSignUp.setOnClickListener() {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java));
        }

        tvForgot.setOnClickListener() {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java));
        }

        btnLogin?.setOnClickListener() {
            Log.e("text", texts);
            if (etMobileNo.text.isNullOrBlank()) {
                setError(etMobileNo, "Enter valid mobile number");
            } else if (texts.equals("CC")) {
                setError(etMobileNo, "Select valid country code");
            } else if (etPassword.text.isNullOrBlank()) {
                setError(etPassword, "Enter valid password");
            } else {
                login_API(texts, etMobileNo.text.toString().trim(), etPassword.text.toString().trim())
            }
        }
    }

    private fun setError(editText: EditText?, message: String) {
        editText?.setError(message);
        editText?.requestFocus();
    }

    private fun login_API(country_code: String, mobile: String, password: String) {
        Log.e("country_code_login", country_code)
        val body = APIClient.createBuilder(
            arrayOf("country_code", "mobile", "password"),
            arrayOf("+" + country_code, mobile, password)
        )
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("Login", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(
                            this@LoginActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
//                        Log.e("temp_id_register",jsonObject.getJSONObject("data").getInt("id").toString())
                        var data=jsonObject.getJSONObject("data")
                        SharedPreferenceUtility.getInstance().save(
                            AppConstants.PREF_IS_LOGGED_IN,
                            true
                        )
                        SharedPreferenceUtility.getInstance().save(
                            AppConstants.PREF_ACCESS_TOKEN,
                            jsonObject.getString(AppConstants.PREF_ACCESS_TOKEN)
                        )
                        SharedPreferenceUtility.getInstance().save(
                            AppConstants.PREF_TOKEN_TYPE,
                            jsonObject.getString(AppConstants.PREF_TOKEN_TYPE)
                        )
                        SharedPreferenceUtility.getInstance().save(
                            AppConstants.PREF_AUTH,
                            data.optString(AppConstants.PREF_AUTH)
                        )
                        SharedPreferenceUtility.getInstance().save(
                            AppConstants.AUTHORIZATION,
                            jsonObject.optString(AppConstants.AUTHORIZATION)
                        )
                        SharedPreferenceUtility.getInstance().save(
                            AppConstants.PREF_USER_ID,
                            data.optInt("id").toString()
                        )
                        SharedPreferenceUtility.getInstance().save(
                            AppConstants.PREF_EMAIL,
                            data.optString("email").toString()
                        )
                        SharedPreferenceUtility.getInstance().save(
                            AppConstants.PREF_USER_DATA,
                            data.toString()
                        )

//                        SharedPreferenceUtility.getInstance().save(
//                            AppConstants.PREF_EMAIL,
//                            jsonObject.optString("id")
//                        )
                        SharedPreferenceUtility.getInstance().save(
                            AppConstants.PREF_FULL_NAME,
                            data.optString("name")
                        )
                        SharedPreferenceUtility.getInstance().save(
                            AppConstants.PREF_IMAGE,
                            data.optString("profile")
                        )
                        SharedPreferenceUtility.getInstance().save(
                            AppConstants.PREF_MOB,
                            data.optString("mobile")
                        )
                        SharedPreferenceUtility.getInstance().save(
                            AppConstants.PREF_COUNTRY_CODE,
                            data.optString("country_code")
                        )
                        startActivity(
                            Intent(this@LoginActivity, HomeActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        );
                    } else {
                        MyApplication.errorAlert(
                            this@LoginActivity,
                            jsonObject.optString("message", "Incorrect email or password"),
                            "Login"
                        )
                    }
                } catch (e: JSONException) {

                }
            }

        }
        APIClient(this).post(AppConstants.URL_LOGIN, body, apiRequestListener, true)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity();
    }

}