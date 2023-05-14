package com.filozilla.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.filozilla.repositories.ChangePasswordRepo

class MembershipInfoFragmentVM: ViewModel() {
    private var changePasswordRepo = ChangePasswordRepo()

    fun userCreationDate(date: String): String = changePasswordRepo.userCreationDate(date = date)
    fun saveMembershipInfo(fullName: String, birthDate: String, tcknPassport: String, email: String) = changePasswordRepo.saveMembershipInfo(fullName = fullName, birthDate = birthDate, tcknPassport = tcknPassport, email = email)
    fun updatePw(i_id: Int, pw: String) = changePasswordRepo.updatePw(i_id = i_id, pw = pw)

    fun getMembershipInfoController(): MutableLiveData<Int> = changePasswordRepo.getMembershipInfoController()
    fun getChangePwController(): MutableLiveData<Int> = changePasswordRepo.getChangePwController()
}