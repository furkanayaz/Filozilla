package com.filozilla.repositories

import androidx.lifecycle.MutableLiveData
import com.filozilla.models.City
import com.filozilla.models.Country
import com.filozilla.models.County
import com.filozilla.responses.CRUD
import com.filozilla.responses.CRUD3
import com.filozilla.responses.CRUD4
import com.filozilla.responses.CRUD5
import com.filozilla.retrofits.RetrofitUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BillingInfoRepo {
    private val countriesController = MutableLiveData(3)
    private val citiesController = MutableLiveData(3)
    private val countiesController = MutableLiveData(3)
    private val billingController = MutableLiveData(3)
    private var countryList = ArrayList<Country>()
    private var cityList = ArrayList<City>()
    private var countyList = ArrayList<County>()
    private var fetchController = 0

    fun getTitle(controller: Int): String = when(controller) {
        0 -> "Telefon Kodu Seçiniz"
        1 -> "Ülke Seçiniz"
        2 -> "İl Seçiniz"
        3 -> "İlçe Seçiniz"
        else -> ""
    }

    fun getCountries(): ArrayList<Country> {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitUtils.getRetrofitDaoInterface().inquiryCountries().enqueue(object : Callback<CRUD3> {
                override fun onResponse(call: Call<CRUD3>, response: Response<CRUD3>) {
                    response.body()?.let {
                        when(it.success) {
                            0 -> {
                                fetchController--
                                countriesController.value = 0
                            }
                            1 -> {
                                countryList = it.countries
                                fetchController++
                                countriesController.value = 1
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<CRUD3>, t: Throwable) {
                    fetchController--
                    countriesController.value = 2
                }

            })
        }

        return countryList
    }

    fun getCities(): ArrayList<City> {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitUtils.getRetrofitDaoInterface().inquiryCities().enqueue(object : Callback<CRUD4> {
                override fun onResponse(call: Call<CRUD4>, response: Response<CRUD4>) {
                    response.body()?.let {
                        when(it.success) {
                            0 -> {
                                fetchController--
                                citiesController.value = 0
                            }
                            1 -> {
                                cityList = it.cities
                                fetchController++
                                citiesController.value = 1
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<CRUD4>, t: Throwable) {
                    fetchController--
                    citiesController.value = 2
                }

            })
        }

        return cityList
    }

    fun getCounties(): ArrayList<County> {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitUtils.getRetrofitDaoInterface().inquiryCounties().enqueue(object : Callback<CRUD5> {
                override fun onResponse(call: Call<CRUD5>, response: Response<CRUD5>) {
                    response.body()?.let {
                        when(it.success) {
                            0 -> {
                                fetchController--
                                countiesController.value = 0
                            }
                            1 -> {
                                countyList = it.counties
                                fetchController++
                                countiesController.value = 1
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<CRUD5>, t: Throwable) {
                    fetchController--
                    countiesController.value = 2
                }

            })
        }

        return countyList
    }

    fun updateBillingInfo(uid: Int, phoneNumber: String, country: String, city: String, county: String, address: String, postCode: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitUtils.getRetrofitDaoInterface().updateBillingInfo(uid = uid, phoneNumber = phoneNumber, country = country, city = city, county = county, address = address, postCode = postCode).enqueue(object : Callback<CRUD> {
                override fun onResponse(call: Call<CRUD>, response: Response<CRUD>) {
                    response.body()?.let {
                        when(it.success) {
                            0 -> {
                                billingController.value = 0
                            }
                            1 -> {
                                billingController.value = 1
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<CRUD>, t: Throwable) {
                    billingController.value = 2
                }
            })
        }
    }

    fun getCountriesController(): MutableLiveData<Int> = countriesController
    fun getCitiesController(): MutableLiveData<Int> = citiesController
    fun getCountiesController(): MutableLiveData<Int> = countiesController
    fun getBillingController(): MutableLiveData<Int> = billingController
    fun getCountryList(): ArrayList<Country> = countryList
    fun getCityList(): ArrayList<City> = cityList
    fun getCountyList(): ArrayList<County> = countyList
    fun getFetchController(): Int = fetchController

}