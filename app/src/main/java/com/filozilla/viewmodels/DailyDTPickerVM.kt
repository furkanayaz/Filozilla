package com.filozilla.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.filozilla.R
import com.filozilla.datatransfers.DateTimeTransfer
import com.filozilla.models.DailyDateSelection
import com.filozilla.repositories.CustomDialogRepo
import com.filozilla.repositories.DailyDTPickerRepo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.atomic.AtomicInteger

@SuppressLint("SimpleDateFormat")
class DailyDTPickerVM: ViewModel() {
    private val dailyDTPickerRepo = DailyDTPickerRepo()
    val timeList = listOf("00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30", "05:00", "05:30", "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30")

    fun getDayClick(): AtomicInteger = dailyDTPickerRepo.onClickController

    fun setFirst(
        chosenFirstPos: Int,
        chosenFirstDay: Int,
        chosenFirstDate: String
    ) {
        getSelection().run {
            if (this.chosenLastPos == 0 && this.chosenLastDay == 0) {
                this.chosenFirstPos = chosenFirstPos
                this.chosenFirstDay = chosenFirstDay
                this.chosenFirstDate = chosenFirstDate
            } else {
                this.chosenFirstPos = chosenFirstPos
                this.chosenFirstDay = chosenFirstDay
                this.chosenFirstDate = chosenFirstDate
                this.chosenLastPos = 0
                this.chosenLastDay = 0
                this.chosenLastDate = ""
            }
        }
    }

    fun setLast(
        chosenLastPos: Int,
        chosenLastDay: Int,
        chosenLastDate: String
    ) {
        getSelection().run {
            this.chosenLastPos = chosenLastPos
            this.chosenLastDay = chosenLastDay
            this.chosenLastDate = chosenLastDate
        }
    }

    fun removeDaySelection(
        pos: Int,
        value: Int?
    ) { // Null safety kullanmana gerek yok. Çünkü zaten "-" ya da "" seçilemez
        value?.let {
            getSelection().run {
                if ((this.chosenFirstPos == pos) and (this.chosenFirstDay == it)) {
                    this.chosenFirstPos = this.chosenLastPos
                    this.chosenFirstDay = this.chosenLastDay
                    this.chosenFirstDate = this.chosenLastDate
                    this.chosenLastPos = 0
                    this.chosenLastDay = 0
                    this.chosenLastDate = ""
                } else if ((this.chosenLastPos == pos) and (this.chosenLastDay == it)) {
                    this.chosenLastPos = 0
                    this.chosenLastDay = 0
                    this.chosenLastDate = ""
                }

                getDayClick().decrementAndGet()
            }
        }
    }

    fun removeSelection() {
        getSelection().run {
            this.chosenFirstPos = 0
            this.chosenFirstDay = 0
            this.chosenFirstDate = ""
            this.chosenLastPos = 0
            this.chosenLastDay = 0
            this.chosenLastDate = ""
        }
    }

    fun getSelection(): DailyDateSelection = dailyDTPickerRepo.dailyDateSelection

    fun getAndCompareDates(context: Context, view: View, pickUpTime: String, dropOffTime: String) {
        getSelection().run {
            val customDialogRepo = CustomDialogRepo(context = context)

            if (chosenFirstDate.isEmpty()) {
                customDialogRepo.showWarningDialog(msg = context.getString(R.string.daily_date_range_error))
                return
            }

            if (chosenFirstDate.isNotEmpty() && chosenLastDate.isEmpty() && !compareTimes(pickUpTime = pickUpTime, dropOffTime = dropOffTime)) {
                customDialogRepo.showWarningDialog(msg = context.getString(R.string.daily_time_range_error))
                return
            }

            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm")
            val calendar = Calendar.getInstance()
            val calendar2 = Calendar.getInstance()

            if (chosenLastDate != "") {
                val tempSdf = SimpleDateFormat("dd MMMM yyyy") // saat ve dakikayı kaldır millis'i aktardıktan sonra ekle.
                val tempFirstDate = tempSdf.parse(chosenFirstDate)
                val tempLastDate = tempSdf.parse(chosenLastDate)

                if (tempFirstDate != null && tempLastDate != null) {
                    calendar.time = tempFirstDate
                    calendar2.time = tempLastDate

                    DateTimeTransfer.run {
                        //calendar = lastdate, calendar2 = firstdate condition
                        if (calendar.timeInMillis > calendar2.timeInMillis) {
                            calendar2.set(Calendar.HOUR_OF_DAY, pickUpTime.substring(0, 2).toInt())
                            calendar2.set(Calendar.MINUTE, pickUpTime.substring(3, 5).toInt())
                            calendar.set(Calendar.HOUR_OF_DAY, dropOffTime.substring(0, 2).toInt())
                            calendar.set(Calendar.MINUTE, dropOffTime.substring(3, 5).toInt())
                            dailyFirstMillis = calendar2.timeInMillis
                            dailyLastMillis = calendar.timeInMillis
                            dailyDateRange = "${sdf.format(calendar2.time)} - ${sdf.format(calendar.time)}"
                        }else {
                            calendar2.set(Calendar.HOUR_OF_DAY, dropOffTime.substring(0, 2).toInt())
                            calendar2.set(Calendar.MINUTE, dropOffTime.substring(3, 5).toInt())
                            calendar.set(Calendar.HOUR_OF_DAY, pickUpTime.substring(0, 2).toInt())
                            calendar.set(Calendar.MINUTE, pickUpTime.substring(3, 5).toInt())
                            dailyFirstMillis = calendar.timeInMillis
                            dailyLastMillis = calendar2.timeInMillis
                            dailyDateRange = "${sdf.format(calendar.time)} - ${sdf.format(calendar2.time)}"
                        }
                    }

                    toHomePage(view = view)
                }
            }else {
                chosenLastDate = "$chosenFirstDate $dropOffTime"
                chosenFirstDate += " $pickUpTime"

                val firstDate = sdf.parse(chosenFirstDate)
                val lastDate = sdf.parse(chosenLastDate)

                if (firstDate != null && lastDate != null) {
                    calendar.time = firstDate
                    calendar2.time = lastDate

                    DateTimeTransfer.run {
                        dailyFirstMillis = calendar.timeInMillis
                        dailyLastMillis = calendar2.timeInMillis
                        dailyDateRange = "${sdf.format(calendar.time)} - ${sdf.format(calendar2.time)}"
                    }

                    toHomePage(view = view)
                }
            }
        }
    }

    private fun compareTimes(pickUpTime: String, dropOffTime: String): Boolean {
        val sdf = SimpleDateFormat("HH:mm")
        var firstMillis = 0L
        var lastMillis = 0L
        val tempPickUpTime = sdf.parse(pickUpTime)
        val tempDropOffTime = sdf.parse(dropOffTime)

        if (tempPickUpTime != null && tempDropOffTime != null) {
            val calendar = Calendar.getInstance()
            calendar.time = tempPickUpTime
            firstMillis = calendar.timeInMillis
            calendar.time = tempDropOffTime
            lastMillis = calendar.timeInMillis
        }

        return (lastMillis - firstMillis) >= 1800000
    }

    private fun toHomePage(view: View) = Navigation.findNavController(view).navigate(R.id.dtPickerToHomePage)
}