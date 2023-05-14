package com.filozilla.repositories

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.filozilla.responses.CRUD
import com.filozilla.retrofits.RetrofitUtils
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SendEmailRepo {
    private var time = MutableLiveData<String>()
    var sendController = MutableLiveData<Int>()

    init {
        sendController = MutableLiveData(-1)
        time = MutableLiveData("Yeniden Gönder")
    }

    fun getSendCheck(): MutableLiveData<Int> {
        return sendController
    }

    fun getTimeData(): MutableLiveData<String> {
        return time
    }

    fun sendEmail(email: String, fullName: String, subject: String, body: String) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitUtils.getRetrofitDaoInterface().sendEmail(
                email = email,
                fullName = fullName,
                subject = subject,
                body = body
            ).enqueue(object : Callback<CRUD> {
                override fun onResponse(call: Call<CRUD>, response: Response<CRUD>) {
                    when (response.body()?.success) {
                        0 -> {
                            time.value = "Yeniden Gönder"
                            sendController.value = 2
                            this@launch.cancel()
                        }
                        1 -> {
                            timer()
                            sendController.value = 1
                            this@launch.cancel()
                        }
                    }
                }

                override fun onFailure(call: Call<CRUD>, t: Throwable) {
                    time.value = "Yeniden Gönder"
                    sendController.value = 3
                    this@launch.cancel()
                }

            })
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun timer() {
        CoroutineScope(Dispatchers.Main).launch {
            var counter = 0
            val sdf = SimpleDateFormat("mm:ss")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = 180000

            while (true) {
                if (counter < 180) {
                    calendar.timeInMillis -= 1000
                    time.value = sdf.format(calendar.time).toString()
                    counter++
                    delay(1000)
                }else {
                    time.value = "Yeniden Gönder"
                    this.cancel()
                    break
                }
            }
        }
    }

}