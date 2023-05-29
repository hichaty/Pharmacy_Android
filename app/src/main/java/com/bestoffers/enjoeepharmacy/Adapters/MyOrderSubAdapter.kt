package com.bestoffers.enjoeepharmacy.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bestoffers.enjoeepharmacy.Models.GetOrderModelSubData
import com.bestoffers.enjoeepharmacy.Models.GetOrderProductDetail
import com.bestoffers.enjoeepharmacy.Models.MyOrderMainModel
import com.bestoffers.enjoeepharmacy.Models.MyOrderSubModel
import com.bestoffers.enjoeepharmacy.R
import com.squareup.picasso.Picasso

class MyOrderSubAdapter(
    val getOrderModelSubData: ArrayList<GetOrderProductDetail>,
    val context: Context
) : RecyclerView.Adapter<MyOrderSubAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.my_order_sub_item_layout, parent, false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: MyOrderSubAdapter.ViewHolder, position: Int) {
        val getOrderModelSubDataa = getOrderModelSubData.get(position);
        Picasso.with(context).load(getOrderModelSubDataa.productImage)
            .into(holder.iv_product_image);
        holder.tv_productName.setText(getOrderModelSubDataa.productName);
        holder.tv_price.setText("Rs. " + getOrderModelSubDataa.salePrice);
        holder.tv_quantity.setText("Quantity: " + getOrderModelSubDataa.quantity.toString());
        holder.tv_status.setText("Status: " + getOrderModelSubDataa.orderStatus.toString());

        if (getOrderModelSubDataa.orderStatus.toString()
                .equals("Pending") || getOrderModelSubDataa.orderStatus.toString()
                .equals("failed")
        ) {
            holder.iv_error.visibility = View.VISIBLE
            holder.tv_track.visibility=View.VISIBLE
        } else {
            holder.iv_error.visibility = View.GONE
            holder.tv_track.visibility=View.GONE
        }

    }

    override fun getItemCount(): Int {
        return getOrderModelSubData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_product_image = itemView.findViewById<ImageView>(R.id.iv_product_image);
        var iv_error = itemView.findViewById<ImageView>(R.id.iv_error);
        var tv_productName = itemView.findViewById<TextView>(R.id.tv_productName);
        var tv_quantity = itemView.findViewById<TextView>(R.id.tv_quantity);
        var tv_price = itemView.findViewById<TextView>(R.id.tv_price);
        var tv_status = itemView.findViewById<TextView>(R.id.tv_status);
        var tv_repeat = itemView.findViewById<TextView>(R.id.tv_repeat);
        var tv_track = itemView.findViewById<TextView>(R.id.tv_track);
    }

}