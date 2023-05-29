package com.bestoffers.enjoeepharmacy.Models

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

class ResendOtpModel (
    @Nullable
    @SerializedName("code") var code: Int,

    @Nullable
    @SerializedName("status") var status: Boolean,

    @Nullable
    @SerializedName("message") var message: String,

//    @Nullable
//    @SerializedName("data") var data: ResendOtpData,

//    @Nullable
//    @SerializedName("access_token") var accessToken: String,
//
//    @Nullable
//    @SerializedName("token_type") var tokenType: String

)

data class ResendOtpData(
    @Nullable
    @SerializedName("id") var id: Int
)