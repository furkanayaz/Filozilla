package com.filozilla.viewmodels

import android.animation.ObjectAnimator
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.filozilla.interfaces.CommonVMInterface
import com.filozilla.repositories.FeedBackRepo

class FeedBackFragmentVM: ViewModel(), CommonVMInterface {
    private val feedBackRepo = FeedBackRepo()

    override fun viewAnimation(view: View) {
        ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1.0f).run {
            duration = 1000
            start()
        }
    }

    override fun viewAnimation2(view: View) {

    }

    fun getFeedBackController(): MutableLiveData<Int> = feedBackRepo.getFeedBackController()

    fun sendMail(email: String, fullName: String, faceController: Int, msg: String) {
        feedBackRepo.sendMail(email = email, fullName = fullName, faceController = faceController, msg = msg)
    }
}