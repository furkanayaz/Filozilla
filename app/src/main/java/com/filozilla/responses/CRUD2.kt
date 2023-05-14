package com.filozilla.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CRUD2(
    @SerializedName("fullname")
    @Expose
    var fullName: String,
    @SerializedName("email")
    @Expose
    var email: String,
    @SerializedName("pw")
    @Expose
    var pw: String,
    @SerializedName("success")
    @Expose
    var success: Int,
    @SerializedName("message")
    @Expose
    var message: String
)