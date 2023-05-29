package com.bestoffers.enjoeepharmacy.Activity

import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

private const val DUMMY_VALUE = "dummy_value"

abstract class BaseActivity : AppCompatActivity() {

    private val TAG = BaseActivity::class.java.simpleName
    private var progressDialog: ProgressDialog? = null

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putInt(DUMMY_VALUE, 0)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onPause() {
        super.onPause()
        hideProgressDialog()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(
                    Intent(
                        this,
                        HomeActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun showErrorSnackbar(
        @StringRes resId: Int,
        e: Exception?,
        clickListener: View.OnClickListener?
    ) {
        Toast.makeText(this, resources.getString(resId), Toast.LENGTH_SHORT).show()
/*
        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        rootView?.let {
            Toast.makeText()
            showSnackbar(it, resId, e, R.string.dlg_retry, clickListener)
        }
*/
    }

    protected fun showProgressDialog(@StringRes messageId: Int) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
            progressDialog!!.isIndeterminate = true
            progressDialog!!.setCancelable(false)
            progressDialog!!.setCanceledOnTouchOutside(false)

            val keyListener = DialogInterface.OnKeyListener { dialog,
                                                              keyCode,
                                                              event ->
                keyCode == KeyEvent.KEYCODE_BACK
            }
            progressDialog!!.setOnKeyListener(keyListener)
        }
        //progressDialog!!.setMessage(getString(messageId))
        try {
            progressDialog!!.show()
        } catch (e: Exception) {
            e.message?.let { Log.d(TAG, it) }
        }
    }

    protected fun hideProgressDialog() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    protected fun isProgresDialogShowing(): Boolean {
        if (progressDialog != null && progressDialog?.isShowing != null) {
            return progressDialog!!.isShowing
        } else {
            return false
        }
    }

    override fun onResume() {
        super.onResume()
        hideNotifications()
        onResumeFinished()

    }

    private fun hideNotifications() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    open fun onResumeFinished() {
        // Need to Override onResumeFinished() method in nested classes if we need to handle returning from background in Activity
    }

    override fun onBackPressed() {
        finish()
    }

}