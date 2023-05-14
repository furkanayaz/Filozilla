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

class DrivingLicenseInfoRepo {
    private val drivingLicenseInfoController = MutableLiveData(3)

    fun updateDrivingInfo(uid: Int, drivingNumber: String, drivingChosenClass: String, drivingPickUp: String, drivingIssueDate: String) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitUtils.getRetrofitDaoInterface().updateDrivingInfo(uid = uid, drivingNumber = drivingNumber, drivingChosenClass = drivingChosenClass, drivingPickUp = drivingPickUp, drivingIssueDate = drivingIssueDate).enqueue(object : Callback<CRUD> {
                override fun onResponse(call: Call<CRUD>, response: Response<CRUD>) {
                    when(response.body()?.success) {
                        0 -> drivingLicenseInfoController.value = 0
                        1 -> drivingLicenseInfoController.value = 1
                    }
                }

                override fun onFailure(call: Call<CRUD>, t: Throwable) {
                    drivingLicenseInfoController.value = 2
                }

            })
        }
    }

    fun getDrivingLicenseInfoController(): MutableLiveData<Int> = drivingLicenseInfoController
}