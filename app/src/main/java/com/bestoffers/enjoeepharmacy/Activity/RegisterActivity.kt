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
import com.bestoffers.enjoeepharmacy.Models.RegisterApiModel
import com.bestoffers.enjoeepharmacy.Utils.Utils
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import com.hbb20.CCPCountry
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.etMobileNo
import kotlinx.android.synthetic.main.activity_register.etPassword
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    var text = "0";
    var list = ArrayList<RegisterApiModel>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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
        searchSpinnerCountry_register.setTitle("Country Code");
        Utils.setSpinnerCCAdapter(
            this@RegisterActivity,
            searchSpinnerCountry_register,
            array_cc
        )
    }

    private fun initView() {
        searchSpinnerCountry_register.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                text = adapterView.getItemAtPosition(i).toString().split(" ")[0].toString();
                Log.e("shdbsj", text.toString());
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })

        tvLogin.setOnClickListener() {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java));
        }

        btnRegister?.setOnClickListener() {
            if (etFullName.text.isNullOrBlank()) {
                setError(etFullName, "Enter valid name");
            } else if (text.equals("CC")) {
                setError(etMobileNo, "Select valid country code");
            } else if (etMobileNo.text.isNullOrBlank() || etMobileNo.text.length < 10) {
                setError(etMobileNo, "Enter valid mobile number");
            } else if (etPassword.text.isNullOrBlank()) {
                setError(etPassword, "Enter valid password");
            } else {
                register_API(
                    etFullName.text.toString().trim(),
                    text,
                    etMobileNo.text.toString().trim(),
                    etPassword.text.toString().trim()
                )
            }
        }
    }

    private fun register_API(name: String, country_code: String, mobile: String, password: String) {
        val body = APIClient.createBuilder(
            arrayOf("name", "country_code", "mobile", "password"),
            arrayOf(name, "+" + country_code, mobile, password)
        )
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("Signup", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(
                            this@RegisterActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("temp_id_register",jsonObject.getJSONObject("data").getInt("id").toString())
                        startActivity(
                            Intent(this@RegisterActivity, OTPActivity::class.java)
                                    .putExtra("temp_id",jsonObject.getJSONObject("data").getInt("id").toString())
                                .putExtra("from","1")
                        )
                    }else{
                        MyApplication.errorAlert(
                            this@RegisterActivity,
                            jsonObject.optString("message", "Incorrect email or password"),
                            "Sign Up"
                        )
                    }
                } catch (e: JSONException) {

                }
            }

        }
        APIClient(this).post(AppConstants.URL_SIGNUP, body, apiRequestListener, true)

    }

    private fun setError(editText: EditText?, message: String) {
        editText?.setError(message);
        editText?.requestFocus();
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity();
    }

}