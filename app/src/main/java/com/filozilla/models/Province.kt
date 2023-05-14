package com.filozilla.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Province(
    @SerializedName("il_adi")
    @Expose
    val province_name: String,
    @SerializedName("plaka_kodu")
    @Expose
    val plate_code: String,
    @SerializedName("alan_kodu")
    @Expose
    val area_code: String = "",
    @SerializedName("nufus")
    @Expose
    val population: String = "",
    @SerializedName("bolge")
    @Expose
    val region: String = "",
    @SerializedName("yuzolcumu")
    @Expose
    val acreage: String = "", // Yüz ölçümü demektir.
    @SerializedName("erkek_nufus_yuzde")
    @Expose
    val male_population_percentage: String = "",
    @SerializedName("erkek_nufus")
    @Expose
    val male_population: String = "",
    @SerializedName("kadin_nufus_yuzde")
    @Expose
    val female_population_percentage: String = "",
    @SerializedName("kadin_nufus")
    @Expose
    val female_population: String = "",
    @SerializedName("nufus_yuzdesi_genel")
    @Expose
    val general_population_percentage: String = "",
    @SerializedName("ilceler")
    @Expose
    val districts: List<District>,
    @SerializedName("kisa_bilgi")
    @Expose
    val short_info: String = ""
)