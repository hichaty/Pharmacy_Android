package com.bestoffers.enjoeepharmacy.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bestoffers.enjoeepharmacy.Models.HomeModel
import com.bestoffers.enjoeepharmacy.Models.PrescriptionModel
import com.bestoffers.enjoeepharmacy.R

class PrescriptionAdapter(
    val prescriptionModelList: ArrayList<PrescriptionModel>,
    val context: Context
) : RecyclerView.Adapter<PrescriptionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.prescription_item_layout, parent, false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_medicineName.text="Glucose"
    }

    override fun getItemCount(): Int {
//        return prescriptionModelList.size;
        return 10;
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_medicineName=itemView.findViewById<TextView>(R.id.tv_medicineName);
        var tv_date=itemView.findViewById<TextView>(R.id.tv_date);
        var tv_time=itemView.findViewById<TextView>(R.id.tv_time);
        var btn_view_prescription=itemView.findViewById<Button>(R.id.btn_view_prescription);
    }

}