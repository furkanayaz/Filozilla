package com.filozilla.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.filozilla.activities.MainActivity
import com.filozilla.interfaces.CommonVMInterface
import com.filozilla.repositories.DeleteAccountRepo
import com.filozilla.rooms.RoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DeleteAccountFragmentVM: ViewModel(), CommonVMInterface {
    private val deleteAccountRepo = DeleteAccountRepo()

    override fun viewAnimation(view: View) {

    }

    override fun viewAnimation2(view: View) {

    }

    fun getDeleteAccountController(): MutableLiveData<Int> = deleteAccountRepo.getDeleteAccountController()
    fun getSendMailController(): MutableLiveData<Int> = deleteAccountRepo.getSendMailController()

    fun deleteAccount() = deleteAccountRepo.deleteAccount()

    fun deletePaymentMethods(msg: String) {
        MainActivity.user?.let { user ->
            CoroutineScope(Dispatchers.Main).launch {
                RoomDB.INSTANCE?.getPaymentDao()?.deletePaymentMethods(i_id = user.iId)
                sendMail(msg = msg)
                this.cancel()
            }
        }
    }

    private fun sendMail(msg: String) = deleteAccountRepo.sendMail(msg = msg)
}