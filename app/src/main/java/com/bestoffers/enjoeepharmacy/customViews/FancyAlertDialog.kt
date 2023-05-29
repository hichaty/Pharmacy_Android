package com.bestoffers.enjoeepharmacy.customViews

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bestoffers.enjoeepharmacy.R

class FancyAlertDialog private constructor(builder: Builder) {
    private val title: String?
    private val message: String?
    private val positiveBtnText: String?
    private val negativeBtnText: String?
    private val hideNegativeBtn = false
    private val activity: Activity
    private val icon: Int
    private val visibility: Icon?
    private val animation: Animation?
    private val pListener: FancyAlertDialogListener?
    private val nListener: FancyAlertDialogListener?
    private val pBtnColor: Int
    private val nBtnColor: Int
    private val bgColor: Int
    private val cancel: Boolean

    class Builder(val activity: Activity) {
        var title: String? = null
        var message: String? = null
        var positiveBtnText: String? = null
        var negativeBtnText: String? = null
        var hideNegativeBtn = false
        var icon = 0
        val visibility: Icon? = null
        var animation: Animation? = null
        var pListener: FancyAlertDialogListener? = null
        var nListener: FancyAlertDialogListener? = null
        var pBtnColor = 0
        var nBtnColor = 0
        var bgColor = 0
        var cancel = false
        fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        fun setBackgroundColor(bgColor: Int): Builder {
            this.bgColor = bgColor
            return this
        }

        fun setMessage(message: String?): Builder {
            this.message = message
            return this
        }

        fun setPositiveBtnText(positiveBtnText: String?): Builder {
            this.positiveBtnText = positiveBtnText
            return this
        }

        fun setPositiveBtnBackground(pBtnColor: Int): Builder {
            this.pBtnColor = pBtnColor
            return this
        }

        fun setNegativeBtnText(negativeBtnText: String?): Builder {
            this.negativeBtnText = negativeBtnText
            return this
        }

        fun setNegativeBtnBackground(nBtnColor: Int): Builder {
            this.nBtnColor = nBtnColor
            return this
        }

        fun setHideNegativeBtn(hide: Boolean): Builder {
            hideNegativeBtn = hide
            return this
        }

        //setIcon
        fun setIcon(icon: Int): Builder {
            this.icon = icon
            return this
        }

        fun setAnimation(animation: Animation?): Builder {
            this.animation = animation
            return this
        }

        //set Positive listener
        fun OnPositiveClicked(pListener: FancyAlertDialogListener?): Builder {
            this.pListener = pListener
            return this
        }

        //set Negative listener
        fun OnNegativeClicked(nListener: FancyAlertDialogListener?): Builder {
            this.nListener = nListener
            return this
        }

        fun isCancellable(cancel: Boolean): Builder {
            this.cancel = cancel
            return this
        }

        fun build(): FancyAlertDialog {
            val message1: TextView
            val title1: TextView
            val iconImg: ImageView
            val nBtn: Button
            val pBtn: Button
            val view: View
            val dialog: Dialog
            if (animation == Animation.POP) dialog = Dialog(
                activity, R.style.PopTheme
            ) else dialog = Dialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(cancel)
            dialog.setContentView(R.layout.fancyalertdialog)

            //getting resources
            view = dialog.findViewById(R.id.background) as View
            title1 = dialog.findViewById<View>(R.id.title) as TextView
            message1 = dialog.findViewById<View>(R.id.message) as TextView
            iconImg = dialog.findViewById<View>(R.id.icon) as ImageView
            nBtn = dialog.findViewById<View>(R.id.negativeBtn) as Button
            pBtn = dialog.findViewById<View>(R.id.positiveBtn) as Button
            title1.text = title
            message1.text = message
            if (positiveBtnText != null) pBtn.text = positiveBtnText
            if (hideNegativeBtn) nBtn.visibility = View.GONE else nBtn.visibility = View.VISIBLE
            if (pBtnColor != 0) {
                val bgShape = pBtn.background as GradientDrawable
                bgShape.mutate()
                bgShape.setColor(activity.resources.getColor(pBtnColor))
            }
            if (nBtnColor != 0) {
                val bgShape = nBtn.background as GradientDrawable
                bgShape.mutate()
                //              bgShape.setColor(nBtnColor);
                bgShape.setStroke(3, activity.resources.getColor(nBtnColor))
            }
            if (negativeBtnText != null) nBtn.text = negativeBtnText
            iconImg.setImageResource(icon)
            /*
            if(visibility==Icon.Visible)
            iconImg.setVisibility(View.VISIBLE);
            else
            iconImg.setVisibility(View.GONE);
*/if (bgColor != 0) view.setBackgroundColor(bgColor)
            if (pListener != null) {
                pBtn.setOnClickListener(View.OnClickListener {
                    pListener!!.OnClick()
                    dialog.dismiss()
                })
            } else {
                pBtn.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View) {
                        dialog.dismiss()
                    }
                })
            }
            if (nListener != null) {
                nBtn.visibility = View.VISIBLE
                nBtn.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View) {
                        nListener!!.OnClick()
                        dialog.dismiss()
                    }
                })
            }
            dialog.show()
            return FancyAlertDialog(this)
        }
    }

    enum class Animation {
        POP, SIDE, SLIDE
    }

    enum class Icon {
        Visible, Gone
    }

    interface FancyAlertDialogListener {
        fun OnClick()
    }

    init {
        title = builder.title
        message = builder.message
        activity = builder.activity
        icon = builder.icon
        animation = builder.animation
        visibility = builder.visibility
        pListener = builder.pListener
        nListener = builder.nListener
        positiveBtnText = builder.positiveBtnText
        negativeBtnText = builder.negativeBtnText
        pBtnColor = builder.pBtnColor
        nBtnColor = builder.nBtnColor
        bgColor = builder.bgColor
        cancel = builder.cancel
    }
}