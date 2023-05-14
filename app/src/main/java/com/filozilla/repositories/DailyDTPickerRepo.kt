package com.filozilla.repositories

import com.filozilla.models.DailyDateSelection
import java.util.concurrent.atomic.AtomicInteger

class DailyDTPickerRepo {
    var onClickController = AtomicInteger(0)
    val dailyDateSelection = DailyDateSelection()
}