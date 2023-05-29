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
import com.bestoffers.enjoeepharmacy.Activity.VendorProductActivity
import com.bestoffers.enjoeepharmacy.Models.NearestVendorItemData
import com.bestoffers.enjoeepharmacy.R
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class NearStoreAdapter(
    val nearStoreModelList: ArrayList<NearestVendorItemData>,
    val context: Context
) : RecyclerView.Adapter<NearStoreAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.stores_card_view_item, parent, false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var nearStoreModelListt = nearStoreModelList.get(position)

        Picasso.with(context).load(nearStoreModelListt.storeImage)
            .placeholder(R.drawable.ic_baseline_camera_alt_24)
            .error(R.drawable.ic_baseline_camera_alt_24)
            .into(holder.iv_product);

        holder.tvStoreName.setText(nearStoreModelListt.storeName)
        holder.tv_store_place.setText(nearStoreModelListt.storeLocation+" , "+nearStoreModelListt.location)

        val df = DecimalFormat("#.##")
        val roundoff = df.format(nearStoreModelListt.distance)
        holder.tv_distance.setText(roundoff.toString())

        holder.cardView.setOnClickListener {
            val intent = Intent(context, VendorProductActivity::class.java)
            intent.putExtra("vendor_id",nearStoreModelListt.vendorClickId.toString())
            intent.putExtra("store_name",nearStoreModelListt.storeName)
            context.startActivity(intent);
        }

    }

    override fun getItemCount(): Int {
        return nearStoreModelList.size;
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_distance = itemView.findViewById<TextView>(R.id.tv_distance);
        var iv_product = itemView.findViewById<ImageView>(R.id.iv_product);
        var tvStoreName = itemView.findViewById<TextView>(R.id.tvStoreName);
        var tv_store_place = itemView.findViewById<TextView>(R.id.tv_store_place);
        var cardView = itemView.findViewById<CardView>(R.id.cardView);
    }

}