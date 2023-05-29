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
import com.bestoffers.enjoeepharmacy.Adapters.RelatedIdProductAdapter
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.Models.DataProductList
import com.bestoffers.enjoeepharmacy.R
import com.bestoffers.enjoeepharmacy.Utils.SharedPreferenceUtility
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_bottom_base.*
import kotlinx.android.synthetic.main.activity_related_id_product.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

class RelatedIdProductActivity : AppCompatActivity() {

    var dataProductList = ArrayList<DataProductList>();
    var relatedIdProductAdapter: RelatedIdProductAdapter? = null
    var pageNo = 1
    var isLastPage = false
    var prod_id = ""
    var prod_name = ""
    var user_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_related_id_product)

        val intentt = intent?.extras;
        if (intentt != null) {
            prod_id = intentt.getString("prod_id").toString()
            prod_name = intentt.getString("prod_name").toString()
        }

        user_id = SharedPreferenceUtility.getInstance().get(AppConstants.PREF_USER_ID)
        initView()
        dataProductList.clear()
        relatedIdProductAdapter!!.notifyDataSetChanged()
        relatedIDProduct_API()

    }

    private fun relatedIDProduct_API() {
        val body = APIClient.createBuilder(
            arrayOf("product_id", "page"),
            arrayOf(prod_id, pageNo.toString())
        )
        Log.e("RelatedIdProduct ", " product_id "+prod_id)
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("RelatedIdProduct", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
//                        Toast.makeText(
//                            this@RelatedIdProductActivity,
//                            jsonObject.getString("message"),
//                            Toast.LENGTH_LONG
//                        ).show()

                        val gson = Gson()
                        var data = jsonObject.getJSONObject("data")
                        relatedIdProductAdapter?.notifyDataSetChanged()
                        val listType: Type =
                            object : TypeToken<List<DataProductList?>?>() {}.getType()
                        var data2 = data.getJSONArray("data").toString()

                        var list: ArrayList<DataProductList> = gson.fromJson(data2, listType)
                        dataProductList.addAll(list)
                        relatedIdProductAdapter?.notifyDataSetChanged()

                        Log.e("relatedIdProductlist", list.size.toString())

                        if (list.size < 1) {
//                            iv_null.visibility = View.VISIBLE
                            rv_related_id_product.visibility = View.GONE
                            ll_empty.visibility = View.VISIBLE

                        } else {
//                            iv_null.visibility = View.GONE
                            rv_related_id_product.visibility = View.VISIBLE
                            ll_empty.visibility = View.GONE

                        }

                        if (jsonObject.getJSONObject("data").getInt("last_page") == pageNo) {
                            isLastPage = true
                        }

                    } else {
                        rv_related_id_product.visibility = View.GONE
                        ll_empty.visibility = View.VISIBLE
//                        MyApplication.errorAlert(
//                            this@RelatedIdProductActivity,
//                            jsonObject.optString("message", "Please try again later."),
//                            "Error"
//                        )
                    }
                } catch (e: JSONException) {
                    Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    Log.e("RelatedIdProduct_error", e.message.toString())
                }
            }
        }
        if (!isLastPage) {
            APIClient(this).post(
                AppConstants.URL_RELATED_ID_PRODUCT,
                body,
                apiRequestListener,
                true
            )
        }
    }

    private fun initView() {

        toolbarTitle.setText(prod_name)

        iv_cart.setOnClickListener {
            startActivity(Intent(this@RelatedIdProductActivity, CartActivity::class.java));
        }

        iv_back.visibility = View.VISIBLE
        iv_hamburger.visibility = View.GONE
        iv_cart.visibility = View.GONE

        iv_back.setOnClickListener {
            onBackPressed()
        }

        iv_hamburger.setOnClickListener {
            fullLayout?.openDrawer(Gravity.LEFT);
        }

        relatedIdProductAdapter = RelatedIdProductAdapter(this, dataProductList);
        rv_related_id_product?.layoutManager = GridLayoutManager(this@RelatedIdProductActivity, 2)
        rv_related_id_product?.adapter = relatedIdProductAdapter;

        rv_related_id_product.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        (rv_related_id_product.adapter as RelatedIdProductAdapter).itemCount.toString()
                    )
                    Log.e("lastVisiblePosition", lastVisiblePosition.toString())

                    if (lastVisiblePosition == (rv_related_id_product.adapter as RelatedIdProductAdapter).itemCount - 1) {
                        pageNo++
                        relatedIDProduct_API()
                    }
                }
            }
        })

    }

    public fun addToCart(
        vendorClickId: String,
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
            arrayOf(user_id, vendorClickId, vendorProductId, productId, quantity, prescription)
        )
        Log.e("AddToCart", "user_id "+user_id+" vendorClickId "+vendorClickId+" vendorProductId "+vendorProductId+" productId "+productId+" quantity "+quantity+" prescription "+prescription)
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("AddToCart", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(
                            this@RelatedIdProductActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        MyApplication.errorAlert(
                            this@RelatedIdProductActivity,
                            jsonObject.optString("message", "Please try again later."),
                            "Error"
                        )
                    }
                } catch (e: JSONException) {
                    Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    Log.e("AddToCart_error", e.message.toString())
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