package com.bestoffers.enjoeepharmacy.customViews

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bestoffers.enjoeepharmacy.AppEnvironment
import com.bestoffers.enjoeepharmacy.R
import com.google.firebase.FirebaseApp
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class MyApplication : Application() {

    var progressDialog: Dialog? = null
    fun hideProgress() = progressDialog?.dismiss()


    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        instance = this
        appContext = this
        statusMap.put("0", "Pending")
        statusMap.put("1", "Approved")
        statusMap.put("2", "Rejected")

        setupActivityListener()
        setAppEnvironment(AppEnvironment.PRODUCTION);
    }




    private fun setupActivityListener() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
//                activity.window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
            }

            override fun onActivityResumed(activity: Activity) {
            }

        })
    }

    companion object {

        var statusMap = HashMap<String, String>()
        var appEnvironment: AppEnvironment? = null

        @get:Synchronized
        lateinit var instance: MyApplication
        var appContext: Context? = null
        private val maxHeight = 1280.0f
        private val maxWidth = 1280.0f
        private var plainString: String = ""
        private lateinit var newSpannable: SpannableStringBuilder

        fun getDeviceWidthHeight(context: Context): IntArray {
            val display = (context as Activity).windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            return intArrayOf(size.x, size.y)
        }

        val isConnectingToInternet: Boolean
            @SuppressLint("MissingPermission")
            get() {
                val connectivity =
                    instance!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (connectivity != null) {
                    val info = connectivity.allNetworkInfo
                    if (info != null)
                        for (i in info.indices)
                            if (info[i].state == NetworkInfo.State.CONNECTED) {
                                return true
                            }

                }
                return false
            }


        fun toggleSoftKeyBoard(activity: Activity, hide: Boolean) {
            val inputManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = activity.currentFocus ?: return
            if (hide) {
                inputManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            } else {
                inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }


        var EMOJI_FILTER: InputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            for (index in start until end) {

                val type = Character.getType(source[index])

                if (type == Character.SURROGATE.toInt()) {
                    return@InputFilter ""
                }
            }
            null
        }


        fun formatDate(string: String): String {
            var formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var date: Date? = null
            try {
                date = formatter.parse(string)
            } catch (e: ParseException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            formatter = SimpleDateFormat("MM/dd/yy")

            return formatter.format(date!!)
        }

        fun formatDateShortMonth(string: String): String {
            var formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var date: Date? = null
            try {
                date = formatter.parse(string)
            } catch (e: ParseException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            formatter = SimpleDateFormat("dd MMM yyyy")

            return formatter.format(date!!)
        }

        fun getCurrentFormattedDate(): String {
            val c = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            return formatter.format(c)
        }


        fun toggleSoftKeyBoard(activity: Activity, hide: Boolean, v: View?) {
            val inputManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = v ?: activity.currentFocus ?: return
            if (hide) {
                inputManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            } else {
                inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        fun decodeFile(f: File, bitmap: Bitmap?): Bitmap? {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            var scaledBitmap: Bitmap? = null
            var bmp: Bitmap? = null

            if (bitmap == null)
                bmp = BitmapFactory.decodeFile(f.path, options)

            var actualHeight = options.outHeight
            var actualWidth = options.outWidth

            var imgRatio = actualWidth.toFloat() / actualHeight.toFloat()
            val maxRatio = maxWidth / maxHeight

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight
                    actualWidth = (imgRatio * actualWidth).toInt()
                    actualHeight = maxHeight.toInt()
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth
                    actualHeight = (imgRatio * actualHeight).toInt()
                    actualWidth = maxWidth.toInt()
                } else {
                    actualHeight = maxHeight.toInt()
                    actualWidth = maxWidth.toInt()

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
            options.inJustDecodeBounds = false
            options.inDither = false
            options.inPurgeable = true
            options.inInputShareable = true
            options.inTempStorage = ByteArray(16 * 1024)

            try {
                bmp = BitmapFactory.decodeFile(f.path, options)
                return bmp
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()

            }

            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }

            return scaledBitmap
        }

        fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int
        ): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }
            val totalPixels = (width * height).toFloat()
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }

            return inSampleSize
        }


        fun alertDialog(activity: Activity, msg: String, titile: String) {
            val adb = AlertDialog.Builder(activity)
            adb.setMessage(msg)
            adb.setTitle(titile)
            adb.setPositiveButton("Ok") { dialog, which -> dialog.dismiss() }
            adb.show()
        }


        fun selectDate(editText: EditText, activity: Activity) {
            // Get Current Date
            //yyyy-mm-dd
            val c = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog(
                activity,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    editText.setText(
                        String.format("%02d", dayOfMonth) + "-" + String.format(
                            "%02d",
                            monthOfYear + 1
                        ) + "-" + year
                    )
                }, mYear, mMonth, mDay
            )

            datePickerDialog.show()
        }


        fun selectDOB(editText: EditText, activity: Activity) {
            // Get Current Date
            val c = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog(
                activity,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val userAge = GregorianCalendar(year, monthOfYear, dayOfMonth)
                    val minAdultAge = GregorianCalendar()
                    minAdultAge.add(Calendar.YEAR, -18)
                    if (minAdultAge.before(userAge)) {
                        editText.setText("")
                        Toast.makeText(activity, "You should be 18 Year Old", Toast.LENGTH_SHORT)
                            .show()
                    } else
                        editText.setText(
                            String.format("%02d", dayOfMonth)
                                    + "-" + String.format("%02d", monthOfYear + 1) + "-" + year
                        )
                }, mYear, mMonth, mDay
            )

            val now = System.currentTimeMillis() - 1000
            datePickerDialog.datePicker.maxDate = now
            datePickerDialog.show()
        }


        fun openDialerActivity(activity: Activity, telephone: String) {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:$telephone")
            try {
                activity.startActivity(callIntent)
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(activity, "Something is not Right", Toast.LENGTH_SHORT).show()
            }

        }


        fun doubleActionWarningAlert(
            activity: Activity, msg: String, title: String, leftBtnText: String,
            rightBtnText: String, leftBtnListener: FancyAlertDialog.FancyAlertDialogListener,
            rightBtnListener: FancyAlertDialog.FancyAlertDialogListener
        ) {
            FancyAlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setHideNegativeBtn(false)
                .setNegativeBtnText(if (TextUtils.isEmpty(leftBtnText)) "Yes" else leftBtnText)
                .setPositiveBtnText(if (TextUtils.isEmpty(rightBtnText)) "No" else rightBtnText)
                .setAnimation(FancyAlertDialog.Animation.POP)
                .isCancellable(false)
                .setIcon(R.drawable.ic_baseline_error_outline_24)
                .OnPositiveClicked(rightBtnListener)
                .OnNegativeClicked(leftBtnListener)
                .build()
        }

        fun singleActionWarningAlert(
            activity: Activity, msg: String, title: String,
            btnText: String, icon: Int, btnColor: Int,
            rightBtnListener: FancyAlertDialog.FancyAlertDialogListener
        ) {
            FancyAlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setHideNegativeBtn(true)
                .setPositiveBtnText(if (TextUtils.isEmpty(btnText)) "Ok" else btnText)
                .setAnimation(FancyAlertDialog.Animation.POP)
                .isCancellable(false)
                .setIcon(icon)
                .setPositiveBtnBackground(btnColor)
                .OnPositiveClicked(rightBtnListener)
                .build()
        }

        fun warningAlert(activity: Activity, msg: String, title: String) {
            FancyAlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setHideNegativeBtn(true)
                .setPositiveBtnText("Ok")
                .setAnimation(FancyAlertDialog.Animation.POP)
                .isCancellable(false)
                .setIcon(R.drawable.ic_baseline_error_outline_24)
                .build()
        }

        fun errorAlert(activity: Activity, msg: String, title: String) {
            FancyAlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setHideNegativeBtn(true)
                .setPositiveBtnText("Ok")
                .setAnimation(FancyAlertDialog.Animation.POP)
                .isCancellable(false)
                .setIcon(R.drawable.ic_baseline_error_outline_24)
                .setPositiveBtnBackground(R.color.pink)
                .build()
        }

        fun successAlert(
            activity: Activity,
            msg: String,
            title: String,
            okbtn: FancyAlertDialog.FancyAlertDialogListener
        ) {
            FancyAlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setHideNegativeBtn(true)
                .setPositiveBtnText("Ok")
                .setAnimation(FancyAlertDialog.Animation.POP)
                .isCancellable(false)
                .OnPositiveClicked(okbtn)
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveBtnBackground(R.color.blue)
                .build()
        }


        fun correctDecimal(price: Double): Double {
            val s = String.format("%.2f", price)
            return s.toDouble()
        }

        fun correctDecimal(price: String): String? {
            val d = price.toDouble()
            return String.format("%.2f", d)
        }

        fun correctDecimalString(price: Double): String {
            return String.format("%.2f", price)
        }

        fun dpToPx(c: Context, dipValue: Float): Int {
            val metrics = c.resources.displayMetrics
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics).toInt()
        }


        fun isInHindi(value: String): Boolean {
            return value != "Not"
        }


        fun removeStar() {
            var oldSpannable = newSpannable
            val index = plainString.indexOf("*")
            plainString = plainString.replaceFirst("*", "")
            oldSpannable.delete(index, index + 1)
            newSpannable = oldSpannable
            if (plainString.contains("*"))
                removeStar()
        }

        fun countChar(str: String, c: Char): Int {
            var count = 0
            for (i in 0 until str.length) {
                if (str[i] == c) count++
            }
            return count
        }

        fun setStatusBarLight(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val view: View = activity.window.getDecorView()
                var flags: Int = view.getSystemUiVisibility()
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                view.setSystemUiVisibility(flags)
                activity.getWindow().setStatusBarColor(Color.parseColor("#A3F6F6F6"))
            }

        }

        @JvmName("getAppEnvironment1")
        public fun getAppEnvironment(): AppEnvironment? {
            return appEnvironment
        }

        @JvmName("setAppEnvironment1")
        fun setAppEnvironment(appEnvironment: AppEnvironment) {
            this.appEnvironment = appEnvironment
        }


    }

    fun showProgress(activity: Activity) {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        var dialogLoading = Dialog(activity)
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLoading.setContentView(R.layout.dialog_layout)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogLoading.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialogLoading.window!!.attributes = lp
        dialogLoading.setCancelable(false)

        progressDialog = dialogLoading
        progressDialog?.show()
    }


}