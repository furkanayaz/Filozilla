package com.filozilla.viewmodels

import androidx.lifecycle.ViewModel

class ContractFragmentVM: ViewModel() {

    fun getPage(head: String): String {
        return when(head) {
            "Gizlilik Politikası" -> "privacy_policy.html"
            "Kişisel Verilerin Korunması" -> "protection_of_personal_data.html"
            "Kullanım Koşulları" -> "terms_of_use.html"
            else -> ""
        }
    }

}