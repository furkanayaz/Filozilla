package com.filozilla.fragments

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.filozilla.R
import com.filozilla.activities.MembershipActivity
import com.filozilla.databinding.FragmentOtherBinding
import com.filozilla.databinding.SearchCarComponentsDialogBinding
import com.filozilla.datatransfers.OtherTransfer
import com.filozilla.interfaces.CommonInterface
import com.filozilla.viewmodels.OtherFragmentVM
import com.google.android.material.bottomsheet.BottomSheetDialog

@SuppressLint("InflateParams", "UseCompatLoadingForDrawables", "ResourceAsColor")
class OtherFragment: Fragment(), CommonInterface {
    private lateinit var binding: FragmentOtherBinding
    private lateinit var viewModel: OtherFragmentVM
    private lateinit var sharedPreferences: SharedPreferences
    private var chosenLanguage = 0
    private var chosenCurrency = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: OtherFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOtherBinding.inflate(layoutInflater, container, false)
        binding.apply {
            fragmentOtherObject = this@OtherFragment
            accountDetailVisibility = false
            contactDetailVisibility = false
            contractDetailVisibility = false
            settingsDetailVisibility = false
            aboutAppDetailVisibility = false
            chosenLanguage = ""
            chosenCurrency = ""
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
        touchListeners()
    }

    override fun initializeComponents() {
        setOtherChanges()
        initSharedPref()
        getChanges()
    }

    override fun touchListeners() {

    }

    private fun setOtherChanges() {

    }

    private fun initSharedPref() {
        sharedPreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)!!
        getSettings()
    }

    private fun getChanges() {
        binding.apply {
            accountDetailVisibility = OtherTransfer.accoutDetailController
            contactDetailVisibility = OtherTransfer.contactDetailController
            contractDetailVisibility = OtherTransfer.contractDetailController
            settingsDetailVisibility = OtherTransfer.settingsDetailController
            aboutAppDetailVisibility = OtherTransfer.aboutAppDetailController

            animateView2(accountDetailVisibility!!, 0)
            animateView2(contactDetailVisibility!!, 1)
            animateView2(contractDetailVisibility!!, 2)
            animateView2(settingsDetailVisibility!!, 3)
            animateView2(aboutAppDetailVisibility!!, 4)
        }
    }

    fun myAccountOnClick() {
        binding.accountDetailVisibility = !binding.accountDetailVisibility!!
        OtherTransfer.accoutDetailController = binding.accountDetailVisibility!!
        animateView(binding.accountDetailVisibility!!, 0)
    }

    fun membershipInfoOnClick() {
        toPage(id = R.id.otherToMembershipInfo)
    }

    fun drivingLicenseInfoOnClick() {
        toPage(id = R.id.otherToDrivingInfo)
    }

    fun billingInfoOnClick() {
        toPage(id = R.id.otherToBillingInfo)
    }

    fun paymentMethodOnClick() {
        toPage(id = R.id.otherToPayment)
    }

    fun signOutOnClick() {
        sharedPreferences.edit().apply {
            clear()
            apply()
            toMembershipPage()
        }
    }

    fun contactOnClick() {
        binding.contactDetailVisibility = !binding.contactDetailVisibility!!
        OtherTransfer.contactDetailController = binding.contactDetailVisibility!!
        animateView(binding.contactDetailVisibility!!, 1)
    }

    fun callUsOnClick() {
        toIntentView(Intent.ACTION_DIAL, "tel:+905539759957")
    }

    fun requestFeatureOnClick() {
        toPage(id = R.id.otherToFeedBack)
    }

    fun contractOnClick() {
        binding.contractDetailVisibility = !binding.contractDetailVisibility!!
        OtherTransfer.contractDetailController = binding.contractDetailVisibility!!
        animateView(binding.contractDetailVisibility!!, 2)
    }

    fun privacyPolicyOnClick() {
        toContractPage(contractHead = getString(R.string.privacy_policy))
    }

    fun personalDataOnClick() {
        toContractPage(contractHead = getString(R.string.protection_personal_data))
    }

    fun termsOfUseOnClick() {
        toContractPage(contractHead = getString(R.string.terms_of_use))
    }

    fun settingsOnClick() {
        binding.settingsDetailVisibility = !binding.settingsDetailVisibility!!
        OtherTransfer.settingsDetailController = binding.settingsDetailVisibility!!
        animateView(binding.settingsDetailVisibility!!, 3)
    }

    fun languageOnClick() {
        showSettingsDialog(controller = 0)
    }

    fun currencyOnClick() {
        showSettingsDialog(controller = 1)
    }

    fun aboutAppOnClick() {
        binding.aboutAppDetailVisibility = !binding.aboutAppDetailVisibility!!
        OtherTransfer.aboutAppDetailController = binding.aboutAppDetailVisibility!!
        animateView(binding.aboutAppDetailVisibility!!, 4)
    }

    fun facebookOnClick() {
        toIntentView(Intent.ACTION_VIEW, "https://www.facebook.com/ucuzkitapalcom/")
    }

    fun twitterOnClick() {
        toIntentView(Intent.ACTION_VIEW, "https://www.twitter.com")
    }

    fun youtubeOnClick() {
        toIntentView(Intent.ACTION_VIEW, "https://www.youtube.com/@frknnyz")
    }

    fun linkedinOnClick() {
        toIntentView(Intent.ACTION_VIEW, "https://www.linkedin.com")
    }

    private fun toIntentView(action: String, link: String) {
        Intent(action, Uri.parse(link)).apply {
            requireActivity().startActivity(this)
        }
    }

    private fun animateView(controller: Boolean, rowIndex: Int) {
        var rowIcon: View? = null

        when(rowIndex) {
            0 -> rowIcon = binding.ivOtherMyAccountDetail
            1 -> rowIcon = binding.ivOtherContactDetail
            2 -> rowIcon = binding.ivOtherContractDetail
            3 -> rowIcon = binding.ivOtherSettingsDetail
            4 -> rowIcon = binding.ivOtherAboutDetail
        }

        rowIcon?.let {
            if (controller) {
                ObjectAnimator.ofFloat(it, "rotation", 0.0f, 45.0f).apply {
                    this.duration = 150
                    this.start()
                }
            }else {
                ObjectAnimator.ofFloat(it, "rotation", 45.0f, 0.0f).apply {
                    this.duration = 150
                    this.start()
                }
            }
        }
    }

    private fun animateView2(controller: Boolean, rowIndex: Int) {
        var rowIcon: View? = null

        when(rowIndex) {
            0 -> rowIcon = binding.ivOtherMyAccountDetail
            1 -> rowIcon = binding.ivOtherContactDetail
            2 -> rowIcon = binding.ivOtherContractDetail
            3 -> rowIcon = binding.ivOtherSettingsDetail
            4 -> rowIcon = binding.ivOtherAboutDetail
        }

        rowIcon?.let {
            if (controller) {
                rowIcon.rotation = 45.0f
            }else {
                rowIcon.rotation = 0.0f
            }
        }
    }

    private fun showSettingsDialog(controller: Int) {
        val dialogBinding = SearchCarComponentsDialogBinding.inflate(layoutInflater)
        val languageViews = listOf(dialogBinding.tvSearchCarTurkish, dialogBinding.tvSearchCarEnglish, dialogBinding.tvSearchCarDeutsch)
        val currencyViews = listOf(dialogBinding.tvSearchCarTry, dialogBinding.tvSearchCarUsd, dialogBinding.tvSearchCarEur, dialogBinding.tvSearchCarGbp)

        when(controller) {
            0 -> {
                dialogBinding.tvSearchCarComponentsTop.text = getString(R.string.language)
                dialogBinding.clSearchCarComponentsSortHolder.visibility = View.GONE
                dialogBinding.clSearchCarComponentsCurrencyHolder.visibility = View.GONE

                languageViews[chosenLanguage].run {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setBackgroundColor(requireContext().getColor(R.color.third_color))
                        setTextColor(requireContext().getColor(R.color.white))
                    }else {
                        setBackgroundColor(R.color.third_color)
                        setTextColor(R.color.white)
                    }
                }
            }
            1 -> {
                dialogBinding.tvSearchCarComponentsTop.text = getString(R.string.currency_3)
                dialogBinding.clSearchCarComponentsSortHolder.visibility = View.GONE
                dialogBinding.clSearchCarComponentsLanguageHolder.visibility = View.GONE

                currencyViews[chosenCurrency].run {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setBackgroundColor(requireContext().getColor(R.color.third_color))
                        setTextColor(requireContext().getColor(R.color.white))
                    }else {
                        setBackgroundColor(R.color.third_color)
                        setTextColor(R.color.white)
                    }
                }
            }
        }

        BottomSheetDialog(requireContext()).apply {
            setCancelable(true)
            setContentView(dialogBinding.root)

            dialogBinding.ivSearchCarComponentsTop.setOnClickListener {
                dismiss()
            }

            dialogBinding.tvSearchCarTry.setOnClickListener {
                chosenCurrency = 0
                saveSettings()
                dismiss()
            }

            dialogBinding.tvSearchCarUsd.setOnClickListener {
                chosenCurrency = 1
                saveSettings()
                dismiss()
            }

            dialogBinding.tvSearchCarEur.setOnClickListener {
                chosenCurrency = 2
                saveSettings()
                dismiss()
            }

            dialogBinding.tvSearchCarGbp.setOnClickListener {
                chosenCurrency = 3
                saveSettings()
                dismiss()
            }

            dialogBinding.tvSearchCarTurkish.setOnClickListener {
                chosenLanguage = 0
                saveSettings()
                dismiss()
            }

            dialogBinding.tvSearchCarEnglish.setOnClickListener {
                chosenLanguage = 1
                saveSettings()
                dismiss()
            }

            dialogBinding.tvSearchCarDeutsch.setOnClickListener {
                chosenLanguage = 2
                saveSettings()
                dismiss()
            }

            show()
        }
    }

    private fun getSettings() {
        chosenLanguage = sharedPreferences.getInt("language", 0)
        chosenCurrency = sharedPreferences.getInt("currency", 0)

        binding.chosenLanguage = " (Seçilen: ${viewModel.getLanguages()[chosenLanguage]})"
        binding.chosenCurrency = " (Seçilen: ${viewModel.getCurrencies()[chosenCurrency]})"
    }

    private fun saveSettings() {
        sharedPreferences.edit().apply {
            this.remove("currency")
            this.remove("language")
            this.putInt("currency", chosenCurrency)
            this.putInt("language", chosenLanguage)

            apply()
            initSharedPref()
        }
    }

    private fun toPage(id: Int) {
        Navigation.findNavController(binding.root).navigate(id)
    }

    private fun toContractPage(contractHead: String) {
        val id = OtherFragmentDirections.otherToContract(contractHead = contractHead)
        Navigation.findNavController(binding.root).navigate(id)
    }

    private fun toMembershipPage() {
        Intent(requireContext(), MembershipActivity::class.java).apply {
            requireActivity().startActivity(this)
            requireActivity().finish()
        }
    }

}