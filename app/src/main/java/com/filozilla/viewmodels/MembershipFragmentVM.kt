package com.filozilla.viewmodels

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.text.Spanned
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.filozilla.interfaces.CommonVMInterface
import com.filozilla.models.User
import com.filozilla.repositories.SigninRepo

class MembershipFragmentVM : ViewModel(), CommonVMInterface {
    private var signinRepo = SigninRepo()
    private var tabSignin = MutableLiveData<Spanned>()
    private var tabSignup = MutableLiveData<Spanned>()

    init {
        tabSignin = MutableLiveData<Spanned>(HtmlCompat.fromHtml("<b>Giriş Yap</b>", HtmlCompat.FROM_HTML_MODE_LEGACY))
        tabSignup = MutableLiveData<Spanned>(HtmlCompat.fromHtml("Kayıt Ol", HtmlCompat.FROM_HTML_MODE_LEGACY))
    }

    fun getTabSignin(): MutableLiveData<Spanned> {
        return tabSignin
    }

    fun getTabSignup(): MutableLiveData<Spanned> {
        return tabSignup
    }

    fun tabSigninOnClick() {
        tabSignin.value = HtmlCompat.fromHtml("<b>Giriş Yap</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        tabSignup.value = HtmlCompat.fromHtml("Kayıt Ol", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    fun tabSignupOnClick() {
        tabSignin.value = HtmlCompat.fromHtml("Giriş Yap", HtmlCompat.FROM_HTML_MODE_LEGACY)
        tabSignup.value = HtmlCompat.fromHtml("<b>Kayıt Ol</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    override fun viewAnimation(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.99f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.99f)
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.80f)

        AnimatorSet().run {
            play(scaleX).with(scaleY).with(alpha)
            duration = 150
            start()
        }
    }

    override fun viewAnimation2(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.99f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.99f, 1.0f)
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 0.80f, 1.0f)

        AnimatorSet().run {
            play(scaleX).with(scaleY).with(alpha)
            duration = 150
            start()
        }
    }

    fun signIn(email: String, pw: String) {
        signinRepo.signIn(email = email, pw = pw)
    }

    fun signUp(fullName: String, email: String, phoneNumber: String, pw: String) {
        signinRepo.signUp(fullName = fullName, email = email, phoneNumber = phoneNumber, pw = pw)
    }

    fun getSignInController(): MutableLiveData<Int> = signinRepo.getSignInController()
    fun getSignUpController(): MutableLiveData<Int> = signinRepo.getSignUpController()
    fun getUser(): User? = signinRepo.getUser()

}