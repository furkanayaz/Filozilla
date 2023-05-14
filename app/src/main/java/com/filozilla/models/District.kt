package com.filozilla.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class District(
    @SerializedName("ilce_adi")
    @Expose
    val district_name: String,
    @SerializedName("nufus")
    @Expose
    val population: String = "",
    @SerializedName("erkek_nufus")
    @Expose
    val male_population: String = "",
    @SerializedName("kadin_nufus")
    @Expose
    val female_population: String = "",
    @SerializedName("yuzolcumu")
    @Expose
    val acreage: String = "" // Yüz ölçümü demektir.
)