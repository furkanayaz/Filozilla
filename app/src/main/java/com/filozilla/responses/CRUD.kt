package com.filozilla.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CRUD(
    @SerializedName("success")
    @Expose
    var success: Int,
    @SerializedName("message")
    @Expose
    var message: String
)