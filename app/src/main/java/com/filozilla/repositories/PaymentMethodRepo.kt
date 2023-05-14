package com.filozilla.repositories

import androidx.lifecycle.MutableLiveData
import com.filozilla.models.Payment
import com.filozilla.rooms.RoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class PaymentMethodRepo {
    private val paymentList = MutableLiveData<List<Payment>>()

    fun getPaymentList(): MutableLiveData<List<Payment>> = paymentList

    suspend fun getItems(i_id: Int) {
        paymentList.value = RoomDB.INSTANCE?.getPaymentDao()?.getPaymentMethods(i_id = i_id)
    }

    fun insertItem(payment: Payment) {
        CoroutineScope(Dispatchers.Main).launch {
            RoomDB.INSTANCE?.getPaymentDao()?.insertPaymentMethod(payment = payment)
            getItems(i_id = payment.iId)
            this.cancel()
        }
    }

    fun updateItem(payment: Payment) {
        CoroutineScope(Dispatchers.Main).launch {
            RoomDB.INSTANCE?.getPaymentDao()?.updatePaymentMethod(payment = payment)
            getItems(i_id = payment.iId)
            this.cancel()
        }
    }

    fun deleteItem(payment: Payment) {
        CoroutineScope(Dispatchers.Main).launch {
            RoomDB.INSTANCE?.getPaymentDao()?.deletePaymentMethod(payment = payment)
            getItems(i_id = payment.iId)
            this.cancel()
        }
    }

}