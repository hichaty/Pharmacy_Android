package com.bestoffers.enjoeepharmacy.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bestoffers.enjoeepharmacy.APIClient
import com.bestoffers.enjoeepharmacy.APIRequestListener
import com.bestoffers.enjoeepharmacy.Adapters.CartAdapter
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.Models.DataProductList
import com.bestoffers.enjoeepharmacy.R
import com.bestoffers.enjoeepharmacy.Utils.SharedPreferenceUtility
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_bottom_base.*
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

class CartActivity : AppCompatActivity() {
    public var dataProductList = ArrayList<DataProductList>();
    public var cartAdapter: CartAdapter? = null
    var pageNo = 1
    var isLastPage = false
    var user_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        initView()
        user_id = SharedPreferenceUtility.getInstance().get(AppConstants.PREF_USER_ID, "")

        dataProductList.clear()
        cartAdapter?.notifyDataSetChanged()
        getCart_API();

    }

    private fun getCart_API() {

        val body = APIClient.createBuilder(
            arrayOf("user_id","page"),
            arrayOf(user_id,pageNo.toString())
        )
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("GetCart", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
//                        Toast.makeText(
//                            this@ProductsActivity,
//                            jsonObject.getString("message"),
//                            Toast.LENGTH_LONG
//                        ).show()

                        val gson = Gson()
                        val data = jsonObject.getJSONObject("data")
                        val record = jsonObject.getJSONObject("record")
                        val listType: Type =
                            object : TypeToken<List<DataProductList?>?>() {}.getType()
                        var data2 = data.getJSONArray("data").toString()
                        var list: ArrayList<DataProductList> = gson.fromJson(data2, listType)
                        dataProductList.addAll(list)
                        cartAdapter?.notifyDataSetChanged()

                        if (list.size > 0) {
                            ll_empty.visibility = View.GONE
                            rv_cart.visibility = View.VISIBLE
                            rl_amount.visibility = View.VISIBLE
                            tv_rules.visibility = View.VISIBLE
                        } else {
                            ll_empty.visibility = View.VISIBLE
                            rv_cart.visibility = View.GONE
                            rl_amount.visibility = View.GONE
                            tv_rules.visibility = View.GONE
                        }

                        tv_sub_total.setText(record.getString("sub_total").toString())
                        tv_shipping_charges.setText(record.getInt("shipping_charges").toString())
                        tv_tax.setText(record.getInt("tax").toString())
                        tv_net_amount.setText(record.getString("net_amount").toString())

                        if (data.getInt("last_page") == pageNo) {
                            isLastPage = true
                        }

                    } else {
                        ll_empty.visibility = View.VISIBLE
                        rv_cart.visibility = View.GONE
                        rl_amount.visibility = View.GONE
                        tv_rules.visibility = View.GONE
//                        MyApplication.errorAlert(
//                            this@CartActivity,
//                            jsonObject.optString("message", "Please try again later."),
//                            "Cart"
//                        )
                    }
                } catch (e: JSONException) {
                    Log.e("get_cart_error", e.message.toString())
                }
            }

        }
        if (!isLastPage) {
            APIClient(this).post(AppConstants.URL_GET_CART, body, apiRequestListener, true)
        }
    }

    private fun initView() {
        cartAdapter = CartAdapter(dataProductList, this);
        rv_cart?.layoutManager =
            LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false);
        rv_cart?.adapter = cartAdapter;

        btn_checkout.setOnClickListener {
            if (dataProductList.size > 0) {
                val intent = Intent(this@CartActivity, CheckoutActivity::class.java);
                intent.putExtra("subtotal", tv_sub_total.text.toString())
                intent.putExtra("shipping_charges", tv_shipping_charges.text.toString())
                intent.putExtra("tax", tv_tax.text.toString())
                intent.putExtra("total", tv_net_amount.text.toString())
                startActivity(intent);
            }else{
                Toast.makeText(applicationContext,"No items in Cart",Toast.LENGTH_SHORT).show()
            }
        }

        iv_hamburger.visibility = View.GONE;
        iv_cart.visibility = View.GONE;
        iv_back.visibility = View.VISIBLE;

        iv_back.setOnClickListener {
            onBackPressed()
        }

        iv_hamburger.setOnClickListener {
            fullLayout?.openDrawer(Gravity.LEFT);
        }

        toolbarTitle.setText("My Cart")

        rv_cart.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) //check for scroll down
                {
                    val layoutManager: LinearLayoutManager =
                        recyclerView.getLayoutManager() as LinearLayoutManager
                    val lastVisiblePosition: Int =
                        layoutManager.findLastCompletelyVisibleItemPosition()
                    Log.e(
                        "itemCount",
                        (rv_cart.adapter as CartAdapter).itemCount.toString()
                    )
                    Log.e("lastVisiblePosition", lastVisiblePosition.toString())

                    if (lastVisiblePosition == (rv_cart.adapter as CartAdapter).itemCount - 1) {
                        pageNo++
                        getCart_API()
                    }
                }
            }
        })

    }

    public fun updateCart_API(cart_id: String, quantity: String, prescription: String) {
        val body = APIClient.createBuilder(
            arrayOf("cart_id", "quantity", "prescription"),
            arrayOf(cart_id, quantity, prescription)
        )
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("UpdateCart", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(
                            this@CartActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                        dataProductList.clear()
                        cartAdapter?.notifyDataSetChanged()
                        pageNo = 1
                        getCart_API()

                    } else {
                        MyApplication.errorAlert(
                            this@CartActivity,
                            jsonObject.optString("message", "Please try again later."),
                            "Update Cart"
                        )
                    }
                } catch (e: JSONException) {
                    Log.e("upadte_cart_error", e.message.toString())
                }
            }

        }
        APIClient(this).post(AppConstants.URL_UPDATE_CART, body, apiRequestListener, true)

    }


    public fun removeItemCart_API(cart_id: String) {
        val body = APIClient.createBuilder(
            arrayOf("cart_id"),
            arrayOf(cart_id)
        )
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("RemoveItemCart", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(
                            this@CartActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                        this@CartActivity.recreate()

                    } else {
                        MyApplication.errorAlert(
                            this@CartActivity,
                            jsonObject.optString("message", "Please try again later."),
                            "Remove Cart Item"
                        )
                    }
                } catch (e: JSONException) {
                    Log.e("RemoveItemCart_error", e.message.toString())
                }
            }

        }
        APIClient(this).post(AppConstants.URL_CART_REMOVE_PRODUCT, body, apiRequestListener, true)

    }

}