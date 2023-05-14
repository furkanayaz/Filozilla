package com.filozilla.models

data class DailyDateSelection(
    var chosenFirstPos: Int = 0,
    var chosenLastPos: Int = 0,
    var chosenFirstDay: Int = 0,
    var chosenLastDay: Int = 0,
    var chosenFirstDate: String = "",
    var chosenLastDate: String = ""
)