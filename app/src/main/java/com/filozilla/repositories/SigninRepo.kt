package com.filozilla.repositories

import androidx.lifecycle.MutableLiveData
import com.filozilla.models.User
import com.filozilla.responses.CRUD
import com.filozilla.retrofits.RetrofitUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninRepo {
    private var signInController = MutableLiveData(4)
    private var signUpController = MutableLiveData(3)
    private var user: User? = null

    fun signIn(email: String, pw: String) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitUtils.getRetrofitDaoInterface().signIn(email = email, pw = pw).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    response.body()?.let {
                        when(it.success) {
                            0 -> {
                                signInController.value = 0
                                this@launch.cancel()
                            }
                            1 -> {
                                user = it
                                if (it.status == 0) {
                                    signInController.value = 2
                                }else if (it.status == 1) {
                                    signInController.value = 1
                                }
                                this@launch.cancel()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    signInController.value = 3
                    this@launch.cancel()
                }
            })
        }
    }

    fun signUp(fullName: String, email: String, phoneNumber: String, pw: String) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitUtils.getRetrofitDaoInterface().signUp(
                fullName = fullName,
                email = email,
                i_phoneNumber = phoneNumber,
                pw = pw,
                birthDate = "",
                tcknPassport = ""
            ).enqueue(object : Callback<CRUD> {
                override fun onResponse(call: Call<CRUD>, response: Response<CRUD>) {
                    when (response.body()?.success) {
                        0 -> signUpController.value = 0
                        1 -> signUpController.value = 1
                    }

                    this@launch.cancel()
                }

                override fun onFailure(call: Call<CRUD>, t: Throwable) {
                    signUpController.value = 2
                    this@launch.cancel()
                }

            })
        }
    }

    fun getSignInController(): MutableLiveData<Int> = signInController
    fun getSignUpController(): MutableLiveData<Int> = signUpController
    fun getUser(): User? = user

}