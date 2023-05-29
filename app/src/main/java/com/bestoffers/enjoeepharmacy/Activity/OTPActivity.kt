package com.bestoffers.enjoeepharmacy.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.bestoffers.enjoeepharmacy.*
import com.bestoffers.enjoeepharmacy.Models.ResendOtpModel
import com.bestoffers.enjoeepharmacy.Models.VerifyOtpModel
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.btnSubmit
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_otpactivity.*
import kotlinx.android.synthetic.main.activity_otpactivity.tvLogin
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.FormBody
import org.json.JSONException
import org.json.JSONObject

class OTPActivity : AppCompatActivity() {
    var text = "0";
    var from = "0" //1:Signup 2:Login 3:Forgot Password
    var verifyOtpModellist = ArrayList<VerifyOtpModel>();
    var resendOtpModellist = ArrayList<ResendOtpModel>();
    var temp_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpactivity)

        val intentt = intent.extras;
        from = intentt?.getString("from").toString();
        temp_id = intentt?.getString("temp_id").toString();

        Log.e("temp_id", temp_id.toString())

        initView()
//        setCountryCodeSpinner()

    }

    private fun setCountryCodeSpinner() {
        val array = ArrayList<String>()
        val array_code = ArrayList<String>()
        val array_flag = ArrayList<String>()
        val array_cc = ArrayList<String>()

//        for (i in CCPCountry.getLibraryMasterCountriesEnglish()) {
//            array_code.add(i.phoneCode);
//            array.add(i.name);
//            array_flag.add(i.flagID.toString())
//            array_cc.add(i.phoneCode + "       " + i.name);
//        }
//        array_cc.add(0, "CC")
//        searchSpinnerCountry_fp.setTitle("Country Code");
//        Utils.setSpinnerCCAdapter(
//            this@OTPActivity,
//            searchSpinnerCountry_fp,
//            array_cc
//        )
    }

    private fun initView() {

//        searchSpinnerCountry_fp.setOnItemSelectedListener(object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
//                text = adapterView.getItemAtPosition(i).toString().split(" ")[0].toString();
//            }
//
//            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
//        })

        btnSubmit?.setOnClickListener() {
//            if (etMobileNo.text.isNullOrBlank()) {
//                setError(etMobileNo, "Enter valid mobile number");
//            } else
//                if (text.equals("0")) {
//                setError(etMobileNo, "Select valid country code");
//            } else
            if (etOtp.text.isNullOrBlank()) {
                setError(etOtp, "Enter valid otp");
            } else {
                otp_API(
                    temp_id.toString().trim(),
                    etOtp.text.toString().trim()
                )
            }
        }

        tvLogin.setOnClickListener() {
            startActivity(Intent(this@OTPActivity, LoginActivity::class.java));
        }

        if(from=="3"){
            tvResendOtp.visibility=View.GONE
        }

        tvResendOtp.setOnClickListener {
            resend_otp_API(
                temp_id.toString()
            )
        }

    }

    private fun setError(editText: EditText?, message: String) {
        editText?.setError(message);
        editText?.requestFocus();
    }

    private fun otp_API(temp_id: String, otp: String) {
        Log.e("from",from);
        var body: FormBody.Builder

        body = APIClient.createBuilder(arrayOf("id", "otp"), arrayOf(temp_id, otp))
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("otp", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
//                        val data = jsonObject.getJSONObject("data")
                        Toast.makeText(
                            this@OTPActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
//                            var sessionManager1 = SessionManager(applicationContext);
//                            sessionManager1.CreateSession(
//                            )
                        if (from.equals("3")) {
                            startActivity(
                                Intent(
                                    this@OTPActivity,
                                    NewPasswordActivity::class.java
                                ).putExtra("temp_id", temp_id)
                            );
                        } else {
                            val intent = Intent(this@OTPActivity, LoginActivity::class.java);
                            startActivity(intent);
                        }
                    } else {
                        MyApplication.errorAlert(
                            this@OTPActivity,
                            jsonObject.optString("message", "Incorrect otp"),
                            "OTP"
                        )
                    }
                } catch (e: JSONException) {

                }

            }
        }
        if (from.equals("3")) {
            APIClient(this).post(
                AppConstants.URL_FORGOT_PASSWORD_VERIFY,
                body,
                apiRequestListener,
                true
            )
        } else {
            APIClient(this).post(AppConstants.URL_OTP_VERIFY, body, apiRequestListener, true)
        }
    }

    private fun resend_otp_API(temp_id: String) {
        var body: FormBody.Builder

        body = APIClient.createBuilder(arrayOf("id"), arrayOf(temp_id))
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("otp_resend", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
                        val data = jsonObject.getJSONObject("data")
                        Toast.makeText(
                            this@OTPActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
//                            var sessionManager1 = SessionManager(applicationContext);
//                            sessionManager1.CreateSession(
//                            )
                        if (from == "3") {
                            startActivity(
                                Intent(
                                    this@OTPActivity,
                                    NewPasswordActivity::class.java
                                ).putExtra("temp_id", temp_id)
                            );
                        } else {
                            val intent = Intent(this@OTPActivity, HomeActivity::class.java);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent);
                        }
                    } else {
                        MyApplication.errorAlert(
                            this@OTPActivity,
                            jsonObject.optString("message", "Incorrect otp"),
                            "OTP"
                        )
                    }
                } catch (e: JSONException) {

                }
            }
        }
        APIClient(this).post(AppConstants.URL_RESEND_VERIFY_OTP, body, apiRequestListener, true)

    }

//    private fun otp_API(temp_id: String, otp: String) {
//        if (from == "3") {
//            startActivity(Intent(this@OTPActivity, NewPasswordActivity::class.java));
//        } else {
//            progressDialog?.show();
//            var retIn = RetroFitClient.getRetrofitInstance().create(APIInterface::class.java);
//            retIn.verifyOtp(VerifyOTPBody(temp_id, otp))
//                .enqueue(object : Callback<VerifyOtpModel> {
//                    override fun onResponse(
//                        call: Call<VerifyOtpModel>,
//                        response: Response<VerifyOtpModel>
//                    ) {
//                        if (response?.body() != null) {
//                            Log.e("verifyOtp_response", Gson().toJson(response.body()!!));
//                            if (response.code() == 200 || response.code() == 201)
//                                if (response.body()!!.status) {
//                                    progressDialog?.dismiss()
//                                    verifyOtpModellist.add(response.body()!!);
//                                    Toast.makeText(
//                                        applicationContext,
//                                        response.body()!!.message,
//                                        Toast.LENGTH_SHORT
//                                    ).show();
////                                    var sessionManager1 = SessionManager(applicationContext);
////                                    sessionManager1.CreateSession(verifyOtpModellist.get(0).data.id.toString(), verifyOtpModellist.get(0).data.username.toString(), verifyOtpModellist.get(0).data.email.toString(),verifyOtpModellist.get(0).token)
//                                    val intent = Intent(this@OTPActivity, HomeActivity::class.java);
//                                    startActivity(intent);
//                                } else {
//                                    progressDialog?.dismiss()
//                                    Toast.makeText(
//                                        applicationContext,
//                                        response.body()!!.message,
//                                        Toast.LENGTH_SHORT
//                                    ).show();
//                                }
//                        }
//
//                    }
//
//                    override fun onFailure(call: Call<VerifyOtpModel>, t: Throwable) {
//                        progressDialog?.dismiss()
//                        Toast.makeText(
//                            this@OTPActivity,
//                            "Poor Internet Connection",
//                            Toast.LENGTH_SHORT
//                        ).show();
//                    }
//                })
//
//        }
//    }

//    private fun resend_otp_API(temp_id: String) {
//        if (from == "3") {
//            startActivity(Intent(this@OTPActivity, NewPasswordActivity::class.java));
//        } else {
//            progressDialog?.show();
//            var retIn = RetroFitClient.getRetrofitInstance().create(APIInterface::class.java);
//            retIn.resendVerifyOtp(ResendVerifyOTPBody(temp_id))
//                .enqueue(object : Callback<ResendOtpModel> {
//                    override fun onResponse(
//                        call: Call<ResendOtpModel>,
//                        response: Response<ResendOtpModel>
//                    ) {
//                        if (response?.body() != null) {
//                            Log.e("resendOtp_response",Gson().toJson(response.body()!!));
//                            if (response.code() == 200 || response.code() == 201)
//                                if (response.body()!!.status) {
//                                    progressDialog?.dismiss()
//                                    resendOtpModellist.add(response.body()!!);
//                                    Toast.makeText(
//                                        applicationContext,
//                                        response.body()!!.message,
//                                        Toast.LENGTH_SHORT
//                                    ).show();
////                                    val intent = Intent(this@OTPActivity, HomeActivity::class.java);
////                                    startActivity(intent);
//                                } else {
//                                    progressDialog?.dismiss()
//                                    Toast.makeText(
//                                        applicationContext,
//                                        response.body()!!.message,
//                                        Toast.LENGTH_SHORT
//                                    ).show();
//                                }
//                        }
//
//                    }
//
//                    override fun onFailure(call: Call<ResendOtpModel>, t: Throwable) {
//                        progressDialog?.dismiss()
//                        Log.e("resendOtp_error",t.message.toString());
//                        Toast.makeText(
//                            this@OTPActivity,
//                            "Poor Internet Connection",
//                            Toast.LENGTH_SHORT
//                        ).show();
//                    }
//                })
//
//        }
//    }

}