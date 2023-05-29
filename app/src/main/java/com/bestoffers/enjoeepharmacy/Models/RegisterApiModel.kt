package com.bestoffers.enjoeepharmacy.Models

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class RegisterApiModel(
    @Nullable
    @SerializedName("code")
    val code: Int,
    @Nullable
    @SerializedName("status")
    val status: Boolean,
    @Nullable
    @SerializedName("message")
    val message: String,
    @Nullable
    @SerializedName("data")
    val data: RegisterData
)

data class RegisterData(
    @Nullable
    @SerializedName("id")
    val id: Int
)