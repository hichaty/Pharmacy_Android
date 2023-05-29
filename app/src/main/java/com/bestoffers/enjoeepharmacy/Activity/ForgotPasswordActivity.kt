package com.bestoffers.enjoeepharmacy.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import com.bestoffers.enjoeepharmacy.APIClient
import com.bestoffers.enjoeepharmacy.APIRequestListener
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.R
import com.bestoffers.enjoeepharmacy.Utils.Utils
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import com.hbb20.CCPCountry
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etMobileNo
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {
    var text = "0";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

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
        searchSpinnerCountry_fp.setTitle("Country Code");
        Utils.setSpinnerCCAdapter(
            this@ForgotPasswordActivity,
            searchSpinnerCountry_fp,
            array_cc
        )
    }

    private fun initView() {

        searchSpinnerCountry_fp.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                text = adapterView.getItemAtPosition(i).toString().split(" ")[0].toString();
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })

        btnSubmit?.setOnClickListener() {
            if (etMobileNo.text.isNullOrBlank())
            {
                setError(etMobileNo,"Enter valid mobile number");
            }else if (text.equals("CC")) {
                setError(etMobileNo,"Select valid country code");
            } else {
                forgotPassword_API(text,etMobileNo.text.toString())
            }
        }
    }

    private fun setError(editText: EditText?, message: String) {
        editText?.setError(message);
        editText?.requestFocus();
    }

    private fun forgotPassword_API(country_code:String,mobile: String) {
        val body = APIClient.createBuilder(
            arrayOf("country_code", "mobile"),
            arrayOf("+" + country_code, mobile)
        )
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("ForgotPassword", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
//                        Log.e("temp_id_register",jsonObject.getJSONObject("data").getInt("id").toString())

                        startActivity(Intent(this@ForgotPasswordActivity, OTPActivity::class.java)
                            .putExtra("temp_id",jsonObject.getJSONObject("data").getInt("id").toString())
                            .putExtra("from","3"));
                    } else {
                        MyApplication.errorAlert(
                            this@ForgotPasswordActivity,
                            jsonObject.optString("message", "Invalid mobile number"),
                            "Forgot Password"
                        )
                    }
                } catch (e: JSONException) {

                }
            }

        }
        APIClient(this).post(AppConstants.URL_FORGOT_PASSWORD, body, apiRequestListener, true)
    }
}