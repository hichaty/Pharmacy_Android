package com.bestoffers.enjoeepharmacy.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bestoffers.enjoeepharmacy.APIClient
import com.bestoffers.enjoeepharmacy.APIRequestListener
import com.bestoffers.enjoeepharmacy.Adapters.HomeAdapter
import com.bestoffers.enjoeepharmacy.Adapters.SpinnerArrayAdapter
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.Models.DataCategories
import com.bestoffers.enjoeepharmacy.Models.HomeModel
import com.bestoffers.enjoeepharmacy.R
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type
import android.widget.AdapterView

import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.bestoffers.enjoeepharmacy.Adapters.CategoriesHomeAdapter
import com.bestoffers.enjoeepharmacy.Models.DataProductList
import com.bestoffers.enjoeepharmacy.Utils.SharedPreferenceUtility


class HomeActivity : BottomBaseActivity() {

    var productsList = ArrayList<DataProductList>();
    var categoriesList = ArrayList<DataCategories>();
    var categoriesListSpinner = ArrayList<DataCategories>();
    var spinnerArrayAdapter: SpinnerArrayAdapter? = null
    var categoriesHomeAdapter: CategoriesHomeAdapter? = null
    var homeAdapter: HomeAdapter? = null
    var pageNo = 1
    var category_id = ""
    var search_keyword = ""
    var isLastPage = false
    var requestcode = 101
    var user_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        user_id = SharedPreferenceUtility.getInstance().get(AppConstants.PREF_USER_ID)
        var name = SharedPreferenceUtility.getInstance().get(AppConstants.PREF_FULL_NAME,"")
        Log.e("name",name.toString()+"::::::"+user_id.toString())
        searchView.clearFocus()
        initView()
        getCategoriesList_API();
        productsList.clear();
        homeAdapter?.notifyDataSetChanged()
        getProducts_API()
        setListeners()
        isLocationPermissionGranted()

    }

    private fun setListeners() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val cat_id = categoriesListSpinner.get(i).id.toString()
                if (!cat_id.equals("0")) {
                    pageNo = 1
                    category_id = cat_id
                    isLastPage = false
                    productsList.clear();
                    homeAdapter?.notifyDataSetChanged()
                    getProducts_API()
                } else {
                    pageNo = 1
                    search_keyword = ""
                    category_id = ""
                    isLastPage = false
                    productsList.clear();
                    homeAdapter?.notifyDataSetChanged()
                    getProducts_API()
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (!TextUtils.isEmpty(p0)) {
                    pageNo = 1
                    search_keyword = p0?.trim().toString()
                    isLastPage = false
                    productsList.clear();
                    homeAdapter?.notifyDataSetChanged()
                    getProducts_API()
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                //binding.root.findNavController().navigate(SearchFragmentDirections.actionMoreFragmentToReportFragment(""))
                et_search.visibility = View.GONE
//                iv_close.visibility = View.VISIBLE
//                if(p0?.length!! >0){
//                    searchView.isIconified=true
//                }
                return true
            }

        })

        iv_close.setOnClickListener {
            searchView.setQuery("", false)
            searchView.clearFocus()
            pageNo = 1
            search_keyword = ""
            isLastPage = false
            productsList.clear();
            homeAdapter?.notifyDataSetChanged()
//            et_search.visibility = View.VISIBLE
            iv_close.visibility = View.GONE
            getProducts_API()
            searchView.isIconified = false
        }

        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                pageNo = 1
                search_keyword = ""
                isLastPage = false
                productsList.clear();
                homeAdapter?.notifyDataSetChanged()
                getProducts_API()
//                et_search.visibility = View.VISIBLE
                searchView.isIconified = false
                return false
            }
        })

        rv_home_products.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        (rv_home_products.adapter as HomeAdapter).itemCount.toString()
                    )
                    Log.e("lastVisiblePosition", lastVisiblePosition.toString())

                    if (lastVisiblePosition == (rv_home_products.adapter as HomeAdapter).itemCount - 1) {
                        pageNo++
                        getProducts_API()
                    }
                }
            }
        })

    }

    private fun getProductsCategoryId_API(cat_id: String) {
        val body = APIClient.createBuilder(
            arrayOf("category_id"),
            arrayOf(cat_id)
        )
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("GetProductsCategoryId", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
//                        Toast.makeText(
//                            this@HomeActivity,
//                            jsonObject.getString("message"),
//                            Toast.LENGTH_LONG
//                        ).show()

                        productsList.clear();
                        val gson = Gson()
                        homeAdapter?.notifyDataSetChanged()
                        val listType: Type =
                            object : TypeToken<List<DataProductList?>?>() {}.getType()
                        var data = jsonObject.getJSONArray("data").toString()
                        var list: ArrayList<DataProductList> = gson.fromJson(data, listType)
                        productsList.addAll(list)
                        homeAdapter?.notifyDataSetChanged()

                        if (list.size < 1) {
//                            iv_null.visibility = View.VISIBLE
                            rv_home_products.visibility = View.GONE
//                            rv_home_categories.visibility = View.VISIBLE

                        } else {
//                            iv_null.visibility = View.GONE
                            rv_home_products.visibility = View.VISIBLE
//                            rv_home_categories.visibility = View.GONE

                        }
                    } else {
                        MyApplication.errorAlert(
                            this@HomeActivity,
                            jsonObject.optString("message", "Please try again later."),
                            "Products List"
                        )
                    }
                } catch (e: JSONException) {
                    Log.e("get_prod_error", e.message.toString())
                }
            }

        }
        APIClient(this).post(AppConstants.URL_GET_PRODUCTS, body, apiRequestListener, true)

    }

    private fun getProducts_API() {
        var hashMap = HashMap<String, String>()
        hashMap["page"] = pageNo.toString()

        if (!TextUtils.isEmpty(category_id)) {
            hashMap["category_id"] = category_id.toString()
        }
        if (!TextUtils.isEmpty(search_keyword)) {
            hashMap["search"] = search_keyword.toString()
        }

        var body = APIClient.createHashMapBuilder(
            hashMap
        )
//        var body : FormBody.Builder? =null;
//
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("GetProducts", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
//                        Toast.makeText(
//                            this@HomeActivity,
//                            jsonObject.getString("message"),
//                            Toast.LENGTH_LONG
//                        ).show()

//                        pageNo = jsonObject.getJSONObject("data").getInt("current_page") + 1
//                        var data = jsonObject.getJSONObject("data")


                        val gson = Gson()
                        var mMineUserEntity = gson?.fromJson(response, HomeModel::class.java)
                        Log.e(
                            "categoryName",
                            mMineUserEntity.data!!.dataProductList.get(0).productClickId.toString()
                        )

//                        val listType: Type =
//                            object : TypeToken<List<DataProductList?>?>() {}.getType()
//                        var data2=data.getJSONArray("data").toString()
//                        var list: ArrayList<DataProductList> = gson.fromJson(data2, listType)
//                        productsList.addAll(list)
//                        homeAdapter?.notifyDataSetChanged()


                        var list = ArrayList<DataProductList>()
                        list.addAll(mMineUserEntity.data!!.dataProductList)
                        productsList.addAll(mMineUserEntity.data!!.dataProductList)
                        homeAdapter?.notifyDataSetChanged()
                        if (list.size < 1) {
                            iv_empty.visibility = View.VISIBLE
                            rv_home_products.visibility = View.GONE
//                            rv_home_categories.visibility = View.VISIBLE

                        } else {
                            iv_empty.visibility = View.GONE
                            rv_home_products.visibility = View.VISIBLE
//                            rv_home_categories.visibility = View.GONE

                        }

                        if (jsonObject.getJSONObject("data").getInt("last_page") == pageNo) {
                            isLastPage = true
                        }

                    } else {
                        iv_empty.visibility = View.VISIBLE
                        rv_home_products.visibility = View.GONE
//                        MyApplication.errorAlert(
//                            this@HomeActivity,
//                            jsonObject.optString("message", "Please try again later."),
//                            "Products List"
//                        )
                    }
                } catch (e: JSONException) {
                    Log.e("get_prod_error", e.message.toString())
                }
            }

        }
        if (!isLastPage) {
            APIClient(this).post(AppConstants.URL_GET_PRODUCTS, body, apiRequestListener, true)
        }
    }

    private fun getCategoriesList_API() {
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("GetCategories", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
//                        Toast.makeText(
//                            this@HomeActivity,
//                            jsonObject.getString("message"),
//                            Toast.LENGTH_LONG
//                        ).show()
                        categoriesList.clear();
                        categoriesListSpinner.clear();
                        val gson = Gson()
                        spinnerArrayAdapter?.notifyDataSetChanged()
                        categoriesHomeAdapter?.notifyDataSetChanged()
                        val listType: Type =
                            object : TypeToken<List<DataCategories?>?>() {}.getType()
                        var data = jsonObject.getJSONArray("data").toString()
                        var list: ArrayList<DataCategories> = gson.fromJson(data, listType)
                        categoriesList.addAll(list)
                        categoriesListSpinner.addAll(list)
                        categoriesListSpinner.add(
                            0,
                            DataCategories(0, "Category", "")
                        );
                        spinnerArrayAdapter?.notifyDataSetChanged()
                        categoriesHomeAdapter?.notifyDataSetChanged()
                    } else {
                        MyApplication.errorAlert(
                            this@HomeActivity,
                            jsonObject.optString("message", "Please try again later."),
                            "Categories List"
                        )
                    }
                } catch (e: JSONException) {
                    Log.e("get_cat_error", e.message.toString())
                }
            }

        }
        APIClient(this).get(AppConstants.URL_GET_CATEGORIES, apiRequestListener, true)

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
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("RelatedIdProduct", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(
                            this@HomeActivity,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        MyApplication.errorAlert(
                            this@HomeActivity,
                            jsonObject.optString("message", "Please try again later."),
                            "Error"
                        )
                    }
                } catch (e: JSONException) {
                    Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    Log.e("RelatedIdProduct_error", e.message.toString())
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

    private fun initView() {
        homeAdapter = HomeAdapter(this, productsList, "home");
        rv_home_products?.layoutManager = GridLayoutManager(this@HomeActivity, 2);
        rv_home_products?.adapter = homeAdapter;

        categoriesHomeAdapter = CategoriesHomeAdapter(this, categoriesList);
        rv_home_categories?.layoutManager = GridLayoutManager(this@HomeActivity, 2);
        rv_home_categories?.adapter = categoriesHomeAdapter;

        spinnerArrayAdapter = SpinnerArrayAdapter(this, categoriesListSpinner);
        spinner.adapter = spinnerArrayAdapter;
        spinner.gravity = Gravity.RIGHT;

//        fullLayout?.closeDrawer(Gravity.LEFT)

        iv_hamburger.setOnClickListener {
            fullLayout?.openDrawer(Gravity.LEFT);
        }

        iv_cart.setOnClickListener {
            startActivity(Intent(this@HomeActivity, CartActivity::class.java));
        }

        Handler().postDelayed(Runnable {
            et_search.clearFocus();
        }, 0);

    }

    override fun onResume() {
        super.onResume()
        imgMyOrder.colorFilter = null
        imgHome.setColorFilter(
            ContextCompat.getColor(
                this@HomeActivity,
                R.color.pink
            ), PorterDuff.Mode.SRC_IN
        )
        imgProfile.setColorFilter(null)
        imgNearStore.setColorFilter(null)
        imgAlert.setColorFilter(null)

//        layoutProfile!!.setBackgroundColor(Color.parseColor("#ffffff"))
//        layoutMyOrder!!.setBackgroundColor(Color.parseColor("#ffffff"))
//        layoutPrescription!!.setBackgroundColor(Color.parseColor("#ffffff"))
//        layoutHome!!.setBackgroundColor(Color.parseColor("#ED8701"))
//        layoutAlert!!.setBackgroundColor(Color.parseColor("#ffffff"))

        tvProfile!!.setTextColor(Color.parseColor("#000000"))
        tvMyOrder!!.setTextColor(Color.parseColor("#000000"))
        tvNearStore!!.setTextColor(Color.parseColor("#000000"))
        tvHome!!.setTextColor(Color.parseColor("#EC99BA"))
        tvAlert!!.setTextColor(Color.parseColor("#000000"))



    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                requestcode
            )
            false
        } else {
            true
        }
    }

}