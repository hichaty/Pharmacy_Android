package com.bestoffers.enjoeepharmacy

import android.app.Activity
import android.app.Dialog
import android.graphics.Point
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.webkit.MimeTypeMap
import com.bestoffers.enjoeepharmacy.Utils.SharedPreferenceUtility
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class APIClient(val context: Activity) {
    lateinit var request: Request
    var progressDialog: Dialog
    var JSON: MediaType? = "application/json; charset=utf-8".toMediaType()
    lateinit var call: Call
    var cacheSize = 10 * 1024 * 1024 // 10 MB
    var cache = Cache(context.cacheDir, cacheSize.toLong())


    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(25, TimeUnit.MINUTES)
        .writeTimeout(25, TimeUnit.MINUTES)
//        .addInterceptor(logging)
//        .addNetworkInterceptor(CacheInterceptor())
//        .addInterceptor(ForceCacheInterceptor())
//        .cache(cache)
        .readTimeout(25, TimeUnit.MINUTES)
        .retryOnConnectionFailure(true)
        .build()

    init {
        progressDialog = loadDialog()
    }

    fun loadDialog(): Dialog {
        val display = (context as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val deviceWidth = size.x

        var dialogLoading = Dialog(context)
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLoading.setContentView(R.layout.dialog_layout)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogLoading.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialogLoading.window!!.attributes = lp
        dialogLoading.setCancelable(false)

        return dialogLoading
    }

    fun get(url: String, apiRequestListener: APIRequestListener, showProgress: Boolean = false) {
//        Log.e(
//            "authorization",SharedPreferenceUtility.getInstance().get(AppConstants.PREF_ACCESS_TOKEN))
//        Log.e(
//            "authorization",SharedPreferenceUtility.getInstance().get(AppConstants.PREF_TOKEN_TYPE))

        val auth = SharedPreferenceUtility.getInstance()
            .get(AppConstants.PREF_TOKEN_TYPE, "") + " " + SharedPreferenceUtility.getInstance()
            .get(AppConstants.PREF_ACCESS_TOKEN, "");
        Log.e("auth", auth);

        val handler = Handler(Looper.getMainLooper())
        if (showProgress)
            progressDialog.show()
        Log.e("GetURL", url)

        request = Request.Builder().url(url)
            .addHeader(
                "Authorization", auth
            )
            .build()

        call = okHttpClient.newCall(request)

        call.enqueue(object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val result = response.body!!.string()
                if (response.cacheResponse == null)
                    Log.e("Cache null", "null")
                if (response.networkResponse == null)
                    Log.e("network null", "null")
                else if (response.networkResponse != null && response.cacheResponse != null)
                    Log.e("botn not null", "not null")

                handler.post {
                    if (progressDialog.isShowing)
                        progressDialog.dismiss()
                    apiRequestListener.onResponse(result)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("ERROR", e.toString())
                handler.post {
                    if (progressDialog.isShowing)
                        progressDialog.dismiss()
                }
            }
        })
    }


    fun post(
        url: String,
        body: Any,
        apiRequestListener: APIRequestListener,
        showProgress: Boolean = false
    ) {
        if (showProgress)
            progressDialog.show()

        Log.e("PostURL", url)
        val auth = SharedPreferenceUtility.getInstance()
            .get(AppConstants.PREF_TOKEN_TYPE, "") + " " + SharedPreferenceUtility.getInstance()
            .get(AppConstants.PREF_ACCESS_TOKEN, "");
        Log.e("auth", auth);

        if (body is String) {
            Log.e("PostJSON", body)
            val requestBody: RequestBody = RequestBody.create(JSON, body)
            request = Request.Builder().url(url)
                .addHeader(
                    "Authorization", auth
                )
                .post(requestBody).build()
        } else if (body is FormBody.Builder) {
            request = Request.Builder().url(url)
                .addHeader(
                    "Authorization", auth
                )
                .post(body.build())
                .build()
        } else if (body is MultipartBody.Builder) {
            request = Request.Builder().url(url)
                .addHeader(
                    "Authorization", auth
                )
                .post(body.build())
                .build()
        }
//        else if (body is HashMap<*, *>) {
//            request = Request.Builder().url(url)
//                .addHeader(
//                    "Authorization", auth
//                )
//                .post(body.build())
//                .build()
//        }

        call = okHttpClient.newCall(request)

        call.enqueue(object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val result = response.body!!.string()
                (context as Activity).runOnUiThread {
                    if (progressDialog.isShowing)
                        progressDialog.dismiss()
                    apiRequestListener.onResponse(result)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("ERROR", e.toString())
                (context as Activity).runOnUiThread {
                    if (progressDialog.isShowing)
                        progressDialog.dismiss()
                }
            }
        })
    }

    fun delete(
        url: String,
        body: Any,
        apiRequestListener: APIRequestListener,
        showProgress: Boolean = false
    ) {
        if (showProgress)
            progressDialog.show()


        Log.e("DeleteURL", url)

        if (body is String) {
            Log.e("DeleteJSON", body)
            val requestBody: RequestBody = RequestBody.create(JSON, body)
            request = Request.Builder().url(url).delete(requestBody).build()
        } else if (body is FormBody.Builder) {
            request = Request.Builder().url(url)
                .delete(body.build())
                .build()
        } else if (body is MultipartBody.Builder) {
            request = Request.Builder().url(url)
                .delete(body.build())
                .build()
        }

        call = okHttpClient.newCall(request)

        call.enqueue(object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val result = response.body!!.string()
                (context as Activity).runOnUiThread {
                    if (progressDialog.isShowing)
                        progressDialog.dismiss()
                    apiRequestListener.onResponse(result)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("ERROR", e.toString())
                (context as Activity).runOnUiThread {
                    if (progressDialog.isShowing)
                        progressDialog.dismiss()
                }
            }
        })
    }


    companion object {
        fun createBuilder(paramsName: Array<String>, paramsValue: Array<String>): FormBody.Builder {
            val builder = FormBody.Builder()
            for (i in paramsName.indices) {
                Log.e("CHECK", paramsName[i] + "___" + paramsValue[i])
                builder.add(paramsName[i], paramsValue[i])
            }
            return builder
        }

        fun createHashMapBuilder(hashMap: HashMap<String,String>): FormBody.Builder {
            val builder = FormBody.Builder()
            hashMap.forEach { (k, v) ->
                Log.e("CHECK", k + "___" +  v)
                builder.add(k, v)
            }

            return builder
        }

        fun createMultiPartBuilder(
            paramsName: Array<String>,
            paramsValue: Array<String>
        ): MultipartBody.Builder {

            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

            for (i in paramsName.indices) {

                Log.e(paramsName[i], paramsValue[i])

                if (!TextUtils.isEmpty(paramsValue[i])) {
                    if (paramsValue[i].contains("/storage") || paramsValue[i].contains("data/")) {
                        val fileData = getFileNameAndType(paramsValue[i])
                        Log.e("type", "" + fileData[1])
                        var mimeType = fileData[1]
                        builder.addFormDataPart(
                            paramsName[i], fileData[0] + "",
                            RequestBody.create(mimeType.toMediaType(), File(paramsValue[i]))
                        )
                    } else {
                        builder.addFormDataPart(paramsName[i], paramsValue[i])
                    }
                }
            }
            return builder
        }

        fun getFileNameAndType(path: String): Array<String> {
            var type: String? = ""
            val filename = path.substring(path.lastIndexOf("/") + 1)
            Log.e("FILENAME", filename)
            val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(path)).toString())

            if (extension != null)
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            else {
                val fileType = filename.substring(filename.lastIndexOf(".") + 1)
                type = "file/$fileType"
            }
            return arrayOf<String>(filename, type ?: "")
        }

    }

}