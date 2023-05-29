package com.bestoffers.enjoeepharmacy.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bestoffers.enjoeepharmacy.Models.DataCategories
import com.bestoffers.enjoeepharmacy.R
import com.squareup.picasso.Picasso

class CategoriesHomeAdapter (
    val context: Context,
    val categoryList: ArrayList<DataCategories>

) : RecyclerView.Adapter<CategoriesHomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.medicine_card_view_item, parent, false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val categoryList = categoryList.get(position);
        Picasso.with(context).load(categoryList.image)
            .into(holder.iv_product);
        holder.tvMedicineName.setText(categoryList.name);
        holder.tv_price.visibility=View.GONE
        holder.tvAddToCart.visibility=View.GONE
//        holder.tv_price.setText("Rs. " + categoryList.price);
        holder.cardView?.setOnClickListener() {
//            val intent = Intent(context, ProductsActivity::class.java)
//            intent.putExtra("cat_id",categoryList.id.toString());
//            intent.putExtra("cat_name",categoryList.name);
//            context.startActivity(intent);
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size;
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_offer = itemView.findViewById<TextView>(R.id.tv_offer);
        var iv_product = itemView.findViewById<ImageView>(R.id.iv_product);
        var tvMedicineName = itemView.findViewById<TextView>(R.id.tvMedicineName);
        var tvAddToCart = itemView.findViewById<TextView>(R.id.tvAddToCart);
        var tv_price = itemView.findViewById<TextView>(R.id.tv_price);
        var cardView = itemView.findViewById<CardView>(R.id.cardView);
    }

}