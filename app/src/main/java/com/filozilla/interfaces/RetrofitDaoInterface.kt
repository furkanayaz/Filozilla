package com.filozilla.interfaces

import com.filozilla.models.User
import com.filozilla.responses.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitDaoInterface {
    @POST("signin.php")
    @FormUrlEncoded
    fun signIn(
        @Field("email") email: String,
        @Field("pw") pw: String
    ): Call<User>

    // A data class must create. Than @Field annotation must replace to @Body [Urgent!]
    @POST("signup.php")
    @FormUrlEncoded
    fun signUp(
        @Field("fullname") fullName: String,
        @Field("birthdate") birthDate: String,
        @Field("tcknpassport") tcknPassport: String,
        @Field("email") email: String,
        @Field("i_phonenumber") i_phoneNumber: String,
        @Field("pw") pw: String
    ): Call<CRUD>

    @POST("delete_account.php")
    @FormUrlEncoded
    fun deleteAccount(@Field("i_id") i_id: Int): Call<CRUD>

    @POST("inquiry_email.php")
    @FormUrlEncoded
    fun inquiryEmail(@Field("email") email: String): Call<CRUD2>

    @GET("inquiry_countries.php")
    fun inquiryCountries(): Call<CRUD3>

    @GET("inquiry_cities.php")
    fun inquiryCities(): Call<CRUD4>

    @GET("inquiry_counties.php")
    fun inquiryCounties(): Call<CRUD5>

    // A data class must create. Than @Field annotation must replace to @Body [Urgent!]
    @POST("webmailer/src/index.php")
    @FormUrlEncoded
    fun sendEmail(
        @Field("email") email: String,
        @Field("fullname") fullName: String,
        @Field("subject") subject: String,
        @Field("body") body: String,
    ): Call<CRUD>

    @POST("forgot_pw.php")
    @FormUrlEncoded
    fun forgotPw(
        @Field("email") email: String,
        @Field("pw") pw: String
    ): Call<CRUD>

    @POST("update_pw.php")
    @FormUrlEncoded
    fun updatePw(
        @Field("i_id") i_id: Int,
        @Field("pw") pw: String): Call<CRUD>

    @POST("update_membership_info.php")
    @FormUrlEncoded
    fun updateMembershipInfo(@Field("fullname") fullName: String, @Field("birthdate") birthDate: String, @Field("tcknpassport") tcknPassport: String, @Field("email") email: String): Call<CRUD>

    @POST("update_driving_info.php")
    @FormUrlEncoded
    fun updateDrivingInfo(@Field("d_uid") uid: Int, @Field("drivingnumber") drivingNumber: String, @Field("drivingchosenclass") drivingChosenClass: String, @Field("drivingpickup") drivingPickUp: String, @Field("drivingissuedate") drivingIssueDate: String): Call<CRUD>

    // A data class must create. Than @Field annotation must replace to @Body [Urgent!]
    @POST("update_billing_info.php")
    @FormUrlEncoded
    fun updateBillingInfo(
        @Field("b_uid") uid: Int,
        @Field("b_phonenumber") phoneNumber: String,
        @Field("country") country: String,
        @Field("city") city: String,
        @Field("county") county: String,
        @Field("address") address: String,
        @Field("postcode") postCode: Int): Call<CRUD>
}
