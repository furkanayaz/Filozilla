package com.filozilla.responses

import com.filozilla.models.Country
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CRUD3(
    @SerializedName("countries")
    @Expose
    var countries: ArrayList<Country>,
    @SerializedName("success")
    @Expose
    var success: Int
)