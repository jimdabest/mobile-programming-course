package com.uth.week05.uthsmarttasks.model

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("price") val price: Double?, // Kiểu số thập phân
    @SerializedName("des") val description: String?, // Map chuẩn với API
    @SerializedName("imgURL") val imageUrl: String?  // Map chuẩn với API
)