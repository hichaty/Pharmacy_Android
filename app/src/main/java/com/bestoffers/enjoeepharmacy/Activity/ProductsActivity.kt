package com.bestoffers.enjoeepharmacy.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.bestoffers.enjoeepharmacy.APIClient
import com.bestoffers.enjoeepharmacy.APIRequestListener
import com.bestoffers.enjoeepharmacy.Adapters.HomeAdapter
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.Models.DataProductList
import com.bestoffers.enjoeepharmacy.R
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_products.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

class ProductsActivity : AppCompatActivity() {
    var cat_id="";
    var cat_name=""
    var productsList = ArrayList<DataProductList>();
    var homeAdapter: HomeAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val intentt=intent?.extras;
        if(intentt!=null){
            cat_id=intentt.getString("cat_id").toString();
            cat_name=intentt.getString("cat_name").toString();
        }

        initView()
        getProducts_API(cat_id);

    }

    private fun initView() {
        iv_hamburger.visibility = View.GONE;
        iv_cart.visibility = View.GONE;
        iv_back.visibility = View.VISIBLE;

        iv_back.setOnClickListener {
            onBackPressed()
        }
        toolbarTitle.setText(cat_name)

        homeAdapter = HomeAdapter(this, productsList, "product");
        rv_products?.layoutManager = GridLayoutManager(this@ProductsActivity, 2);
        rv_products?.adapter = homeAdapter;

    }

    private fun getProducts_API(cat_id: String) {
        val body = APIClient.createBuilder(
            arrayOf("cat_id"),
            arrayOf(cat_id)
        )
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("GetProducts", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
//                        Toast.makeText(
//                            this@ProductsActivity,
//                            jsonObject.getString("message"),
//                            Toast.LENGTH_LONG
//                        ).show()

                        productsList.clear();
                        val gson = Gson()
                        val data=jsonObject.getJSONObject("data")
                        homeAdapter?.notifyDataSetChanged()
                        val listType: Type =
                            object : TypeToken<List<DataProductList?>?>() {}.getType()
                        var data2 = data.getJSONArray("data").toString()
                        var list: ArrayList<DataProductList> = gson.fromJson(data2, listType)
                        productsList.addAll(list)
                        homeAdapter?.notifyDataSetChanged()

                        if(list.size>0){
                            tv_no_products.visibility=View.GONE
                            rv_products.visibility=View.VISIBLE
                        }else{
                            tv_no_products.visibility=View.VISIBLE
                            rv_products.visibility=View.GONE
                        }

                    } else {
                        tv_no_products.visibility=View.VISIBLE
                        rv_products.visibility=View.GONE
//                        MyApplication.errorAlert(
//                            this@ProductsActivity,
//                            jsonObject.optString("message", "Please try again later."),
//                            "Products List"
//                        )
                    }
                } catch (e: JSONException) {
                    Log.e("get_prod_error", e.message.toString())
                }
            }

        }
        APIClient(this).post(AppConstants.URL_GET_PRODUCTS, body, apiRequestListener, true)

    }

}