package com.filozilla.viewmodels

import androidx.lifecycle.ViewModel
import com.filozilla.repositories.OtherRepo

class OtherFragmentVM: ViewModel() {
    private val repo = OtherRepo()

    fun getLanguages(): List<String> = repo.getLanguages()
    fun getCurrencies(): List<String> = repo.getCurrencies()
}