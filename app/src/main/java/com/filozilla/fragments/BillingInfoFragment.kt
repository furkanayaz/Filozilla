package com.filozilla.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.filozilla.R
import com.filozilla.activities.MainActivity
import com.filozilla.adapters.BillingInfoDialogAdapter
import com.filozilla.adapters.BillingInfoDialogAdapter2
import com.filozilla.adapters.BillingInfoDialogAdapter3
import com.filozilla.databinding.FragmentBillingInfoBinding
import com.filozilla.interfaces.CommonInterface
import com.filozilla.models.User
import com.filozilla.repositories.CustomDialogRepo
import com.filozilla.viewmodels.BillingInfoFragmentVM
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson

@SuppressLint("InflateParams", "SetTextI18n")
class BillingInfoFragment : Fragment(), CommonInterface {
    private lateinit var binding: FragmentBillingInfoBinding
    private lateinit var viewModel: BillingInfoFragmentVM
    private lateinit var customDialogRepo: CustomDialogRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: BillingInfoFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBillingInfoBinding.inflate(inflater, container, false)
        binding.apply {
            fragmentBillingInfoObject = this@BillingInfoFragment
            country = ""
            city = ""
            county = ""
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
        touchListeners()
    }

    override fun initializeComponents() {
        initCustomDialog()
        getCountries()
        getCities()
        getCounties()
        observer()
    }

    private fun initCustomDialog() {
        customDialogRepo = CustomDialogRepo(context = requireContext())
        customDialogRepo.createLoadingDialog()
    }

    override fun touchListeners() {

    }

    private fun setOtherChanges() {
        MainActivity.user?.let { user ->
            binding.apply {
                binding.etBillingInfoPhoneNumber.setText(user.bPhoneNumber)
                binding.etBillingInfoAddress.setText(user.address)
                if (user.bPhoneNumber.isNotEmpty() and user.address.isNotEmpty()) {
                    country = user.country
                    city = user.city
                    county = user.county
                    user.postCode?.let { postCode ->
                        etBillingInfoPostCode.setText(postCode.toString())
                    }
                }
            }
        }?: showToastMsg(msg = getString(R.string.error_account))
    }

    private fun observer() {
        viewModel.getBillingController().observe(viewLifecycleOwner) { controller ->
            when(controller) {
                0, 2 -> {
                    customDialogRepo.getLoadingDialog()?.dismiss()
                    customDialogRepo.showWarningDialog(msg = getString(R.string.warning_dialog_desc_6))
                }
                1 -> {
                    binding.apply {
                        val phoneNumber = etBillingInfoPhoneNumber.text.toString().trim()
                        val address = etBillingInfoAddress.text.toString().trim()
                        val postCode = etBillingInfoPostCode.text.toString().trim().toInt()
                        updatePreference(phoneNumber = phoneNumber, country = country!!, city = city!!, county = county!!, address = address, postCode = postCode)
                    }

                }
            }
        }
        viewModel.getCountriesController().observe(viewLifecycleOwner) {
            when(it) {
                0, 1, 2 -> fetchedController()
            }
        }
        viewModel.getCitiesController().observe(viewLifecycleOwner) {
            when(it) {
                0, 1, 2 -> fetchedController()
            }
        }
        viewModel.getCountiesController().observe(viewLifecycleOwner) {
            when(it) {
                0, 1, 2 -> fetchedController()
            }
        }
    }

    private fun fetchedController() {
        if (viewModel.getFetchController() == 3) {
            customDialogRepo.getLoadingDialog()?.dismiss()
            setOtherChanges()
        }else if (viewModel.getFetchController() == -3) {
            customDialogRepo.getLoadingDialog()?.dismiss()
            showAlert(message = getString(R.string.billing_info_error))
        }
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(requireContext()).also {
            it.setCancelable(false)
            it.setMessage(message)
            it.setPositiveButton(getString(R.string.okay)) { _, _ -> toPage(id = R.id.billingInfoToOther) }
            it.create().show()
        }
    }

    private fun getCountries() = viewModel.getCountries()
    private fun getCities() = viewModel.getCities()
    private fun getCounties() = viewModel.getCounties()

    fun backOnClick() {

    }

    fun chooseCountryOnClick() = showCustomizableDialog(controller = 1)
    fun chooseCityOnClick() = showCustomizableDialog(controller = 2)
    fun chooseCountyOnClick() = showCustomizableDialog(controller = 3)
    private fun toPage(id: Int) = Navigation.findNavController(binding.root).navigate(id)

    private fun showCustomizableDialog(controller: Int) {
        var adapter: BillingInfoDialogAdapter? = null
        var adapter2: BillingInfoDialogAdapter2? = null
        var adapter3: BillingInfoDialogAdapter3? = null
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.customizable_billing_info_dialog, null, false)
        val title = view.findViewById(R.id.tvBillingInfoTopTitle) as TextView
        val close = view.findViewById(R.id.tvBillingInfoTopClose) as ImageView
        val rv = view.findViewById(R.id.rvCustomizableBillingInfo) as RecyclerView

        title.text = viewModel.getTitle(controller = controller)

        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false).also {
            rv.hasFixedSize()
            rv.layoutManager = it
        }

        when(controller) {
            0, 1 -> {
                adapter = BillingInfoDialogAdapter(context = requireContext(), list = viewModel.getCountryList()).also {
                    rv.adapter = it
                }
            }
            2 -> {
                adapter2 = BillingInfoDialogAdapter2(context = requireContext(), list = viewModel.getCityList()).also {
                    rv.adapter = it
                }
            }
            3 -> {
                adapter3 = BillingInfoDialogAdapter3(context = requireContext(), list = viewModel.getCountyList()).also {
                    rv.adapter = it
                }
            }
        }

        BottomSheetDialog(requireContext()).apply {
            setCancelable(false)
            setContentView(view = view)

            close.setOnClickListener {
                dismiss()
            }

            adapter?.clickedText?.observe(viewLifecycleOwner) {
                binding.apply {
                    country = it
                    dismiss()
                }
            }

            adapter2?.clickedText?.observe(viewLifecycleOwner) {
                binding.apply {
                    city = it
                    dismiss()
                }
            }

            adapter3?.clickedText?.observe(viewLifecycleOwner) {
                binding.apply {
                    county = it
                    dismiss()
                }
            }

            show()
        }
    }

    fun saveOnClick(phoneNumber: String, country: String, city: String, county: String, address: String, postCode: String) {
        if (phoneNumber.isNotEmpty() && country.isNotEmpty() && city.isNotEmpty() && county.isNotEmpty() && address.isNotEmpty() && postCode.isNotEmpty()) {
            MainActivity.user?.let { user ->
                customDialogRepo.createLoadingDialog()
                viewModel.updateBillingInfo(uid = user.bUid, phoneNumber = phoneNumber, country = country, city = city, county = county, address = address, postCode = postCode.toInt())
            }?: showToastMsg(msg = getString(R.string.error_account))
        }else {
            customDialogRepo.showWarningDialog(msg = getString(R.string.warning_dialog_desc))

            binding.apply {
                if (phoneNumber.isEmpty()) {
                    etBillingInfoPhoneNumber.error = getString(R.string.warning_phone_desc)
                }
                if (country.isEmpty()) {
                    etBillingInfoCountry.error = getString(R.string.warning_country_desc)
                }
                if (city.isEmpty()) {
                    etBillingInfoCity.error = getString(R.string.warning_city_desc)
                }
                if (county.isEmpty()) {
                    etBillingInfoCounty.error = getString(R.string.warning_county_desc)
                }
                if (address.isEmpty()) {
                    etBillingInfoAddress.error = getString(R.string.warning_address_desc)
                }
                if (postCode.isEmpty()) {
                    etBillingInfoPostCode.error = getString(R.string.warning_postcode_desc)
                }
            }
        }
    }

    private fun updatePreference(phoneNumber: String, country: String, city: String, county: String, address: String, postCode: Int) {
        var newUser = ""

        MainActivity.user?.let {
            it.bPhoneNumber = phoneNumber
            it.country = country
            it.city = city
            it.county = county
            it.address = address
            it.postCode = postCode
            newUser = Gson().toJson(it, User::class.java)
        }?: showToastMsg(msg = getString(R.string.error_account))

        requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit().run {
            remove("user")
            putString("user", newUser)
            apply()

            customDialogRepo.getLoadingDialog()?.dismiss()
            customDialogRepo.showSuccessDialog(controller = false, msg = getString(R.string.success_dialog_desc3))
        }
    }

    private fun showToastMsg(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

}