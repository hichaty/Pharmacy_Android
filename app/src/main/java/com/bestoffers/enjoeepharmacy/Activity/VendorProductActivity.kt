package com.bestoffers.enjoeepharmacy.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bestoffers.enjoeepharmacy.APIClient
import com.bestoffers.enjoeepharmacy.APIRequestListener
import com.bestoffers.enjoeepharmacy.Adapters.VendorProductAdapter
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.Models.DataProductList
import com.bestoffers.enjoeepharmacy.R
import com.bestoffers.enjoeepharmacy.Utils.SharedPreferenceUtility
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_bottom_base.*
import kotlinx.android.synthetic.main.activity_vendor_product.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

class VendorProductActivity : AppCompatActivity() {

    var dataProductList = ArrayList<DataProductList>();
    var vendorProductAdapter: VendorProductAdapter? = null
    var pageNo = 1
    var isLastPage = false
    var vendor_id = ""
    var store_name = ""
    var user_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_product)

        val intentt = intent?.extras;
        if (intentt != null) {
            vendor_id = intentt.getString("vendor_id").toString()
            store_name = intentt.getString("store_name").toString()
        }

        user_id = SharedPreferenceUtility.getInstance().get(AppConstants.PREF_USER_ID)
        initView()
        dataProductList.clear()
        vendorProductAdapter!!.notifyDataSetChanged()
        vendorProduct_API()

    }

    private fun initView() {

        toolbarTitle.setText(store_name)

        iv_cart.setOnClickListener {
            startActivity(Intent(this@VendorProductActivity, CartActivity::class.java));
        }

        iv_hamburger.visibility = View.GONE
        iv_cart.visibility = View.GONE
        iv_back.visibility = View.VISIBLE

        iv_back.setOnClickListener { onBackPressed() }


        iv_hamburger.setOnClickListener {
            fullLayout?.openDrawer(Gravity.LEFT);
        }

        vendorProductAdapter = VendorProductAdapter(this, dataProductList);
        rv_vendor_product?.layoutManager = GridLayoutManager(this@VendorProductActivity, 2)
        rv_vendor_product?.adapter = vendorProductAdapter;

        rv_vendor_product.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) //check for scroll down
                {
                    val layoutManager: GridLayoutManager =
                        recyclerView.getLayoutManager() as GridLayoutManager
                    val lastVisiblePosition: Int =
                        layoutManager.findLastCompletelyVisibleItemPosition()
                    Log.e(
                        "itemCount",
                        (rv_vendor_product.adapter as VendorProductAdapter).itemCount.toString()
                    )
                    Log.e("lastVisiblePosition", lastVisiblePosition.toString())

                    if (lastVisiblePosition == (rv_vendor_product.adapter as VendorProductAdapter).itemCount - 1) {
                        pageNo++
                        vendorProduct_API()
                    }
                }
            }
        })

    }

    private fun vendorProduct_API() {
        val body = APIClient.createBuilder(
            arrayOf("vendor_id", "page"),
            arrayOf(vendor_id, pageNo.toString())
        )
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("VendorProduct", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
//                        Toast.makeText(
//                            this@VendorProductActivity,
//                            jsonObject.getString("message"),
//                            Toast.LENGTH_LONG
//                        ).show()

                        val gson = Gson()
                        var data = jsonObject.getJSONObject("data")
                        vendorProductAdapter?.notifyDataSetChanged()
                        val listType: Type =
                            object : TypeToken<List<DataProductList?>?>() {}.getType()
                        var data2 = data.getJSONArray("data").toString()
                        var list: ArrayList<DataProductList> = gson.fromJson(data2, listType)
                        dataProductList.addAll(list)
                        vendorProductAdapter?.notifyDataSetChanged()

                        if (list.size < 1) {
                            ll_empty.visibility = View.VISIBLE
                            rv_vendor_product.visibility = View.GONE

                        } else {
                            ll_empty.visibility = View.GONE
                            rv_vendor_product.visibility = View.VISIBLE

                        }

                        if (jsonObject.getJSONObject("data").getInt("last_page") == pageNo) {
                            isLastPage = true
                        }

                    } else {
                        ll_empty.visibility = View.VISIBLE
                        rv_vendor_product.visibility = View.GONE
//                        MyApplication.errorAlert(
//                            this@VendorProductActivity,
//                            jsonObject.optString("message", "Please try again later."),
//                            "Error"
//                        )
                    }
                } catch (e: JSONException) {
                    Log.e("VendorProduct_error", e.message.toString())
                }
            }
        }
        if (!isLastPage) {
            APIClient(this).post(
                AppConstants.URL_VENDOR_PRODUCT,
                body,
                apiRequestListener,
                true
            )
        }
    }

    public fun addToCart(
        vendorId: String,
        vendorProductId: String,
        productId: String,
        quantity: String,
        prescription: String
    ) {
        val body = APIClient.createBuilder(
            arrayOf(
                "user_id",
                "vendor_id",
                "vendor_product_id",
                "product_id",
                "quantity",
                "prescription"
            ),
            arrayOf(user_id, vendorId, vendorProductId, productId, quantity, prescription)
        )
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("VendorProduct", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(
                            this@VendorProductActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        MyApplication.errorAlert(
                            this@VendorProductActivity,
                            jsonObject.optString("message", "Please try again later."),
                            "Error"
                        )
                    }
                } catch (e: JSONException) {
                    Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    Log.e("VendorProduct_error", e.message.toString())
                }
            }
        }
        APIClient(this).post(
            AppConstants.URL_ADD_TO_CART,
            body,
            apiRequestListener,
            true
        )

    }


}