package com.filozilla.viewmodels

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.filozilla.interfaces.CommonVMInterface
import com.filozilla.models.Payment
import com.filozilla.repositories.PaymentMethodRepo

class PaymentMethodFragmentVM: ViewModel(), CommonVMInterface {
    private var paymentMethodRepo = PaymentMethodRepo()

    fun getPaymentList(): MutableLiveData<List<Payment>> = paymentMethodRepo.getPaymentList()

    suspend fun getItems(i_id: Int) = paymentMethodRepo.getItems(i_id = i_id)
    fun insertItem(payment: Payment) = paymentMethodRepo.insertItem(payment = payment)
    fun updateItem(payment: Payment) = paymentMethodRepo.updateItem(payment = payment)
    fun deleteItem(payment: Payment) = paymentMethodRepo.deleteItem(payment = payment)

    override fun viewAnimation(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.97f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.97f)
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.80f)

        AnimatorSet().run {
            play(scaleX).with(scaleY).with(alpha)
            duration = 150
            start()
        }
    }

    override fun viewAnimation2(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.97f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.97f, 1.0f)
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 0.80f, 1.0f)

        AnimatorSet().run {
            play(scaleX).with(scaleY).with(alpha)
            duration = 150
            start()
        }
    }

}