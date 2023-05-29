package com.bestoffers.enjoeepharmacy.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bestoffers.enjoeepharmacy.Models.DataProductList
import com.bestoffers.enjoeepharmacy.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_bottom_base.*
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.custom_toolbar.*


class ProductDetailActivity : AppCompatActivity() {
    var code = "";
    var productList = DataProductList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val intentt = intent?.extras;
        if (intentt != null) {
            productList = (intent.getSerializableExtra("productList") as? DataProductList)!!
        }
        initView()

    }

    private fun initView() {
        iv_hamburger.visibility = View.GONE;
        iv_cart.visibility = View.VISIBLE;
        iv_back.visibility = View.VISIBLE;

        iv_back.setOnClickListener {
            onBackPressed()
        }

        iv_cart.setOnClickListener {
            startActivity(Intent(this@ProductDetailActivity, CartActivity::class.java));
        }

        toolbarTitle.setText(productList.productName)
        iv_hamburger.setOnClickListener {
            fullLayout?.openDrawer(Gravity.LEFT);
        }

        iv_add_product_details.setOnClickListener {
            tvExpandedListener(tv_exp_product_details, iv_add_product_details);
        }
        iv_add_uses.setOnClickListener {
            tvExpandedListener(tv_exp_uses, iv_add_uses);
        }
        iv_add_warning_precautions.setOnClickListener {
            tvExpandedListener(tv_exp_warning_precautions, iv_add_warning_precautions);
        }
        iv_add_interactions.setOnClickListener {
            tvExpandedListener(tv_exp_interactions, iv_add_interactions);
        }
        iv_add_direction_for_use.setOnClickListener {
            tvExpandedListener(tv_exp_directions_for_use, iv_add_direction_for_use);
        }
        iv_add_side_effects.setOnClickListener {
            tvExpandedListener(tv_exp_side_effects, iv_add_side_effects);
        }
        iv_add_more_info.setOnClickListener {
            tvExpandedListener(tv_exp_more_info, iv_add_more_info);
        }
        iv_call.setOnClickListener {
            Log.e("call_number", productList.mobile.toString())

            val number = "tel:" + productList.mobile.toString()
            val intent = Intent(Intent.ACTION_CALL)
            intent.setData(Uri.parse(number))

            if (ContextCompat.checkSelfPermission(
                    this@ProductDetailActivity,
                    android.Manifest.permission.CALL_PHONE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@ProductDetailActivity, arrayOf(android.Manifest.permission.CALL_PHONE),
                    101
                )

            } else {
                try {
                    startActivity(intent)
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }

        }

        Picasso.with(applicationContext)
            .load(productList.image)
            .placeholder(R.drawable.ic_baseline_camera_alt_24)
            .error(R.drawable.ic_baseline_camera_alt_24)
            .into(iv_product_image);

        tv_productName.setText(productList.productName);
        tv_sale_price.setText("Rs. " + productList.salePrice + " | Mrp Rs. ");
        tv_mrp_price.setPaintFlags(tv_mrp_price.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
        tv_mrp_price.setText(productList.mrp);
        tv_medical.setContent(productList.descriptions)
        tv_strength.setText(productList.strength);
        tv_exp_product_details.setText(productList.descriptions);
        tv_exp_uses.setText(productList.uses);
        tv_exp_warning_precautions.setText(productList.warningPrecautions);
        tv_exp_interactions.setText(productList.interactions);
        tv_exp_directions_for_use.setText(productList.directionsForUse);
        tv_exp_side_effects.setText(productList.sideEffects);
        tv_exp_more_info.setText(productList.moreInfo);
        tv_productName.setText(productList.productName);
        tv_disclaimer.setText(productList.disclaimer);
        if (productList.vendorName.toString().length > 15) {
            tv_mfg.setText(
                productList.vendorName.toString()
                    .substring(0, 10) + "" + productList.vendorName.toString()
                    .substring(11, productList.vendorName.toString().length - 1)
            )
        } else {
            tv_mfg.setText(productList.vendorName);
        }
        var offer = (productList.discount)?.toDouble()
        tv_offer.setText("Flat " + offer + " %");
//        tv_offer.setText(productList.discount);

    }

    public fun tvExpandedListener(textView: TextView, imageView: ImageView) {
        if (textView.visibility == View.VISIBLE) {
            tvHide(textView, imageView);
        } else {
            tvShow(textView, imageView);
        }
    }

    public fun tvHide(textView: TextView, imageView: ImageView) {
        textView.visibility = View.GONE;
        imageView.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.ic_baseline_add_24
            )
        );
    }

    public fun tvShow(textView: TextView, imageView: ImageView) {
        textView.visibility = View.VISIBLE;
        imageView.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.ic_baseline_remove_24
            )
        );
    }

}