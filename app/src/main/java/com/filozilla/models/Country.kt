package com.filozilla.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("countrycode")
    @Expose
    var countryCode: String,
    @SerializedName("country")
    @Expose
    var country: String,
)