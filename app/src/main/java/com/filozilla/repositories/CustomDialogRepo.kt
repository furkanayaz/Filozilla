package com.filozilla.repositories

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.LottieAnimationView
import com.filozilla.R
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("InflateParams")
class CustomDialogRepo(private val context: Context) {
    private var loadingDialog: Dialog? = null
    private val chosenDate = MutableLiveData("")
    private val calendar = Calendar.getInstance()

    fun createLoadingDialog() {
        loadingDialog = Dialog(context).apply {
            this.setCancelable(false)
            this.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
            this.setContentView(R.layout.loading_dialog)
            this.show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun createDatePickerDialog() {
        val sdf = SimpleDateFormat("dd/MM/yyyy")

        DatePickerDialog(context, { _, chosenYear, chosenMonth, chosenDayOfMonth ->
            calendar.set(Calendar.YEAR, chosenYear)
            calendar.set(Calendar.MONTH, chosenMonth)
            calendar.set(Calendar.DAY_OF_MONTH, chosenDayOfMonth)

            chosenDate.value = sdf.format(calendar.time)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun getLoadingDialog(): Dialog? = loadingDialog
    fun getChosenDate(): MutableLiveData<String> = chosenDate

    fun showSuccessDialog(controller: Boolean = false, msg: String) {
        val view = LayoutInflater.from(context).inflate(R.layout.customizable_dialog, null, false)

        Dialog(context).apply {
            setCancelable(false)
            setContentView(view)
            size()
            window?.apply {
                setGravity(Gravity.CENTER and Gravity.CENTER_HORIZONTAL and Gravity.CENTER_VERTICAL)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            val lottie = view.findViewById(R.id.lottieCustomizableDialog) as LottieAnimationView

            if (!controller) {
                lottie.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    setMargins(120, 16, 120, 16)
                }
                lottie.setAnimation(R.raw.success)
            }else
                lottie.visibility = View.GONE

            val tvCustomizable = view.findViewById(R.id.tvCustomizableDialogDesc) as TextView

            tvCustomizable.text = msg

            val actionOk = view.findViewById(
                R.id.
            btnCustomizableDialogOk) as Button
            actionOk.setOnClickListener {
                dismiss()
            }
            show()
        }
    }

    fun showWarningDialog(msg: String) {
        val view = LayoutInflater.from(context).inflate(R.layout.customizable_dialog, null, false)

        Dialog(context).apply {
            setCancelable(false)
            setContentView(view)
            size()
            window?.apply {
                setGravity(Gravity.CENTER and Gravity.CENTER_HORIZONTAL and Gravity.CENTER_VERTICAL)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            val lottie = view.findViewById(R.id.lottieCustomizableDialog) as LottieAnimationView
            val tvCustomizable = view.findViewById(R.id.tvCustomizableDialogDesc) as TextView

            lottie.setAnimation(R.raw.warning)
            tvCustomizable.text = msg

            val actionOk = view.findViewById(
                R.id.
            btnCustomizableDialogOk) as Button
            actionOk.setOnClickListener {
                dismiss()
            }
            show()
        }
    }

    private fun Dialog.size() {
        this.window?.apply {
            setLayout((context.resources.displayMetrics.widthPixels * 0.93).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

}