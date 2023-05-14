package com.filozilla.viewmodels

import androidx.lifecycle.ViewModel
import com.filozilla.models.City
import com.filozilla.models.Country
import com.filozilla.models.County
import com.filozilla.repositories.BillingInfoRepo

class BillingInfoFragmentVM : ViewModel() {
    private val billingInfoRepo = BillingInfoRepo()

    fun getTitle(controller: Int): String = billingInfoRepo.getTitle(controller = controller)
    fun getCountries(): ArrayList<Country> = billingInfoRepo.getCountries()
    fun getCities(): ArrayList<City> = billingInfoRepo.getCities()
    fun getCounties(): ArrayList<County> = billingInfoRepo.getCounties()
    fun getBillingController() = billingInfoRepo.getBillingController()
    fun getCountriesController() = billingInfoRepo.getCountriesController()
    fun getCitiesController() = billingInfoRepo.getCitiesController()
    fun getCountiesController() = billingInfoRepo.getCountiesController()
    fun getCountryList(): ArrayList<Country> = billingInfoRepo.getCountryList()
    fun getCityList(): ArrayList<City> = billingInfoRepo.getCityList()
    fun getCountyList(): ArrayList<County> = billingInfoRepo.getCountyList()
    fun getFetchController(): Int = billingInfoRepo.getFetchController()
    fun updateBillingInfo(uid: Int, phoneNumber: String, country: String, city: String, county: String, address: String, postCode: Int) = billingInfoRepo.updateBillingInfo(uid = uid, phoneNumber = phoneNumber, country = country, city = city, county = county, address = address, postCode = postCode)
}