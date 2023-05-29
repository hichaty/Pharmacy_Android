package com.bestoffers.enjoeepharmacy.Models

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class GetOrderModel(
    @SerializedName("code") var code: Int? = null,
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: GetOrderModelData? = GetOrderModelData()

)

data class GetOrderModelData(

    @Nullable @SerializedName("current_page") var currentPage: Int? = null,
    @Nullable @SerializedName("data") var data: ArrayList<GetOrderModelSubData> = arrayListOf(),
    @Nullable @SerializedName("first_page_url") var firstPageUrl: String? = null,
    @Nullable @SerializedName("from") var from: Int? = null,
    @Nullable @SerializedName("last_page") var lastPage: Int? = null,
    @Nullable @SerializedName("last_page_url") var lastPageUrl: String? = null,
    @Nullable @SerializedName("links") var links: ArrayList<Links> = arrayListOf(),
    @Nullable @SerializedName("next_page_url") var nextPageUrl: String? = null,
    @Nullable @SerializedName("path") var path: String? = null,
    @Nullable @SerializedName("per_page") var perPage: Int? = null,
    @Nullable @SerializedName("prev_page_url") var prevPageUrl: String? = null,
    @Nullable @SerializedName("to") var to: Int? = null,
    @Nullable @SerializedName("total") var total: Int? = null

)

data class GetOrderModelSubData(

    @Nullable @SerializedName("id") var id: Int? = null,
    @Nullable @SerializedName("order_id") var orderId: String? = null,
    @Nullable @SerializedName("vendor_id") var vendorId: String? = null,
    @Nullable @SerializedName("user_id") var userId: Int? = null,
    @Nullable @SerializedName("subtotal") var subtotal: String? = null,
    @Nullable @SerializedName("shipping_charges") var shippingCharges: String? = null,
    @Nullable @SerializedName("tax") var tax: String? = null,
    @Nullable @SerializedName("total_price") var totalPrice: String? = null,
    @Nullable @SerializedName("tracsaction_id") var tracsactionId: String? = null,
    @Nullable @SerializedName("payment_type") var paymentType: String? = null,
    @Nullable @SerializedName("payment_status") var paymentStatus: String? = null,
    @Nullable @SerializedName("order_date") var orderDate: String? = null,
    @Nullable @SerializedName("productDetail") var productDetail: ArrayList<GetOrderProductDetail> = arrayListOf()

)

data class GetOrderProductDetail(

    @Nullable @SerializedName("product_id") var productId: Int? = null,
    @Nullable @SerializedName("product_name") var productName: String? = null,
    @Nullable @SerializedName("product_image") var productImage: String? = null,
    @Nullable @SerializedName("sale_price") var salePrice: String? = null,
    @Nullable @SerializedName("quantity") var quantity: Int? = null,
    @Nullable @SerializedName("order_status") var orderStatus: String? = null

)