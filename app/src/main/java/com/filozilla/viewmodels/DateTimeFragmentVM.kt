package com.filozilla.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.filozilla.datatransfers.DateTimeTransfer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class DateTimeFragmentVM: ViewModel() {

    @SuppressLint("SimpleDateFormat")
    fun editDate(year: Int, month: Int, day_of_month: Int): Date? {
        val calendar2 = Calendar.getInstance()
        calendar2.set(Calendar.DAY_OF_MONTH, day_of_month)
        calendar2.set(Calendar.MONTH, month)
        calendar2.set(Calendar.YEAR, year)
        var sdf = SimpleDateFormat("dd MMM")
        DateTimeTransfer.monthlyDate = sdf.format(calendar2.time)

        sdf = SimpleDateFormat("dd/MM/yyyy")

        return sdf.parse("$day_of_month/${month + 1}/$year")
    }

    fun editTime(date: Date?, hour: Int, minute: Int) {
        val editedHour = String.format("%02d", hour)
        val editedMinute = String.format("%02d", minute)

        date?.let {
            Calendar.getInstance().run {
                this.time = it
                this.set(Calendar.HOUR_OF_DAY, editedHour.toInt())
                this.set(Calendar.MINUTE, editedMinute.toInt())
                DateTimeTransfer.monthlyDateMillis = this.timeInMillis
            }
        }

        DateTimeTransfer.monthlyDate += " $editedHour"
        DateTimeTransfer.monthlyDate += ":$editedMinute"
    }
}