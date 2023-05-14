package com.filozilla.viewmodels

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.filozilla.interfaces.CommonVMInterface
import com.filozilla.repositories.SendEmailRepo

class SecurityCodeFragmentVM : ViewModel(), CommonVMInterface {
    private var sendEmailRepo = SendEmailRepo()
    var sendController = MutableLiveData<Int>()
    var time = MutableLiveData<String>()

    init {
        sendController = sendEmailRepo.getSendCheck()
        time = sendEmailRepo.getTimeData()
    }

    fun sendEmail(email: String, fullName: String, subject: String, body: String) {
        sendEmailRepo.sendEmail(email = email, fullName = fullName, subject = subject, body = body)
    }

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