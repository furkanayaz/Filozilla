package com.filozilla.repositories

import androidx.lifecycle.MutableLiveData
import com.filozilla.activities.MainActivity
import com.filozilla.responses.CRUD
import com.filozilla.retrofits.RetrofitUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteAccountRepo {
    private val deleteAccountController = MutableLiveData(3)
    private val sendMailController = MutableLiveData(3)

    fun getDeleteAccountController(): MutableLiveData<Int> = deleteAccountController
    fun getSendMailController(): MutableLiveData<Int> = sendMailController

    fun deleteAccount() {
        MainActivity.user?.let { user ->
            CoroutineScope(Dispatchers.Main).launch {
                RetrofitUtils.getRetrofitDaoInterface().deleteAccount(i_id = user.iId).enqueue(object : Callback<CRUD> {
                    override fun onResponse(call: Call<CRUD>, response: Response<CRUD>) {
                        when(response.body()?.success) {
                            0 -> deleteAccountController.value = 0
                            1 -> deleteAccountController.value = 1
                        }
                    }

                    override fun onFailure(call: Call<CRUD>, t: Throwable) {
                        deleteAccountController.value = 2
                    }

                })
            }
        }
    }

    fun sendMail(msg: String) {
        MainActivity.user?.let { user ->
            CoroutineScope(Dispatchers.Main).launch {
                val body = "<b>HESAP BİLGİLERİ</b><br>AD SOYAD: ${user.fullName}<br>E-POSTA ADRESİ: ${user.email}<br>TELEFON NUMARASI: ${user.iPhoneNumber}<br><br>Üyelik Silme Nedeni: $msg"

                RetrofitUtils.getRetrofitDaoInterface().sendEmail(email = "admin@filozilla.com", fullName = user.fullName, subject = "Üyelik Silindi", body = body).enqueue(object : Callback<CRUD> {
                    override fun onResponse(call: Call<CRUD>, response: Response<CRUD>) {
                        when(response.body()?.success) {
                            0 -> sendMailController.value = 0
                            1 -> sendMailController.value = 1
                        }
                    }

                    override fun onFailure(call: Call<CRUD>, t: Throwable) {
                        sendMailController.value = 2
                    }

                })
            }
        }
    }

}