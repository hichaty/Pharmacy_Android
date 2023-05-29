package com.bestoffers.enjoeepharmacy.Models

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

class NearestVendorModel(

    @Nullable @SerializedName("code") var code: Int? = null,
    @Nullable @SerializedName("status") var status: Boolean? = null,
    @Nullable @SerializedName("message") var message: String? = null,
    @Nullable @SerializedName("data") var data: NearestVendorModelData? = NearestVendorModelData()

)

data class NearestVendorModelData(

    @Nullable @SerializedName("current_page") var currentPage: Int? = null,
    @Nullable @SerializedName("data") var nearestVendorItemData: ArrayList<NearestVendorItemData> = arrayListOf(),
    @Nullable @SerializedName("first_page_url") var firstPageUrl: String? = null,
    @Nullable @SerializedName("from") var from: Int? = null,
    @Nullable @SerializedName("last_page") var lastPage: Int? = null,
    @Nullable @SerializedName("last_page_url") var lastPageUrl: String? = null,
    @Nullable @SerializedName("links") var vendorLinks: ArrayList<VendorLinks> = arrayListOf(),
    @Nullable @SerializedName("next_page_url") var nextPageUrl: String? = null,
    @Nullable @SerializedName("path") var path: String? = null,
    @Nullable @SerializedName("per_page") var perPage: Int? = null,
    @Nullable @SerializedName("prev_page_url") var prevPageUrl: String? = null,
    @Nullable @SerializedName("to") var to: Int? = null,
    @Nullable @SerializedName("total") var total: Int? = null

)

data class VendorLinks(

    @Nullable @SerializedName("url") var url: String? = null,
    @Nullable @SerializedName("label") var label: String? = null,
    @Nullable @SerializedName("active") var active: Boolean? = null

)

data class NearestVendorItemData(

    @Nullable @SerializedName("id") var id: Int? = null,
    @Nullable @SerializedName("vendor_id") var vendorId: String? = null,
    @Nullable @SerializedName("profile") var profile: String? = null,
    @Nullable @SerializedName("aadhar_card") var aadharCard: String? = null,
    @Nullable @SerializedName("pharmacy_license") var pharmacyLicense: String? = null,
    @Nullable @SerializedName("name") var name: String? = null,
    @Nullable @SerializedName("email") var email: String? = null,
    @Nullable @SerializedName("country_code") var countryCode: String? = null,
    @Nullable @SerializedName("mobile") var mobile: String? = null,
    @Nullable @SerializedName("country_code_mobile") var countryCodeMobile: String? = null,
    @Nullable @SerializedName("gender") var gender: String? = null,
    @Nullable @SerializedName("dob") var dob: String? = null,
    @Nullable @SerializedName("location") var location: String? = null,
    @Nullable @SerializedName("country") var country: String? = null,
    @Nullable @SerializedName("pharmacist_name") var pharmacistName: String? = null,
    @Nullable @SerializedName("store_name") var storeName: String? = null,
    @Nullable @SerializedName("licence_number") var licenceNumber: String? = null,
    @Nullable @SerializedName("aadhar_number") var aadharNumber: String? = null,
    @Nullable @SerializedName("store_location") var storeLocation: String? = null,
    @Nullable @SerializedName("store_image") var storeImage: String? = null,
    @Nullable @SerializedName("store_latitude") var storeLatitude: String? = null,
    @Nullable @SerializedName("store_longitude") var storeLongitude: String? = null,
    @Nullable @SerializedName("bank_name") var bankName: String? = null,
    @Nullable @SerializedName("account_holder_name") var accountHolderName: String? = null,
    @Nullable @SerializedName("bank_branch") var bankBranch: String? = null,
    @Nullable @SerializedName("bank_ifsc_code") var bankIfscCode: String? = null,
    @Nullable @SerializedName("bank_ac_number") var bankAcNumber: String? = null,
    @Nullable @SerializedName("store_status") var storeStatus: String? = null,
    @Nullable @SerializedName("created_at") var createdAt: String? = null,
    @Nullable @SerializedName("distance") var distance: Double? = null,
    @Nullable @SerializedName("vendor_click_id") var vendorClickId: Int? = null

)