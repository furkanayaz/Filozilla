package com.filozilla.repositories

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.filozilla.responses.CRUD
import com.filozilla.retrofits.RetrofitUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar

class ChangePasswordRepo {
    private val membershipInfoController = MutableLiveData(3)
    private val changePwController = MutableLiveData(3)

    @SuppressLint("SimpleDateFormat")
    fun userCreationDate(date: String): String {
        val calendar = Calendar.getInstance()
        val tempSdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        tempSdf.parse(date)
        calendar.time = tempSdf.calendar.time
        val sdf = SimpleDateFormat("dd/MM/yyyy - HH:mm")
        return sdf.format(calendar.time)
    }

    fun saveMembershipInfo(fullName: String, birthDate: String, tcknPassport: String, email: String) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitUtils.getRetrofitDaoInterface().updateMembershipInfo(fullName = fullName, birthDate = birthDate, tcknPassport = tcknPassport, email = email).enqueue(object : Callback<CRUD> {
                override fun onResponse(call: Call<CRUD>, response: Response<CRUD>) {
                    when(response.body()?.success) {
                        0 -> membershipInfoController.value = 0
                        1 -> membershipInfoController.value = 1
                    }
                }

                override fun onFailure(call: Call<CRUD>, t: Throwable) {
                    membershipInfoController.value = 2
                }

            })
        }
    }

    fun updatePw(i_id: Int, pw: String) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitUtils.getRetrofitDaoInterface().updatePw(i_id = i_id, pw = pw).enqueue(object : Callback<CRUD> {
                override fun onResponse(call: Call<CRUD>, response: Response<CRUD>) {
                    when(response.body()?.success) {
                        0 -> changePwController.value = 0
                        1 -> changePwController.value = 1
                    }
                }

                override fun onFailure(call: Call<CRUD>, t: Throwable) {
                    changePwController.value = 2
                }

            })
        }
    }

    fun getMembershipInfoController(): MutableLiveData<Int> = membershipInfoController
    fun getChangePwController(): MutableLiveData<Int> = changePwController
}