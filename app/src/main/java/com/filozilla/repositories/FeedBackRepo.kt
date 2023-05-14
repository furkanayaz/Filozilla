package com.filozilla.repositories

import androidx.lifecycle.MutableLiveData
import com.filozilla.responses.CRUD
import com.filozilla.retrofits.RetrofitUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedBackRepo {
    private val feedBackController = MutableLiveData(3)

    fun getFeedBackController(): MutableLiveData<Int> = feedBackController

    fun sendMail(email: String, fullName: String, faceController: Int, msg: String) {
        val faceText = if (faceController == 1) "Üzgün" else if (faceController == 2) "Kafası Karışmış" else if (faceController == 3) "Memnun" else "Belirtilmemiş"

        CoroutineScope(Dispatchers.Main).launch {
            RetrofitUtils.getRetrofitDaoInterface().sendEmail(
                email = "admin@filozilla.com",
                fullName = "Furkan Ayaz",
                subject = "FeedBack From: $email",
                body = "$fullName/$faceText: $msg"
            ).enqueue(object : Callback<CRUD> {
                override fun onResponse(call: Call<CRUD>, response: Response<CRUD>) {
                    when (response.body()?.success) {
                        0 -> {
                            feedBackController.value = 0
                        }
                        1 -> {
                            feedBackController.value = 1
                        }
                    }
                }

                override fun onFailure(call: Call<CRUD>, t: Throwable) {
                    feedBackController.value = 2
                }

            })
        }
    }
}