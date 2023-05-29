package com.bestoffers.enjoeepharmacy.Activity

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.bestoffers.enjoeepharmacy.*
import com.bestoffers.enjoeepharmacy.Utils.SharedPreferenceUtility
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import com.payu.base.models.ErrorResponse
import com.payu.base.models.PayUPaymentParams
import com.payu.checkoutpro.PayUCheckoutPro.open
import com.payu.checkoutpro.models.PayUCheckoutProConfig
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_HASH_NAME
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_HASH_STRING
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_MERCHANT_RESPONSE
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_PAYU_RESPONSE
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_UDF1
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_UDF2
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_UDF3
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_UDF4
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_UDF5
import com.payu.ui.model.listeners.PayUCheckoutProListener
import com.payu.ui.model.listeners.PayUHashGenerationListener
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import okhttp3.FormBody
import org.json.JSONException
import org.json.JSONObject


class CheckoutActivity : AppCompatActivity(), PaymentResultWithDataListener {
    var user_id = ""
    var mobile = ""
    var email = ""
    var subtotal = ""
    var shipping_charges = ""
    var tax = ""
    var total = ""
    var razorpay_key_id = ""

    var appEnvironment: AppEnvironment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val intentt = intent?.extras;
        if (intentt != null) {
            subtotal = intentt.getString("subtotal").toString();
            shipping_charges = intentt.getString("shipping_charges").toString();
            tax = intentt.getString("tax").toString();
            total = intentt.getString("total").toString();
        }

        if (MyApplication.getAppEnvironment() == AppEnvironment.PRODUCTION) {
            Log.d("TAG", "Environment Set to Production");
        } else {
            Log.d("TAG", "Environment Set to SandBox");
        }



        initView()
        user_id = SharedPreferenceUtility.getInstance().get(AppConstants.PREF_USER_ID, "")
        mobile = SharedPreferenceUtility.getInstance().get(AppConstants.PREF_MOB, "")
        email = SharedPreferenceUtility.getInstance().get(AppConstants.PREF_EMAIL, "")


    }


    private fun initView() {
        iv_hamburger.visibility = View.GONE;
        iv_cart.visibility = View.GONE;
        iv_back.visibility = View.VISIBLE;
        toolbarTitle.setText("Checkout")

        iv_back.setOnClickListener {
            onBackPressed()
        }

        tv_sub_total.setText(subtotal.toString())
        tv_shipping_charges.setText(shipping_charges.toString())
        tv_tax.setText(tax.toString())
        tv_amount_to_be_paid.setText(total.toString())

        btn_place_order_pay.setOnClickListener {
            generateOrderId_API(
                user_id,
                subtotal,
                shipping_charges,
                tax,
                total,
                "button",
                "",
                "",
                ""
            )
        }

    }

    private fun generateOrderId_API(
        user_id: String,
        subtotal: String,
        shipping_charges: String,
        tax: String,
        total_price: String,
        from: String,
        transaction_id: String,
        payment_type: String,
        payment_status: String
    ) {
//        val body = APIClient.createBuilder(
//            arrayOf(
//                "user_id",
//                "subtotal",
//                "shipping_charges",
//                "tax",
//                "total_price",
//            ),
//            arrayOf(user_id,subtotal,shipping_charges,tax, total_price)
//        )

        var body = FormBody.Builder()
        if (from.equals("button")) {
            body = APIClient.createBuilder(
                arrayOf(
                    "user_id",
                    "subtotal",
                    "shipping_charges",
                    "tax",
                    "total_price",
                ),
                arrayOf(user_id, subtotal, shipping_charges, tax, total_price)
            )
        } else {
            body = APIClient.createBuilder(
                arrayOf(
                    "user_id",
                    "subtotal",
                    "shipping_charges",
                    "tax",
                    "total_price",
                    "transaction_id",
                    "payment_type",
                    "payment_status",
                ),
                arrayOf(
                    user_id,
                    subtotal,
                    shipping_charges,
                    tax,
                    total_price,
                    transaction_id,
                    payment_type,
                    payment_status
                )
            )
        }
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("GenerateOrderId", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(
                            this@CheckoutActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                        val jsonObject1 = jsonObject.getJSONObject("data")
                        if (from.equals("button")) {
//                            razorpay_key_id = jsonObject1.getString("offer_id")
//                            startRazorPayment(
//                                (total.toDouble() * 100).toString(),
//                                jsonObject1.getString("id") //order_id
//                            )
                            initPayu()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                jsonObject.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        MyApplication.errorAlert(
                            this@CheckoutActivity,
                            jsonObject.optString("message", "Please try again later."),
                            "Order"
                        )
                    }
                } catch (e: JSONException) {
                    Log.e("generate_error", e.message.toString())
                }
            }

        }
        if (from.equals("button")) {
            APIClient(this).post(AppConstants.URL_ORDER, body, apiRequestListener, true)
        } else {
            APIClient(this).post(AppConstants.URL_UPDATE_ORDER, body, apiRequestListener, true)
        }
    }

    private fun startRazorPayment(amount: String, order_id: String) {
        val activity: Activity = this
        Log.e("amounttttt", amount);
        val checkout = Checkout()
        //rzp_test_fKQ4xzsS8IXGcO
        checkout.setKeyID("rzp_test_fKQ4xzsS8IXGcO")
        val jsonObj = JSONObject()
        try {
            jsonObj.put("name", "Enjoee Pharmacy")
            jsonObj.put("description", "Order Payment")
            jsonObj.put("theme.color", "#F57822")
            jsonObj.put("currency", "INR")
            jsonObj.put("order_id", order_id)
            jsonObj.put("amount", amount)
            jsonObj.put("prefill.contact", mobile)
            jsonObj.put("prefill.email", email)
            jsonObj.put("send_sms_hash", true);
            checkout.open(activity, jsonObj)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    private fun setError(editText: EditText?, message: String) {
        editText?.setError(message);
        editText?.requestFocus();
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        generateOrderId_API(
            user_id,
            subtotal,
            shipping_charges,
            tax,
            total,
            "payment",
            p1!!.paymentId,
            "online",
            p0.toString()
        )

    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
//        generateOrderId_API(
//            user_id,
//            subtotal,
//            shipping_charges,
//            tax,
//            total,
//            "payment",
//            p2!!.paymentId,
//            "online",
//            p1.toString()
//        )
    }

    fun initPayu() {
        open(
            this@CheckoutActivity,
            preparePayUBizParams()!!,
            getCheckoutProConfig()!!,
            object : PayUCheckoutProListener {
                override fun onPaymentSuccess(response: Any) {


                    /*Payu response :
"{\"id\":14169878208,\"mode\":\"UPI\",\"status\":\"success\",\"unmappedstatus\":\"captured\",
\"key\":\"Lslspr\",\"txnid\":\"1635228953555\",\"transaction_fee\":\"1.00\",\"amount\":\"1.00\",
\"discount\":\"0.00\",\"addedon\":\"2021-10-26 11:46:22\",\"productinfo\":\"Macbook Pro\",
\"firstname\":\"john\",\"email\":\"john@yopmail.com\",\"phone\":\"8952898584\",\"udf1\":\"udf1\",
\"udf2\":\"udf2\",\"udf3\":\"udf3\",\"udf4\":\"udf4\",\"udf5\":\"udf5\",
\"hash\":\"d5c6b0bff106d3ab748610115de69f906bbc401f7910c17c143685fe7edce08ab7e8d133e3b0536a5a11179d3e174b821a9e1626ae68447db84342f60ff9945b\",
\"field1\":\"utkarshtiwari40@okaxis\",\"field2\":\"38949413454\",\"field3\":\"utkarshtiwari40@okaxis\",\"field4\":\"UTKARSH TIWARI\",
\"field5\":\"HDF28508534BC864BB48852F6672924E659\",\"field6\":\"Punjab National Bank!7829000100030936!PUNB0782900!918952898584\",
\"field7\":\"APPROVED OR COMPLETED SUCCESSFULLY|00\",\"field9\":\"SUCCESS|Completed Using Callback\",\"payment_source\":\"payu\",
\"PG_TYPE\":\"UPI-PG\",\"bank_ref_no\":\"129921538790\",\"ibibo_code\":\"UPI\",\"error_code\":\"E000\",\"Error_Message\":\"No Error\",
\"is_seamless\":1,\"surl\":\"https://payuresponse.firebaseapp.com/success\",\"furl\":\"https://payuresponse.firebaseapp.com/failure\"}"


Payu Merchent response :
"{\"mihpayid\":\"14169878208\",\"mode\":\"UPI\",\"status\":\"success\",\"unmappedstatus\":\"captured\",
\"key\":\"Lslspr\",\"txnid\":\"1635228953555\",
\"amount\":\"1.00\",\"discount\":\"0.00\",\"net_amount_debit\":\"1\",\"addedon\":\"2021-10-26 11:46:22\",\"productinfo\":\"Macbook Pro\",\"firstname\":\"John\",
\"lastname\":\"\",\"address1\":\"\",\"address2\":\"\",\"city\":\"\",\"state\":\"\",\"country\":\"\",\"zipcode\":\"\",\"email\":\"john@yopmail.com\",
\"phone\":\"8952898584\",\"udf1\":\"udf1\",\"udf2\":\"udf2\",\"udf3\":\"udf3\",\"udf4\":\"udf4\",\"udf5\":\"udf5\",\"udf6\":\"\",\"udf7\":\"\",\"udf8\":\"\",
\"udf9\":\"\",\"udf10\":\"\",
\"hash\":\"d5c6b0bff106d3ab748610115de69f906bbc401f7910c17c143685fe7edce08ab7e8d133e3b0536a5a11179d3e174b821a9e1626ae68447db84342f60ff9945b\",
\"field1\":\"utkarshtiwari40@okaxis\",\"field2\":\"38949413454\",\"field3\":\"utkarshtiwari40@okaxis\",\"field4\":\"UTKARSH TIWARI\",
\"field5\":\"HDF28508534BC864BB48852F6672924E659\",\"field6\":\"Punjab National Bank!7829000100030936!PUNB0782900!918952898584\",
\"field7\":\"APPROVED OR COMPLETED SUCCESSFULLY|00\",\"field8\":\"\",\"field9\":\"SUCCESS|Completed Using Callback\",\"payment_source\":\"payu\",
\"meCode\":\"{\\\"pgMerchantId\\\":\\\"HDFC000000061461\\\",
\\\"encKey\\\":\\\"8b684b56d4032e3fcd6567748e000b38603f081ae8ce86daf36020c27f23a44f0ac360bb53ac217126f79e08551c8c2c\\\",
\\\"payu_aggregator\\\":\\\"1\\\"}\",\"PG_TYPE\":\"UPI-PG\",\"bank_ref_num\":\"129921538790\",\"bankcode\":\"UPI\",
\"error\":\"E000\",\"error_Message\":\"No Error\",\"device_type\":\"1\"}"*/


                    //Cast response object to HashMap
                    val result = response as HashMap<String, Any>
                    val payuResponse = result[CP_PAYU_RESPONSE] as String?
                    val merchantResponse = result[CP_MERCHANT_RESPONSE] as String?

//                        printResponse("Payuresponse", "Success Response Data", payuResponse);
//                        printResponse("PayuMerchanetresponse", "Success Response Data", merchantResponse);
                    try {
                        val payuobject = JSONObject(payuResponse)
                        //   JSONObject merchantobject = new JSONObject(merchantResponse);
                        Log.d("Txn Id", "onPaymentSuccess:DataSTR " + payuobject.getString("id"))

                        //Call Api...
//                        callBookNowApi(adon, payuobject.getString("id"))

                        generateOrderId_API(
                            user_id,
                            subtotal,
                            shipping_charges,
                            tax,
                            total,
                            "payment",
                            payuobject.getString("id"),
                            "online",
                           "successfull"
                        )
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onPaymentFailure(response: Any) {
                    Log.e("payment_failed",response.toString())
                    //Cast response object to HashMap
//                        HashMap<String, Object> result = (HashMap<String, Object>) response;
//                        String payuResponse = (String) result.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE);
//                        String merchantResponse = (String) result.get(PayUCheckoutProConstants.CP_MERCHANT_RESPONSE);
//                        printResponse("Payuresponse", "Failure Response Data", payuResponse);
//                        printResponse("PayuMerchanetresponse", "Failure Response Data", merchantResponse);
                    MyApplication.errorAlert(this@CheckoutActivity, "Payment Failed","Payment")
//                    generateOrderId_API(
//                        user_id,
//                        subtotal,
//                        shipping_charges,
//                        tax,
//                        total,
//                        "payment",
//                        p2!!.paymentId,
//                        "online",
//                        "failed"
//                    )
                }

                override fun onPaymentCancel(isTxnInitiated: Boolean) {
                    MyApplication.errorAlert(this@CheckoutActivity, "Payment Canceled","Payment")
                    //                        Toast.makeText(MainActivity.this, "Payment Canceled", Toast.LENGTH_SHORT).show();
                }

                override fun generateHash(
                    map: HashMap<String, String?>,
                    hashGenerationListener: PayUHashGenerationListener
                ) {
                        val hashName = map[CP_HASH_NAME]
                        val hashData = map[CP_HASH_STRING]
                        if (!TextUtils.isEmpty(hashName) && !TextUtils.isEmpty(hashData)) {
                            //Generate Hash from your backend here
                            var hash: String? = null

                            //Calculate SHA-512 Hash here
                            hash = MyApplication.getAppEnvironment()?.calculateHash(hashData + "T1u7u56p3TBIniGhCmxH8lrqwbt4AalF")
                            /*if (hashName.equalsIgnoreCase(PayUCheckoutProConstants.CP_LOOKUP_API_HASH)){
                                    //Calculate HmacSHA1 HASH for calculating Lookup API Hash
                                    ///Do not generate hash from local, it needs to be calculated from server side only. Here, hashString contains hash created from your server side.
                                    hash = calculateHmacSHA1Hash(hashData, testMerchantSecretKey);
                                } else {
    //Calculate SHA-512 Hash here
                                hash = calculateHash(hashData + "T1u7u56p3TBIniGhCmxH8lrqwbt4AalF");
                                }*/
                            val dataMap: HashMap<String, String?> = HashMap()
                            dataMap[hashName.toString()] = hash
                            hashGenerationListener.onHashGenerated(dataMap)
                        }

                }

                override fun onError(errorResponse: ErrorResponse) {
                    val errorMessage = errorResponse.errorMessage
                    MyApplication.errorAlert(this@CheckoutActivity, errorMessage.toString(),"Payment")
                    //                        Toast.makeText(MainActivity.this, " " + errorMessage, Toast.LENGTH_SHORT).show();
                }

                override fun setWebViewProperties(@Nullable webView: WebView?, @Nullable o: Any?) {
                    //For setting webview properties, if any. Check Customized Integration section for more details on this
                }


            }
        )
    }

    private fun preparePayUBizParams(): PayUPaymentParams? {
        val additionalParams: HashMap<String, Any?> = HashMap()
        additionalParams[CP_UDF1] = "udf1"
        additionalParams[CP_UDF2] = "udf2"
        additionalParams[CP_UDF3] = "udf3"
        additionalParams[CP_UDF4] = "udf4"
        additionalParams[CP_UDF5] = "udf5"
        var amount = 0.0
        try {
            amount = total
                .replace("INR", "").toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val mobile=SharedPreferenceUtility.getInstance()[AppConstants.PREF_MOB,""]
        val cc=SharedPreferenceUtility.getInstance()[AppConstants.PREF_COUNTRY_CODE,""]
        val name=SharedPreferenceUtility.getInstance()[AppConstants.PREF_FULL_NAME,""]
        val email=SharedPreferenceUtility.getInstance()[AppConstants.PREF_EMAIL,""]

        val builder = PayUPaymentParams.Builder()
        builder.setAmount(amount.toString())
            .setIsProduction(true)
            .setProductInfo("Pay For Packages")
            .setKey(MyApplication.getAppEnvironment()?.merchant_Key())
            .setPhone(mobile.toString())
            .setTransactionId(System.currentTimeMillis().toString())
            .setFirstName(name)
            .setEmail(email)
            .setSurl("https://payuresponse.firebaseapp.com/success")
            .setFurl("https://payuresponse.firebaseapp.com/failure")
            .setUserCredential(
                MyApplication.getAppEnvironment()?.merchant_Key()
                    .toString() + ":" + email
            )
            .setAdditionalParams(additionalParams)
        return builder.build()
    }

    private fun getCheckoutProConfig(): PayUCheckoutProConfig? {
        val checkoutProConfig = PayUCheckoutProConfig()
        checkoutProConfig.showCbToolbar = false
        checkoutProConfig.autoSelectOtp = true
        checkoutProConfig.autoApprove = true
        checkoutProConfig.showExitConfirmationOnPaymentScreen = true
        checkoutProConfig.showExitConfirmationOnCheckoutScreen = true
        checkoutProConfig.merchantName = "Enjoee Pharmacy"
        checkoutProConfig.merchantLogo = R.drawable.logo
        checkoutProConfig.waitingTime = 30000
        checkoutProConfig.merchantResponseTimeout = 30000
        //checkoutProConfig.setPaymentModesOrder(getCheckoutOrderList());
        //checkoutProConfig.setOfferDetails(getOfferDetailsList());
//        checkoutProConfig.setEnforcePaymentList(getEnforcePaymentList());
        //    checkoutProConfig.setSurePayCount(Integer.parseInt(binding.etSurePayCount.getText().toString()));
        //  checkoutProConfig.setCustomNoteDetails(getCustomeNoteList());
        return checkoutProConfig
    }


}