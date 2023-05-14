package com.filozilla.rooms

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.filozilla.interfaces.PaymentDaoInterface
import com.filozilla.models.Payment

@Database(entities = [Payment::class], version = 1, exportSchema = false)
abstract class RoomDB: RoomDatabase() {
    abstract fun getPaymentDao(): PaymentDaoInterface

    companion object {
        var INSTANCE: RoomDB? = null

        fun accessDatabase(context: Context): RoomDB? {
            if (INSTANCE == null) {
                synchronized(RoomDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        context = context.applicationContext,
                        klass = RoomDB::class.java,
                        name = "payment_method.sqlite"
                    ).createFromAsset(databaseFilePath = "payment_method.sqlite").build()
                }
            }

            return INSTANCE
        }
    }
}