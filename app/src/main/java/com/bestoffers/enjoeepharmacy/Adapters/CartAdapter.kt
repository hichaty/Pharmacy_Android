package com.bestoffers.enjoeepharmacy.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bestoffers.enjoeepharmacy.Activity.CartActivity
import com.bestoffers.enjoeepharmacy.Models.DataProductList
import com.bestoffers.enjoeepharmacy.R
import com.squareup.picasso.Picasso

class CartAdapter(
    val dataProductList: ArrayList<DataProductList>,
    val context: Context
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.cart_item_layout, parent, false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataProductListt = dataProductList.get(position);
        Picasso.with(context).load(dataProductListt.image).into(holder.iv_product_image);
        holder.tv_productName.setText(dataProductListt.productName);
        holder.tv_price.setText("Rs. " + dataProductListt.salePrice);
        holder.tv_quantity.setText(dataProductListt.quantityCart.toString());

        holder.iv_up.setOnClickListener {
            val cur_qty = holder.tv_quantity.text.toString().toInt()
            val qty = cur_qty + 1
//            holder.tv_quantity.text = qty.toString()
            (context as CartActivity).updateCart_API(
                dataProductListt.cartId.toString(),
                qty.toString(),
                ""
            )

        }

        holder.iv_down.setOnClickListener {
            val cur_qty = holder.tv_quantity.text.toString().toInt()
            if (cur_qty > 1) {
                val qty = cur_qty - 1
//                holder.tv_quantity.text = qty.toString()
                (context as CartActivity).updateCart_API(
                    dataProductListt.cartId.toString(),
                    qty.toString(),
                    ""
                )
            }
        }

        holder.iv_cross.setOnClickListener {
            (context as CartActivity).removeItemCart_API(dataProductListt.cartId.toString())
        }

    }

    override fun getItemCount(): Int {
        return dataProductList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView = itemView.findViewById<CardView>(R.id.cardView);
        var iv_product_image = itemView.findViewById<ImageView>(R.id.iv_product_image);
        var tv_productName = itemView.findViewById<TextView>(R.id.tv_productName);
        var iv_up = itemView.findViewById<ImageView>(R.id.iv_up);
        var tv_quantity = itemView.findViewById<TextView>(R.id.tv_quantity);
        var iv_down = itemView.findViewById<ImageView>(R.id.iv_down);
        var tv_price = itemView.findViewById<TextView>(R.id.tv_price);
        var tv_offer = itemView.findViewById<TextView>(R.id.tv_offer);
        var tv_view = itemView.findViewById<TextView>(R.id.tv_view);
        var tv_upload_prescription = itemView.findViewById<TextView>(R.id.tv_upload_prescription);
        var iv_cross = itemView.findViewById<ImageView>(R.id.iv_cross);
        var iv_prescription = itemView.findViewById<ImageView>(R.id.iv_prescription);
    }
}