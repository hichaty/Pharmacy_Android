package com.bestoffers.enjoeepharmacy.Activity

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestoffers.enjoeepharmacy.Adapters.PrescriptionAdapter
import com.bestoffers.enjoeepharmacy.Models.PrescriptionModel
import com.bestoffers.enjoeepharmacy.R
import kotlinx.android.synthetic.main.activity_prescription.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class PrescriptionActivity : AppCompatActivity() {
    var prescriptionModelList = ArrayList<PrescriptionModel>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescription)

        initView()

    }

    private fun initView() {
        iv_hamburger.visibility= View.GONE;
        iv_cart.visibility= View.GONE;
        iv_back.visibility= View.VISIBLE;

        iv_back.setOnClickListener {
            onBackPressed()
        }

        toolbarTitle.setText("Prescription")

        prescriptionModelList.add(PrescriptionModel(true))
        prescriptionModelList.add(PrescriptionModel(true))
        prescriptionModelList.add(PrescriptionModel(false))
        prescriptionModelList.add(PrescriptionModel(true))
        var homeAdapter = PrescriptionAdapter(prescriptionModelList, this);
        rv_prescription?.layoutManager =LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rv_prescription?.adapter = homeAdapter;
    }


}