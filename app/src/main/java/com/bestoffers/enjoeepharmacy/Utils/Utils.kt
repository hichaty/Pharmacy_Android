package com.bestoffers.enjoeepharmacy.Utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.bestoffers.enjoeepharmacy.R

object Utils {
    fun setSpinnerCCAdapter(context: Context, spinner: Spinner?, list: ArrayList<String>) {
//        val typeface = ResourcesCompat.getFont(context, R.font.roboto_regular)
        val adapter = object : ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_item,
            list
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

                val view = super.getView(position, convertView, parent)
                if (position > 0) {
                    val id = list[position].split(" ")[0].trim()
                    val name = list[position].split(" ")[1].trim()
                    val textView = view.findViewById<TextView>(android.R.id.text1)
                    if (!id.equals("0")) {
//                        textView.typeface = typeface
                        textView.text = "+" + id.toString()
                        textView.tag = id
                    } else {
//                        textView.typeface = typeface
                        textView.text = name.toString()
                        textView.tag = id
                    }
                } else {
//                    val id = list[position].split(" ")[0].trim()
                    val name = list[position];
                    val textView = view.findViewById<TextView>(android.R.id.text1)
//                    textView.typeface = typeface
                    textView.text = name.toString().trim()
                    textView.tag = "0"

                }
                return view
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                if (position > 0) {
                    val id = list[position].split(" ")[0].trim()
                    val name = list[position].split(" ")[1].trim()
                    val textView = view.findViewById<TextView>(android.R.id.text1)
                    if (!id.equals("0")) {
//                        textView.typeface = typeface
                        textView.text = "+" + id + "   " + name
                        textView.tag = id
                    } else {
//                        textView.typeface = typeface
                        textView.text = name
                        textView.tag = id
                    }
                }
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = adapter
    }
}