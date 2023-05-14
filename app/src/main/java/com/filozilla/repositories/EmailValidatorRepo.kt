package com.filozilla.repositories

import androidx.lifecycle.MutableLiveData
import com.filozilla.responses.CRUD2
import com.filozilla.retrofits.RetrofitUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailValidatorRepo {
    private var validateController = MutableLiveData(3)
    private var email = "" ; private var fullName = ""

    fun getValidateController(): MutableLiveData<Int> {
        return validateController
    }

    fun getEmail(): String = this.email
    fun getFullName(): String = this.fullName

    fun validateEmail(email: String) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitUtils.getRetrofitDaoInterface().inquiryEmail(email = email).enqueue(object :
                Callback<CRUD2> {
                override fun onResponse(call: Call<CRUD2>, response: Response<CRUD2>) {
                    when (response.body()?.success) {
                        0 -> {
                            this@launch.cancel()
                            validateController.value = 0
                        }
                        1 -> {
                            response.body()?.let {
                                this@EmailValidatorRepo.email = it.email
                                this@EmailValidatorRepo.fullName = it.fullName
                            }
                            this@launch.cancel()
                            validateController.value = 1
                        }
                    }
                }

                override fun onFailure(call: Call<CRUD2>, t: Throwable) {
                    this@launch.cancel()
                    validateController.value = 2
                }

            })
        }
    }
}