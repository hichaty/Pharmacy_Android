package com.bestoffers.enjoeepharmacy.Models

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class CategoriesListModel(

    @Nullable @SerializedName("code") var code: Int? = null,
    @Nullable @SerializedName("status") var status: Boolean? = null,
    @Nullable @SerializedName("message") var message: String? = null,
    @Nullable @SerializedName("data") var data: ArrayList<DataCategories> = arrayListOf()

)

data class DataCategories(

    @Nullable @SerializedName("id") var id: Int? = null,
    @Nullable @SerializedName("name") var name: String? = null,
//    @Nullable @SerializedName("description") var description: String? = null,
    @Nullable @SerializedName("image") var image: String? = null,
//    @Nullable @SerializedName("file_path") var filePath: String? = null,
//    @Nullable @SerializedName("created_by") var createdBy: String? = null,
//    @Nullable @SerializedName("created_by_ip") var createdByIp: String? = null,
//    @Nullable @SerializedName("status") var status: Int? = null,
//    @Nullable @SerializedName("created_at") var createdAt: String? = null,
//    @Nullable @SerializedName("updated_at") var updatedAt: String? = null

)