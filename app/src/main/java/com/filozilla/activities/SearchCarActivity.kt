package com.filozilla.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.filozilla.R
import com.filozilla.adapters.SearchCarAdapter
import com.filozilla.databinding.ActivitySearchCarBinding
import com.filozilla.databinding.SearchCarComponentsDialogBinding
import com.filozilla.interfaces.CommonInterface
import com.filozilla.models.RentalInformation
import com.filozilla.models.SearchCar
import com.filozilla.viewmodels.SearchCarActivityVM
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson

@SuppressLint("InflateParams", "ResourceAsColor")
class SearchCarActivity: AppCompatActivity(), CommonInterface {
    private lateinit var binding: ActivitySearchCarBinding
    private lateinit var viewModel: SearchCarActivityVM
    private var rentalInformation: RentalInformation? = null
    private var chosenSort = 0
    private var chosenCurrency = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchCarBinding.inflate(layoutInflater)
        binding.apply {
            activitySearchCarObject = this@SearchCarActivity
            totalCars = "(0 araç listelendi)"
        }
        val tempViewModel: SearchCarActivityVM by viewModels()
        this.viewModel = tempViewModel
        setContentView(binding.root)

        getInformations()
        initRv()

    }

    private fun getInformations() {
        Gson().fromJson(intent.getStringExtra("rentalInformation"), RentalInformation::class.java).also { this.rentalInformation = it }
    }

    private fun initRv() {
        binding.apply {
            rvSearchCar.hasFixedSize()
            val layoutManager = LinearLayoutManager(this@SearchCarActivity, LinearLayoutManager.VERTICAL, false)
            rvSearchCar.layoutManager = layoutManager
        }
        setItems()
    }

    private fun setItems() {
        val searchCarList = ArrayList<SearchCar>().also {
            it.add(SearchCar(carImage = listOf("https://cdn-07.imagin.studio/getImage?&customer=trfilozilla&make=abarth&modelFamily=124-spider&modelRange=124-spider&modelVariant=ca&modelYear=2018&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=imagin-grey&angle=22", "https://cdn-07.imagin.studio/getImage?&customer=trfilozilla&make=abarth&modelFamily=124-spider&modelRange=124-spider&modelVariant=ca&modelYear=2018&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=imagin-grey&angle=25", "https://cdn-07.imagin.studio/getImage?&customer=trfilozilla&make=abarth&modelFamily=124-spider&modelRange=124-spider&modelVariant=ca&modelYear=2018&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=imagin-grey&angle=53", "https://cdn-07.imagin.studio/getImage?&customer=trfilozilla&make=abarth&modelFamily=124-spider&modelRange=124-spider&modelVariant=ca&modelYear=2018&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=imagin-grey&angle=21", "https://cdn-07.imagin.studio/getImage?&customer=trfilozilla&make=abarth&modelFamily=124-spider&modelRange=124-spider&modelVariant=ca&modelYear=2018&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=imagin-grey&angle=29", "https://cdn-07.imagin.studio/getImage?&customer=trfilozilla&make=abarth&modelFamily=124-spider&modelRange=124-spider&modelVariant=ca&modelYear=2018&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=imagin-grey&angle=17", "https://cdn-07.imagin.studio/getImage?&customer=trfilozilla&make=abarth&modelFamily=124-spider&modelRange=124-spider&modelVariant=ca&modelYear=2018&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=imagin-grey&angle=13"), seat = 2, gear = "Otomatik", fuel = "Benzin", carType = "Ekonomik", carName = "Abarth Spider", deliveryType = "Terminal İçi Buluşma", deposit = 1.500f, km = 3.500f, rating = 4f, comments = 1505, chosenDay = 10, dailyPrice = 380.0f, chosenPrice = 4200f))
            it.add(SearchCar(carImage = listOf("https://cdn-01.imagin.studio/getImage?&customer=trfilozilla&make=tesla&modelFamily=roadster&modelRange=roadster&modelVariant=ta&modelYear=2021&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=imagin-grey&angle=01", "https://cdn-02.imagin.studio/getImage?&customer=trfilozilla&make=tesla&modelFamily=roadster&modelRange=roadster&modelVariant=ta&modelYear=2021&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=imagin-grey&angle=05", "https://cdn-03.imagin.studio/getImage?&customer=trfilozilla&make=tesla&modelFamily=roadster&modelRange=roadster&modelVariant=ta&modelYear=2021&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=imagin-grey&angle=09", "https://cdn-04.imagin.studio/getImage?&customer=trfilozilla&make=tesla&modelFamily=roadster&modelRange=roadster&modelVariant=ta&modelYear=2021&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=imagin-grey&angle=13", "https://cdn-09.imagin.studio/getImage?&customer=trfilozilla&make=tesla&modelFamily=roadster&modelRange=roadster&modelVariant=ta&modelYear=2021&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=pspc0034&angle=28", "https://cdn-09.imagin.studio/getImage?&customer=trfilozilla&make=tesla&modelFamily=roadster&modelRange=roadster&modelVariant=ta&modelYear=2021&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=pspc0034&angle=27"), seat = 2, gear = "Otomatik", fuel = "Elektrik", carType = "Ekonomik", carName = "Tesla Roadster", deliveryType = "Terminal İçi Buluşma", deposit = 1.500f, km = 3.500f, rating = 4f, comments = 1505, chosenDay = 10, dailyPrice = 380.0f, chosenPrice = 5200f))
            it.add(SearchCar(carImage = listOf("https://cdn-08.imagin.studio/getImage?&customer=trfilozilla&make=bmw&modelFamily=i8&modelRange=i8&modelVariant=co&modelYear=2018&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=pspc0034&angle=23", "https://cdn-01.imagin.studio/getImage?&customer=trfilozilla&make=bmw&modelFamily=i8&modelRange=i8&modelVariant=co&modelYear=2018&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=pspc0034&angle=01", "https://cdn-02.imagin.studio/getImage?&customer=trfilozilla&make=bmw&modelFamily=i8&modelRange=i8&modelVariant=co&modelYear=2018&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=pspc0034&angle=05", "https://cdn-04.imagin.studio/getImage?&customer=trfilozilla&make=bmw&modelFamily=i8&modelRange=i8&modelVariant=co&modelYear=2018&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=pspc0034&angle=13", "https://cdn-03.imagin.studio/getImage?&customer=trfilozilla&make=bmw&modelFamily=i8&modelRange=i8&modelVariant=co&modelYear=2018&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=pspc0034&angle=09", "https://cdn-07.imagin.studio/getImage?&customer=trfilozilla&make=bmw&modelFamily=i8&modelRange=i8&modelVariant=co&modelYear=2018&powerTrain=fossil&transmission=0&bodySize=2&trim=0&paintId=pspc0034&angle=22"), seat = 4, gear = "Otomatik", fuel = "Benzin", carType = "Ekonomik", carName = "BMW i8 Series", deliveryType = "Terminal İçi Buluşma", deposit = 1.500f, km = 3.500f, rating = 4f, comments = 1505, chosenDay = 10, dailyPrice = 380.0f, chosenPrice = 2200f))
            it.add(SearchCar(carImage = listOf("https://cdn-08.imagin.studio/getImage?&customer=trfilozilla&make=audi&modelFamily=a7&modelRange=a7&modelVariant=ha&modelYear=2019&powerTrain=fossil&transmission=0&bodySize=5&trim=0&paintId=pspc0034&angle=23", "https://cdn-01.imagin.studio/getImage?&customer=trfilozilla&make=audi&modelFamily=a7&modelRange=a7&modelVariant=ha&modelYear=2019&powerTrain=fossil&transmission=0&bodySize=5&trim=0&paintId=pspc0034&angle=01", "https://cdn-02.imagin.studio/getImage?&customer=trfilozilla&make=audi&modelFamily=a7&modelRange=a7&modelVariant=ha&modelYear=2019&powerTrain=fossil&transmission=0&bodySize=5&trim=0&paintId=pspc0034&angle=05", "https://cdn-03.imagin.studio/getImage?&customer=trfilozilla&make=audi&modelFamily=a7&modelRange=a7&modelVariant=ha&modelYear=2019&powerTrain=fossil&transmission=0&bodySize=5&trim=0&paintId=pspc0034&angle=09", "https://cdn-04.imagin.studio/getImage?&customer=trfilozilla&make=audi&modelFamily=a7&modelRange=a7&modelVariant=ha&modelYear=2019&powerTrain=fossil&transmission=0&bodySize=5&trim=0&paintId=pspc0034&angle=13", "https://cdn-07.imagin.studio/getImage?&customer=trfilozilla&make=audi&modelFamily=a7&modelRange=a7&modelVariant=ha&modelYear=2019&powerTrain=fossil&transmission=0&bodySize=5&trim=0&paintId=pspc0034&angle=22", "https://cdn-06.imagin.studio/getImage?&customer=trfilozilla&make=audi&modelFamily=a7&modelRange=a7&modelVariant=ha&modelYear=2019&powerTrain=fossil&transmission=0&bodySize=5&trim=0&paintId=pspc0034&angle=21"), seat = 2, gear = "Otomatik", fuel = "Benzin", carType = "Ekonomik", carName = "Audi A7 Series", deliveryType = "Terminal İçi Buluşma", deposit = 1.500f, km = 3.500f, rating = 4f, comments = 1505, chosenDay = 10, dailyPrice = 380.0f, chosenPrice = 4000f))
        }

        binding.totalCars = "(${searchCarList.size} araç listelendi)"

        initAdapter(searchCarList = searchCarList)
    }

    private fun initAdapter(searchCarList: List<SearchCar>) {

        with(
            SearchCarAdapter(
                context = this@SearchCarActivity,
                searchCarList = searchCarList
            )
        ) {
            binding.rvSearchCar.adapter = this@with
        }
    }

    override fun initializeComponents() {

    }

    override fun touchListeners() {

    }

    fun backOnClick() {
        Intent(this, MainActivity::class.java).run {
            startActivity(this)
            finish()
        }
    }

    fun filterOnClick() {

    }

    fun sortOnClick() {
        showComponentsDialog(component = "sort_holder")
    }

    fun currencyOnClick() {
        showComponentsDialog(component = "currency_holder")
    }

    private fun showComponentsDialog(component: String) {
        val dialogBinding = SearchCarComponentsDialogBinding.inflate(layoutInflater)

        BottomSheetDialog(this).apply {
            val sortViews = listOf(dialogBinding.tvSearchCarRecommended, dialogBinding.tvSearchCarLowestPrice, dialogBinding.tvSearchCarHighestPrice, dialogBinding.tvSearchCarHighestRate, dialogBinding.tvSearchCarMostRated)
            val currencyViews = listOf(dialogBinding.tvSearchCarTry, dialogBinding.tvSearchCarUsd, dialogBinding.tvSearchCarEur, dialogBinding.tvSearchCarGbp)

            if (component == "sort_holder") {
                dialogBinding.tvSearchCarComponentsTop.text = getString(R.string.sort)
                dialogBinding.clSearchCarComponentsCurrencyHolder.visibility = View.GONE
                dialogBinding.clSearchCarComponentsSortHolder.visibility = View.VISIBLE

                sortViews[chosenSort].run {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setBackgroundColor(getColor(R.color.third_color))
                        setTextColor(getColor(R.color.white))
                    }else {
                        setBackgroundColor(R.color.third_color)
                        setTextColor(R.color.white)
                    }
                }
            }else if (component == "currency_holder") {
                dialogBinding.tvSearchCarComponentsTop.text = getString(R.string.currency_2)
                dialogBinding.clSearchCarComponentsCurrencyHolder.visibility = View.VISIBLE
                dialogBinding.clSearchCarComponentsSortHolder.visibility = View.GONE

                currencyViews[chosenCurrency].run {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setBackgroundColor(getColor(R.color.third_color))
                        setTextColor(getColor(R.color.white))
                    }else {
                        setBackgroundColor(R.color.third_color)
                        setTextColor(R.color.white)
                    }
                }
            }

            dialogBinding.ivSearchCarComponentsTop.setOnClickListener {
                dismiss()
            }

            dialogBinding.tvSearchCarRecommended.setOnClickListener {
                chosenSort = 0
                dismiss()
            }

            dialogBinding.tvSearchCarLowestPrice.setOnClickListener {
                chosenSort = 1
                dismiss()
            }

            dialogBinding.tvSearchCarHighestPrice.setOnClickListener {
                chosenSort = 2
                dismiss()
            }

            dialogBinding.tvSearchCarHighestRate.setOnClickListener {
                chosenSort = 3
                dismiss()
            }

            dialogBinding.tvSearchCarMostRated.setOnClickListener {
                chosenSort = 4
                dismiss()
            }

            dialogBinding.tvSearchCarTry.setOnClickListener {
                chosenCurrency = 0
                dismiss()
            }

            dialogBinding.tvSearchCarUsd.setOnClickListener {
                chosenCurrency = 1
                dismiss()
            }

            dialogBinding.tvSearchCarEur.setOnClickListener {
                chosenCurrency = 2
                dismiss()
            }

            dialogBinding.tvSearchCarGbp.setOnClickListener {
                chosenCurrency = 3
                dismiss()
            }

            setCancelable(true)
            setContentView(dialogBinding.root)
            show()
        }
    }

}