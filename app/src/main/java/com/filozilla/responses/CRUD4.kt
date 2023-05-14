package com.filozilla.responses

import com.filozilla.models.City
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CRUD4(
    @SerializedName("cities")
    @Expose
    var cities: ArrayList<City>,
    @SerializedName("success")
    @Expose
    var success: Int
)