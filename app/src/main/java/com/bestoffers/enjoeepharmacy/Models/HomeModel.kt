package com.bestoffers.enjoeepharmacy.Models

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HomeModel(

    @Nullable @SerializedName("code") var code: Int? = null,
    @Nullable @SerializedName("status") var status: Boolean? = null,
    @Nullable @SerializedName("message") var message: String? = null,
    @Nullable @SerializedName("data") var data: DataMain? = DataMain()

) : Serializable

data class DataMain(

    @Nullable @SerializedName("current_page") var currentPage: Int? = null,
    @Nullable @SerializedName("data") var dataProductList: ArrayList<DataProductList> = arrayListOf(),
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

) : Serializable

data class Links(

    @Nullable @SerializedName("url") var url: String? = null,
    @Nullable @SerializedName("label") var label: String? = null,
    @Nullable @SerializedName("active") var active: Boolean? = null

) : Serializable

data class DataProductList(

//    @Nullable @SerializedName("id") var id: Int? = null,
//    @Nullable @SerializedName("product_id") var productId: String? = null,
//    @Nullable @SerializedName("category_id") var categoryId: Int? = null,
//    @Nullable @SerializedName("product_code") var productCode: String? = null,
//    @Nullable @SerializedName("product_name") var productName: String? = null,
//    @Nullable @SerializedName("product_slug") var productSlug: String? = null,
//    @Nullable @SerializedName("image") var image: String? = null,
//    @Nullable @SerializedName("stock") var stock: String? = null,
//    @Nullable @SerializedName("descriptions") var descriptions: String? = null,
//    @Nullable @SerializedName("mrp") var mrp: String? = null,
//    @Nullable @SerializedName("sale_price") var salePrice: String? = null,
//    @Nullable @SerializedName("discount") var discount: String? = null,
//    @Nullable @SerializedName("discount_type") var discountType: String? = null,
//    @Nullable @SerializedName("gst_code") var gstCode: String? = null,
//    @Nullable @SerializedName("gst_cgst") var gstCgst: String? = null,
//    @Nullable @SerializedName("gst_sgst") var gstSgst: String? = null,
//    @Nullable @SerializedName("prescription") var prescription: String? = null,
//    @Nullable @SerializedName("strength") var strength: String? = null,
//    @Nullable @SerializedName("net_quantity") var netQuantity: String? = null,
//    @Nullable @SerializedName("item_weight") var itemWeight: String? = null,
//    @Nullable @SerializedName("ingredient") var ingredient: String? = null,
//    @Nullable @SerializedName("hsn_code") var hsnCode: String? = null,
//    @Nullable @SerializedName("country_of_origin") var countryOfOrigin: String? = null,
//    @Nullable @SerializedName("customer_care_no") var customerCareNo: String? = null,
//    @Nullable @SerializedName("directions_to_use") var directionsToUse: String? = null,
//    @Nullable @SerializedName("safety_precaution") var safetyPrecaution: String? = null,
//    @Nullable @SerializedName("name_of_manufacturer_marketer") var nameOfManufacturerMarketer: String? = null,
//    @Nullable @SerializedName("address_of_manufacturer_marketer") var addressOfManufacturerMarketer: String? = null,
//    @Nullable @SerializedName("uses") var uses: String? = null,
//    @Nullable @SerializedName("warning_precautions") var warningPrecautions: String? = null,
//    @Nullable @SerializedName("interactions") var interactions: String? = null,
//    @Nullable @SerializedName("directions_for_use") var directionsForUse: String? = null,
//    @Nullable @SerializedName("side_effects") var sideEffects: String? = null,
//    @Nullable @SerializedName("more_info") var moreInfo: String? = null,
//    @Nullable @SerializedName("disclaimer") var disclaimer: String? = null,
//    @Nullable @SerializedName("category_name") var categoryName: String? = null,
//    @Nullable @SerializedName("product_click_id") var product_click_id: Int? = null,


    @Nullable @SerializedName("id")
    var id: Int? = null,
    @Nullable @SerializedName("product_id")
    var productId: String? = null,
    @Nullable @SerializedName("category_id")
    var categoryId: Int? = null,
    @Nullable @SerializedName("product_code")
    var productCode: String? = null,
    @Nullable @SerializedName("product_name")
    var productName: String? = null,
    @Nullable @SerializedName("product_slug")
    var productSlug: String? = null,
    @Nullable @SerializedName("image")
    var image: String? = null,
    @Nullable @SerializedName("stock")
    var stock: String? = null,
    @Nullable @SerializedName("descriptions")
    var descriptions: String? = null,
    @Nullable @SerializedName("mrp")
    var mrp: String? = null,
    @Nullable @SerializedName("sale_price")
    var salePrice: String? = null,
    @Nullable @SerializedName("discount")
    var discount: String? = null,
    @Nullable @SerializedName("discount_type")
    var discountType: String? = null,
    @Nullable @SerializedName("gst_code")
    var gstCode: String? = null,
    @Nullable @SerializedName("gst_cgst")
    var gstCgst: String? = null,
    @Nullable @SerializedName("gst_sgst")
    var gstSgst: String? = null,
    @Nullable @SerializedName("prescription")
    var prescription: String? = null,
    @Nullable @SerializedName("strength")
    var strength: String? = null,
    @Nullable @SerializedName("net_quantity")
    var netQuantity: String? = null,
    @Nullable @SerializedName("item_weight")
    var itemWeight: String? = null,
    @Nullable @SerializedName("ingredient")
    var ingredient: String? = null,
    @Nullable @SerializedName("hsn_code")
    var hsnCode: String? = null,
    @Nullable @SerializedName("country_of_origin")
    var countryOfOrigin: String? = null,
    @Nullable @SerializedName("customer_care_no")
    var customerCareNo: String? = null,
    @Nullable @SerializedName("directions_to_use")
    var directionsToUse: String? = null,
    @Nullable @SerializedName("safety_precaution")
    var safetyPrecaution: String? = null,
    @Nullable @SerializedName("name_of_manufacturer_marketer")
    var nameOfManufacturerMarketer: String? = null,
    @Nullable @SerializedName("address_of_manufacturer_marketer")
    var addressOfManufacturerMarketer: String? = null,
    @Nullable @SerializedName("uses")
    var uses: String? = null,
    @Nullable @SerializedName("warning_precautions")
    var warningPrecautions: String? = null,
    @Nullable @SerializedName("interactions")
    var interactions: String? = null,
    @Nullable @SerializedName("directions_for_use")
    var directionsForUse: String? = null,
    @Nullable @SerializedName("side_effects")
    var sideEffects: String? = null,
    @Nullable @SerializedName("more_info")
    var moreInfo: String? = null,
    @Nullable @SerializedName("disclaimer")
    var disclaimer: String? = null,
    @Nullable @SerializedName("product_vendor_id")
    var productVendorId: Int? = null,
    @Nullable @SerializedName("vendor_product_id")
    var vendorProductId: Int? = null,
    @Nullable @SerializedName("category_name")
    var categoryName: String? = null,
    @Nullable @SerializedName("vendor_id")
    var vendorId: String? = null,
    @Nullable @SerializedName("vendor_profile")
    var vendorProfile: String? = null,
    @Nullable @SerializedName("aadhar_card")
    var aadharCard: String? = null,
    @Nullable @SerializedName("pharmacy_license")
    var pharmacyLicense: String? = null,
    @Nullable @SerializedName("vendor_name")
    var vendorName: String? = null,
    @Nullable @SerializedName("vendor_email")
    var vendorEmail: String? = null,
    @Nullable @SerializedName("product_click_id")
    var productClickId: Int? = null,
    @Nullable @SerializedName("vendor_click_id")
    var vendorClickId: Int? = null,

    @Nullable @SerializedName("country_code")
    var countryCode: Int? = null,
    @Nullable @SerializedName("mobile")
    var mobile: Long? = null,
    @Nullable @SerializedName("country_code_mobile")
    var countryCodeMobile: Long? = null,


    @Nullable @SerializedName("gender") var gender: String? = null,
    @Nullable @SerializedName("location") var location: String? = null,
    @Nullable @SerializedName("country") var country: String? = null,
    @Nullable @SerializedName("pharmacist_name") var pharmacistName: String? = null,
    @Nullable @SerializedName("store_name") var storeName: String? = null,
    @Nullable @SerializedName("licence_number") var licenceNumber: String? = null,
    @Nullable @SerializedName("aadhar_number") var aadharNumber: String? = null,
    @Nullable @SerializedName("store_location") var storeLocation: String? = null,
    @Nullable @SerializedName("store_image") var storeImage: String? = null,
    @Nullable @SerializedName("bank_name") var bankName: String? = null,
    @Nullable @SerializedName("account_holder_name") var accountHolderName: String? = null,
    @Nullable @SerializedName("bank_branch") var bankBranch: String? = null,
    @Nullable @SerializedName("bank_ifsc_code") var bankIfscCode: String? = null,
    @Nullable @SerializedName("bank_ac_number") var bankAcNumber: String? = null,
    @Nullable @SerializedName("store_status") var storeStatus: String? = null,
    @Nullable @SerializedName("product_vendor_table_id") var productVendorTableId: Int? = null,
    @Nullable @SerializedName("vendor_stock") var vendorStock: String? = null,
    @Nullable @SerializedName("vendor_mrp") var vendorMrp: String? = null,
    @Nullable @SerializedName("vendor_sale_price") var vendorSalePrice: String? = null,
    @Nullable @SerializedName("cart_id") var cartId: Int? = null,
    @Nullable @SerializedName("quantity_cart") var quantityCart: Int? = null

) : Serializable