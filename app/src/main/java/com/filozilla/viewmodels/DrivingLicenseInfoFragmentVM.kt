package com.filozilla.viewmodels

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.filozilla.interfaces.CommonVMInterface
import com.filozilla.repositories.DrivingLicenseInfoRepo

class DrivingLicenseInfoFragmentVM : ViewModel(), CommonVMInterface {
    private var drivingLicenseInfoRepo = DrivingLicenseInfoRepo()

    override fun viewAnimation(view: View) {
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.50f)

        AnimatorSet().run {
            play(alpha)
            duration = 150
            start()
        }
    }

    override fun viewAnimation2(view: View) {
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 0.50f, 1.0f)

        AnimatorSet().run {
            play(alpha)
            duration = 150
            start()
        }
    }

    fun updateDrivingInfo(uid: Int, drivingNumber: String, drivingChosenClass: String, drivingPickUp: String, drivingIssueDate: String) = drivingLicenseInfoRepo.updateDrivingInfo(uid = uid, drivingNumber = drivingNumber, drivingChosenClass = drivingChosenClass, drivingPickUp = drivingPickUp, drivingIssueDate = drivingIssueDate)

    fun getDrivingLicenseInfoController(): MutableLiveData<Int> = drivingLicenseInfoRepo.getDrivingLicenseInfoController()

}