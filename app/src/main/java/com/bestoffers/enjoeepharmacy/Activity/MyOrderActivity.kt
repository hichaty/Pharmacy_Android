package com.bestoffers.enjoeepharmacy.Activity

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bestoffers.enjoeepharmacy.APIClient
import com.bestoffers.enjoeepharmacy.APIRequestListener
import com.bestoffers.enjoeepharmacy.Adapters.MyOrderMainAdapter
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.Models.*
import com.bestoffers.enjoeepharmacy.R
import com.bestoffers.enjoeepharmacy.Utils.SharedPreferenceUtility
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_my_order.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

class MyOrderActivity : BottomBaseActivity() {

    var getOrderModelDataList = ArrayList<GetOrderModelSubData>();
    var myOrderMainAdapter: MyOrderMainAdapter? = null

    var pageNo = 1
    var isLastPage = false
    var user_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order)

        user_id = SharedPreferenceUtility.getInstance().get(AppConstants.PREF_USER_ID, "")
        initView()
        getOrderModelDataList.clear()
        myOrderMainAdapter?.notifyDataSetChanged()
        getOrder_API()

    }

    private fun getOrder_API() {
        val body = APIClient.createBuilder(
            arrayOf("user_id","page"),
            arrayOf(user_id,pageNo.toString())
        )
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("GetOrder", response)
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
                        val listType: Type =
                            object : TypeToken<List<GetOrderModelSubData?>?>() {}.getType()
                        var data2 = data.getJSONArray("data").toString()
                        var list: ArrayList<GetOrderModelSubData> = gson.fromJson(data2, listType)
                        getOrderModelDataList.addAll(list)
                        myOrderMainAdapter?.notifyDataSetChanged()

                        if (list.size > 0) {
                            tv_no_products.visibility = View.GONE
                            rv_my_order.visibility = View.VISIBLE
                        } else {
                            tv_no_products.visibility = View.VISIBLE
                            rv_my_order.visibility = View.GONE
                        }

                        if (data.getInt("last_page") == pageNo) {
                            isLastPage = true
                        }

                    } else {
                        tv_no_products.visibility = View.VISIBLE
                        rv_my_order.visibility = View.GONE
//                        MyApplication.errorAlert(
//                            this@MyOrderActivity,
//                            jsonObject.optString("message", "Please try again later."),
//                            "My Order"
//                        )
                    }
                } catch (e: JSONException) {
                    Log.e("get_order_error", e.message.toString())
                }
            }

        }
        if (!isLastPage) {
            APIClient(this).post(AppConstants.URL_GET_ORDER, body, apiRequestListener, true)
        }
    }

    private fun initView() {

        myOrderMainAdapter = MyOrderMainAdapter(getOrderModelDataList, applicationContext);
        rv_my_order?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_my_order?.adapter = myOrderMainAdapter;

        iv_hamburger.setOnClickListener {
            fullLayout?.openDrawer(Gravity.LEFT);
        }
        iv_cart.setOnClickListener {
            startActivity(Intent(this@MyOrderActivity, CartActivity::class.java));
        }

        toolbarTitle.setText("My Order")

        rv_my_order.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        (rv_my_order.adapter as MyOrderMainAdapter).itemCount.toString()
                    )
                    Log.e("lastVisiblePosition", lastVisiblePosition.toString())

                    if (lastVisiblePosition == (rv_my_order.adapter as MyOrderMainAdapter).itemCount - 1) {
                        pageNo++
                        getOrder_API()
                    }
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        imgNearStore.colorFilter = null
        imgMyOrder.setColorFilter(
            ContextCompat.getColor(
                this@MyOrderActivity,
                R.color.pink
            ), PorterDuff.Mode.SRC_IN
        )
        imgHome.setColorFilter(
            ContextCompat.getColor(
                this@MyOrderActivity,
                R.color.black
            ), PorterDuff.Mode.SRC_IN
        )
        imgProfile.setColorFilter(null)
        imgAlert.setColorFilter(null)

//        layoutHome!!.setBackgroundColor(Color.parseColor("#ffffff"))
//        layoutPrescription!!.setBackgroundColor(Color.parseColor("#ffffff"))
//        layoutProfile!!.setBackgroundColor(Color.parseColor("#ffffff"))
//        layoutMyOrder!!.setBackgroundColor(Color.parseColor("#ED8701"))
//        layoutAlert!!.setBackgroundColor(Color.parseColor("#ffffff"))

        tvHome!!.setTextColor(Color.parseColor("#000000"))
        tvNearStore!!.setTextColor(Color.parseColor("#000000"))
        tvProfile!!.setTextColor(Color.parseColor("#000000"))
        tvMyOrder!!.setTextColor(Color.parseColor("#EC99BA"))
        tvAlert!!.setTextColor(Color.parseColor("#000000"))
    }
}