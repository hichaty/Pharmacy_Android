package com.bestoffers.enjoeepharmacy.Models

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class VerifyOtpModel(
    @Nullable
    @SerializedName("code") var code: Int,

    @Nullable
    @SerializedName("status") var status: Boolean,

    @Nullable
    @SerializedName("message") var message: String,

//    @Nullable
//    @SerializedName("data") var data: VerifyOtpData,

    @Nullable
    @SerializedName("access_token") var accessToken: String,

    @Nullable
    @SerializedName("token_type") var tokenType: String

)

data class VerifyOtpData(
    @Nullable
    @SerializedName("id") var id: Int
)