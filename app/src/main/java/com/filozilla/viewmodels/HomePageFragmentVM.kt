package com.filozilla.viewmodels

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.text.Spanned
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.filozilla.interfaces.CommonVMInterface

class HomePageFragmentVM : ViewModel(), CommonVMInterface {
    private var tabDaily = MutableLiveData<Spanned>()
    private var tabMonthly = MutableLiveData<Spanned>()

    init {
        tabDaily = MutableLiveData<Spanned>(HtmlCompat.fromHtml("<b>Günlük Kiralama</b>", HtmlCompat.FROM_HTML_MODE_LEGACY))
        tabMonthly = MutableLiveData<Spanned>(HtmlCompat.fromHtml("Aylık Kiralama", HtmlCompat.FROM_HTML_MODE_LEGACY))
    }

    fun getDailyEnableListener(): MutableLiveData<Spanned> {
        return tabDaily
    }

    fun getMonthlyEnableListener(): MutableLiveData<Spanned> {
        return tabMonthly
    }

    fun tabDailyOnClick() {
        tabDaily.value = HtmlCompat.fromHtml("<b>Günlük Kiralama</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        tabMonthly.value = HtmlCompat.fromHtml("Aylık Kiralama", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    fun tabMonthlyOnClick() {
        tabDaily.value = HtmlCompat.fromHtml("Günlük Kiralama", HtmlCompat.FROM_HTML_MODE_LEGACY)
        tabMonthly.value = HtmlCompat.fromHtml("<b>Aylık Kiralama</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
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