package com.filozilla.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.filozilla.R

@SuppressLint("ResourceAsColor")
class ViewChangesRepo(private val activity: FragmentActivity, private val context: Context) {

    fun changeStatusBarColor(color: Int) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.window?.statusBarColor = context.getColor(color)
            }else {
                activity.window?.statusBarColor = color
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun changeTextColor(color: Int): Int {
        var rtnColor = color

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rtnColor = context.getColor(color)
        }

        return rtnColor
    }

    fun changeBgColor(view: View, controller: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (controller) {
                view.setBackgroundColor(context.getColor(R.color.second_color))
            }else {
                view.setBackgroundColor(context.getColor(R.color.divider_color))
            }
        }else {
            if (controller)
                view.setBackgroundColor(R.color.second_color)
            else
                view.setBackgroundColor(R.color.divider_color)
        }
    }

}