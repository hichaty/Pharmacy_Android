package com.bestoffers.enjoeepharmacy.Adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bestoffers.enjoeepharmacy.Models.*
import com.bestoffers.enjoeepharmacy.R
import kotlinx.android.synthetic.main.activity_prescription.*

class MyOrderMainAdapter(
    val getOrderModelDataList: ArrayList<GetOrderModelSubData>,
    val context: Context,
) : RecyclerView.Adapter<MyOrderMainAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.my_order_main_item_layout, parent, false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tv_order_date.text = getOrderModelDataList.get(position).orderDate;

        var getOrderModelSubData = ArrayList<GetOrderProductDetail>();
        getOrderModelSubData.addAll(getOrderModelDataList.get(position).productDetail)
        var myOrderSubAdapter = MyOrderSubAdapter(getOrderModelSubData, context);
        holder.rv_my_order_sub?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.rv_my_order_sub?.adapter = myOrderSubAdapter;

        }

        override fun getItemCount(): Int {
        return getOrderModelDataList.size;
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tv_order_date = itemView.findViewById<TextView>(R.id.tv_order_date);
            var rv_my_order_sub = itemView.findViewById<RecyclerView>(R.id.rv_my_order_sub);
        }
}