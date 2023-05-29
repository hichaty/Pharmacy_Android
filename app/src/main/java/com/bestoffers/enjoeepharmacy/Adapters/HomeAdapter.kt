package com.bestoffers.enjoeepharmacy.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bestoffers.enjoeepharmacy.APIClient
import com.bestoffers.enjoeepharmacy.APIRequestListener
import com.bestoffers.enjoeepharmacy.Activity.HomeActivity
import com.bestoffers.enjoeepharmacy.Activity.RelatedIdProductActivity
import com.bestoffers.enjoeepharmacy.AppConstants
import com.bestoffers.enjoeepharmacy.Models.DataProductList
import com.bestoffers.enjoeepharmacy.R
import com.bestoffers.enjoeepharmacy.Utils.SharedPreferenceUtility
import com.bestoffers.enjoeepharmacy.customViews.MyApplication
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class HomeAdapter(
    val context: Context,
    val productList: ArrayList<DataProductList>,
    val activity: String

) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.medicine_card_view_item, parent, false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val productListt = productList.get(position);
        Picasso.with(context).load(productListt.image)
            .placeholder(R.drawable.ic_baseline_camera_alt_24)
            .error(R.drawable.ic_baseline_camera_alt_24)
            .into(holder.iv_product);
        holder.tvMedicineName.setText(productListt.productName);
        var offer = (productListt.discount)?.toDouble()
        holder.tv_offer.setText("" + offer + " %");
        holder.tv_price.setText("Rs. " + productListt.salePrice);
        holder.tv_price_mrp.setText("Mrp. Rs. ")
        holder.tv_price_mrp_strike.setPaintFlags(holder.tv_price_mrp_strike.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
        holder.tv_price_mrp_strike.setText(productListt.mrp)
        holder.cardView?.setOnClickListener() {
            val intent = Intent(context, RelatedIdProductActivity::class.java)
            intent.putExtra("prod_id", productListt.productClickId.toString())
            intent.putExtra("prod_name", productListt.productName);
            context.startActivity(intent);
        }

        holder.tvAddToCart.visibility=View.GONE

        holder.tvAddToCart.setOnClickListener {
            (context as HomeActivity).addToCart(
                productListt.vendorClickId.toString(),
                productListt.productClickId.toString(),
                productListt.vendorProductId.toString(),
                "1",
                ""
            )
        }

    }

    override fun getItemCount(): Int {
        return productList.size;
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_offer = itemView.findViewById<TextView>(R.id.tv_offer);
        var iv_product = itemView.findViewById<ImageView>(R.id.iv_product);
        var tvMedicineName = itemView.findViewById<TextView>(R.id.tvMedicineName);
        var tvAddToCart = itemView.findViewById<TextView>(R.id.tvAddToCart);
        var tv_price = itemView.findViewById<TextView>(R.id.tv_price);
        var tv_price_mrp = itemView.findViewById<TextView>(R.id.tv_price_mrp);
        var tv_price_mrp_strike = itemView.findViewById<TextView>(R.id.tv_price_mrp_strike);
        var cardView = itemView.findViewById<CardView>(R.id.cardView);
    }

}