package com.filozilla.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("plate")
    @Expose
    var plate: Int,
    @SerializedName("countrycode")
    @Expose
    var countrycode: String,
    @SerializedName("city")
    @Expose
    var city: String,
)