package com.filozilla.repositories

import android.annotation.SuppressLint
import android.content.Context
import com.filozilla.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("SimpleDateFormat")
class DailyDtRepo(context: Context) {
    private var dayOfToday = 0
    private var month = 0
    private var year = 0
    private val firstDayList = ArrayList<Int>()
    private val endDayList = ArrayList<Int>()
    private val monthList = ArrayList<Int>()
    private val yearList = ArrayList<Int>()

    init {
        getDate()
        getFirstDay(context = context)
        getEndDay()
        getMonth()
        getYear()
    }

    private fun getDate() {
        Calendar.getInstance().run {
            dayOfToday = this.get(Calendar.DAY_OF_MONTH)
            month = this.get(Calendar.MONTH)
            year = this.get(Calendar.YEAR)
        }
    }

    private fun getFirstDay(context: Context): List<Int> {
        val sdf = SimpleDateFormat("E") // "u" direkt günlerin index'ni veriyor ancak 24. Seviye api istiyor.
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        var index = 0

        while (index <= 12) {
            firstDayList.add(
                when(sdf.format(calendar.time).uppercase()) {
                    context.getString(R.string.monday) -> 0
                    context.getString(R.string.tuesday) -> 1
                    context.getString(R.string.wednesday) -> 2
                    context.getString(R.string.thursday) -> 3
                    context.getString(R.string.friday) -> 4
                    context.getString(R.string.saturday) -> 5
                    context.getString(R.string.sunday) -> 6
                    else -> 0
                }
            )

            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1)
            index++
        }

        return firstDayList
    }

    private fun getEndDay(): List<Int> {
        val calendar = Calendar.getInstance()

        var index = 0

        while (index <= 12) {
            endDayList.add(calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1)
            index++
        }

        return endDayList
    }

    private fun getMonth(): List<Int> {
        for (pos in month .. month + 12) {
            if (pos < 12)
                monthList.add(pos)
            else
                monthList.add(pos - 12)
        }

        return monthList
    }

    private fun getYear(): List<Int> {
        val calendar = Calendar.getInstance()

        var index = 0

        while (index <= 12) {
            yearList.add(calendar.get(Calendar.YEAR))
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1)
            index++
        }

        return yearList
    }

    fun getDayOfToday(): Int = dayOfToday
    fun getFirstDayList() = firstDayList
    fun getEndDayList() = endDayList
    fun getMonthList() = monthList
    fun getYearList() = yearList
}