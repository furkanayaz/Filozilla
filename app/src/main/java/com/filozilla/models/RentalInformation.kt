package com.filozilla.models

import java.io.*

data class RentalInformation(var tabController: Boolean, var dailyPickUp: String, var dailyDropOff: String, var dailyDate: String, var dailyPromotionCode: String, var monthlyPickUp: String, var monthlyDropOff: String, var monthlyDate: String, var monthlyPromotionCode: String)