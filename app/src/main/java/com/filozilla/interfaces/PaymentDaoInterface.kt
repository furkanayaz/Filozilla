package com.filozilla.interfaces

import androidx.room.*
import com.filozilla.models.Payment

@Dao
interface PaymentDaoInterface {
    @Query("SELECT * FROM payment WHERE i_id=:i_id")
    suspend fun getPaymentMethods(i_id: Int): List<Payment>

    @Insert
    suspend fun insertPaymentMethod(payment: Payment)

    @Update
    suspend fun updatePaymentMethod(payment: Payment)

    @Delete
    suspend fun deletePaymentMethod(payment: Payment)

    @Query("DELETE FROM payment WHERE i_id=:i_id")
    suspend fun deletePaymentMethods(i_id: Int)
}