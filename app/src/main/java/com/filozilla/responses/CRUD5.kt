package com.filozilla.responses

import com.filozilla.models.County
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CRUD5(
    @SerializedName("counties")
    @Expose
    var counties: ArrayList<County>,
    @SerializedName("success")
    @Expose
    var success: Int
)