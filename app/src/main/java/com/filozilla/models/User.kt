package com.filozilla.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("i_id")
    @Expose
    var iId: Int,
    @SerializedName("status")
    @Expose
    var status: Int,
    @SerializedName("fullname")
    @Expose
    var fullName: String,
    @SerializedName("birthdate")
    @Expose
    var birthDate: String,
    @SerializedName("tcknpassport")
    @Expose
    var tcknPassport: String,
    @SerializedName("email")
    @Expose
    var email: String,
    @SerializedName("hashed_email")
    @Expose
    var hashedEmail: String,
    @SerializedName("i_phonenumber")
    @Expose
    var iPhoneNumber: String,
    @SerializedName("pw")
    @Expose
    var pw: String,
    @SerializedName("hashed_pw")
    @Expose
    var hashedPw: String,
    @SerializedName("i_creationdate")
    @Expose
    var iCreationDate: String,

    @SerializedName("d_uid")
    @Expose
    var dUid: Int,
    @SerializedName("drivingnumber")
    @Expose
    var drivingNumber: Int?,
    @SerializedName("drivingchosenclass")
    @Expose
    var drivingChosenClass: String,
    @SerializedName("drivingpickup")
    @Expose
    var drivingPickUp: String,
    @SerializedName("drivingissuedate")
    @Expose
    var drivingIssueDate: String,
    @SerializedName("d_creationdate")
    @Expose
    var dCreationDate: String,

    @SerializedName("b_uid")
    @Expose
    var bUid: Int,
    @SerializedName("b_phonenumber")
    @Expose
    var bPhoneNumber: String,
    @SerializedName("country")
    @Expose
    var country: String,
    @SerializedName("city")
    @Expose
    var city: String,
    @SerializedName("county")
    @Expose
    var county: String,
    @SerializedName("address")
    @Expose
    var address: String,
    @SerializedName("postcode")
    @Expose
    var postCode: Int?,
    @SerializedName("b_creationdate")
    @Expose
    var bCreationDate: String,
    @SerializedName("success")
    @Expose
    var success: Int,
    @SerializedName("message")
    @Expose
    var message: String
)