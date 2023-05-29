package com.bestoffers.enjoeepharmacy.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.*
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bestoffers.enjoeepharmacy.APIClient
import com.bestoffers.enjoeepharmacy.APIRequestListener
import com.bestoffers.enjoeepharmacy.Adapters.NearStoreAdapter
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.Models.NearestVendorItemData
import com.bestoffers.enjoeepharmacy.R
import com.bestoffers.enjoeepharmacy.Utils.FieldSelector
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_near_store.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class NearStoreActivity : BottomBaseActivity() {
    var requestcode=101
    var nearStoreModelList = ArrayList<NearestVendorItemData>();
    var nearStoreAdapter : NearStoreAdapter? = null
    var pageNo = 1
    var isLastPage = false
    var latitude=0.00
    var longitude=0.00
    private var currentLocation: Location? = null
    private var locationByGps: Location? = null
    private var locationByNetwork: Location? = null
    lateinit var locationManager: LocationManager
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var fieldSelector: FieldSelector? = null
    val PERMISSION_ID = 42

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_near_store)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fieldSelector = FieldSelector()
        initView()
        getLastLocation()


    }

//    private fun findLocation() {
//
//        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//
//
//        val gpsLocationListener: LocationListener = object : LocationListener {
//            override fun onLocationChanged(location: Location) {
//                locationByGps= location
//            }
//
//            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
//            override fun onProviderEnabled(provider: String) {}
//            override fun onProviderDisabled(provider: String) {}
//        }
//
//        val networkLocationListener: LocationListener = object : LocationListener {
//            override fun onLocationChanged(location: Location) {
//                locationByNetwork= location
//            }
//
//            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
//            override fun onProviderEnabled(provider: String) {}
//            override fun onProviderDisabled(provider: String) {}
//        }
//
//        if (hasGps) {
//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//
//                locationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER,
//                    5000,
//                    0F,
//                    gpsLocationListener
//                )
//                return
//            }
//
//        }
//
//        if (hasNetwork) {
//            locationManager.requestLocationUpdates(
//                LocationManager.NETWORK_PROVIDER,
//                5000,
//                0F,
//                networkLocationListener
//            )
//        }
//
//    }

    private fun nearestVendor_API(latitude: Double, longitude: Double) {
        val body = APIClient.createBuilder(
            arrayOf("page","store_latitude","store_longitude"),
            arrayOf(pageNo.toString(), latitude.toString(), longitude.toString())
        )
        Log.e("nearestVendor_API", pageNo.toString()+" "+latitude.toString()+" "+longitude.toString())
        val apiRequestListener = object : APIRequestListener {
            override fun onResponse(response: String) {
                Log.e("Related IdProduct", response)
                Log.e("nearestVendor_API", response)
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("status")) {
//                        Toast.makeText(
//                            this@NearStoreActivity,
//                            jsonObject.getString("message"),
//                            Toast.LENGTH_LONG
//                        ).show()

                        val data2=jsonObject.getJSONObject("data")
                        val gson = Gson()
                        nearStoreAdapter?.notifyDataSetChanged()
                        val listType: Type =
                            object : TypeToken<List<NearestVendorItemData?>?>() {}.getType()
                        var data = data2.getJSONArray("data").toString()
                        var list: ArrayList<NearestVendorItemData> = gson.fromJson(data, listType)
                        nearStoreModelList.addAll(list)
                        nearStoreAdapter?.notifyDataSetChanged()


                        if (list.size < 1) {
                            ll_empty.visibility = View.VISIBLE
                            rv_near_store.visibility = View.GONE

                        } else {
                            ll_empty.visibility = View.GONE
                            rv_near_store.visibility = View.VISIBLE
                        }

                        if (jsonObject.getJSONObject("data").getInt("last_page") == pageNo) {
                            isLastPage = true
                        }

                    } else {
                        ll_empty.visibility = View.VISIBLE
                        rv_near_store.visibility = View.GONE
//                        MyApplication.errorAlert(
//                            this@NearStoreActivity,
//                            jsonObject.optString("message", "Please try again later."),
//                            "Error"
//                        )
                    }
                } catch (e: JSONException) {
                    Log.e("NearestVendor_error", e.message.toString())
                }
            }
        }
        if (!isLastPage) {
            APIClient(this).post(
                AppConstants.URL_NEAREST_VENDOR,
                body,
                apiRequestListener,
                true
            )
        }
    }

    private fun initView() {
        iv_cart.setOnClickListener {
            startActivity(Intent(this@NearStoreActivity,CartActivity::class.java));
        }

        iv_hamburger.setOnClickListener {
            fullLayout?.openDrawer(Gravity.LEFT);
        }
        toolbarTitle.setText("Store")

         nearStoreAdapter = NearStoreAdapter(nearStoreModelList,this);
        rv_near_store?.layoutManager = GridLayoutManager(this@NearStoreActivity, 2)
        rv_near_store?.adapter = nearStoreAdapter;

        rv_near_store.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        (rv_near_store.adapter as NearStoreAdapter).itemCount.toString()
                    )
                    Log.e("lastVisiblePosition", lastVisiblePosition.toString())

                    if (lastVisiblePosition == (rv_near_store.adapter as NearStoreAdapter).itemCount - 1) {
                        pageNo++
                        nearestVendor_API(latitude, longitude)
                    }
                }
            }
        })

    }

//    private fun isLocationPermissionGranted(): Boolean {
//        return if (ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    android.Manifest.permission.ACCESS_FINE_LOCATION,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION
//                ),
//                requestcode
//            )
//            false
//        } else {
//            true
//        }
//    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            Log.e("yyuy", "ytyt");
            if (isLocationEnabled()) {
                Log.e("vccvb", "dfhdhdh");
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    Log.e("Ffg", "vbvv");
                    if (location == null) {
                        Log.e("vbv", "bff");
                        requestNewLocationData()
                    } else {
                        latitude = location.latitude
                        longitude = location.longitude
                        nearestVendor_API(latitude,longitude)
                        Log.d(
                            "TAG",
                            location.latitude.toString() + "   " + location.longitude.toString()
                        )
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        Log.e("tty", "dee");
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Looper.myLooper()?.let {
            mFusedLocationClient!!.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                it
            )
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            Log.e("lat", mLastLocation.latitude.toString())
            Log.e("long", mLastLocation.longitude.toString())
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude

        }
    }


    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    private fun getAddressLatLong(latitude: Double, longitude: Double): String {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        )
        if (addresses.size > 0) {
            val address: String = addresses[0].getAddressLine(0)

            val city: String = addresses[0].getLocality()
            val state: String = addresses[0].getAdminArea()
            val country: String = addresses[0].getCountryName()
            val postalCode: String = addresses[0].getPostalCode()
            val knownName: String = addresses[0].getFeatureName()
            Log.e("address", address.toString())
            Log.e("city", city.toString())
            Log.e("knownName", knownName.toString())
            return address.toString()
        }else{
            return ""
        }
    }


    override fun onResume() {
        super.onResume()
        imgMyOrder.colorFilter = null
        imgNearStore.setColorFilter(
            ContextCompat.getColor(
                this@NearStoreActivity,
                R.color.pink
            ), PorterDuff.Mode.SRC_IN
        )
        imgHome.setColorFilter(
            ContextCompat.getColor(
            this@NearStoreActivity,
            R.color.black
        ), PorterDuff.Mode.SRC_IN)
        imgProfile.setColorFilter(null)
        imgAlert.setColorFilter(null)

//        layoutHome!!.setBackgroundColor(Color.parseColor("#ffffff"))
//        layoutMyOrder!!.setBackgroundColor(Color.parseColor("#ffffff"))
//        layoutProfile!!.setBackgroundColor(Color.parseColor("#ffffff"))
//        layoutPrescription!!.setBackgroundColor(Color.parseColor("#ED8701"))
//        layoutAlert!!.setBackgroundColor(Color.parseColor("#ffffff"))

        tvHome!!.setTextColor(Color.parseColor("#000000"))
        tvMyOrder!!.setTextColor(Color.parseColor("#000000"))
        tvProfile!!.setTextColor(Color.parseColor("#000000"))
        tvNearStore!!.setTextColor(Color.parseColor("#EC99BA"))
        tvAlert!!.setTextColor(Color.parseColor("#000000"))
    }

}