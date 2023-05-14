package com.filozilla.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class County(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("plate")
    @Expose
    var plate: Int,
    @SerializedName("countrycode")
    @Expose
    var countrycode: String,
    @SerializedName("county")
    @Expose
    var county: String,
)