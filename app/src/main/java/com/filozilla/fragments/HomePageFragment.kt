package com.filozilla.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.airbnb.lottie.LottieAnimationView
import com.filozilla.R
import com.filozilla.activities.SearchCarActivity
import com.filozilla.databinding.FragmentHomePageBinding
import com.filozilla.datatransfers.DateTimeTransfer
import com.filozilla.datatransfers.PickUpDropOffTransfer
import com.filozilla.datatransfers.HomePageTransfer
import com.filozilla.interfaces.CommonInterface
import com.filozilla.models.RentalInformation
import com.filozilla.viewmodels.HomePageFragmentVM
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("InflateParams", "MissingInflatedId", "ClickableViewAccessibility")
class HomePageFragment: Fragment(), CommonInterface, TextWatcher {
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var viewModel: HomePageFragmentVM
    private var formController = true
    private var promotionController2 = false
    private var promotionController3 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: HomePageFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomePageBinding.inflate(layoutInflater, container, false)
        binding.fragmentHomePageObject = this
        binding.differentPlaceController = false
        binding.differentPlaceController2 = false
        binding.tabController = false
        binding.dividerController = false
        binding.dividerController2 = false
        binding.dividerController3 = false
        binding.dividerController4 = false
        binding.dividerController5 = false
        binding.dividerController6 = false
        binding.dividerController7 = false
        binding.dividerController8 = false
        binding.dailyPickUpText = "Alış ve Bırakış Yeri"
        binding.monthlyPickUpText = "Alış ve Bırakış Yeri"
        binding.dailyPickUpDrofOffDate = ""
        binding.monthlyPickUpDrofOffDate = ""
        binding.promotionController = false
        binding.promotionController2 = false
        binding.promotionCode = ""
        binding.promotionCode2 = ""
        binding.findCarVisibility = false

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
        keyListeners()
        touchListeners()
    }

    override fun initializeComponents() {
        getWhichTab()
        setOtherChanges()
        getOtherChanges()
        disableLongListeners()
        observer()
        setHint()
        changeListeners()
        focusChangeListeners()
    }

    private fun getWhichTab() {
        binding.tabController = HomePageTransfer.whichTab
        if (HomePageTransfer.whichTab) {
            viewModel.tabMonthlyOnClick()
            binding.linearLayoutDailyTabHolder.visibility = View.GONE
            binding.linearLayoutMonthlyTabHolder.visibility = View.VISIBLE
        }else {
            viewModel.tabDailyOnClick()
            binding.linearLayoutDailyTabHolder.visibility = View.VISIBLE
            binding.linearLayoutMonthlyTabHolder.visibility = View.GONE
        }
    }

    private fun setOtherChanges() {

    }

    private fun getOtherChanges() {
        binding.differentPlaceController = HomePageTransfer.dailyDifferentPlace
        binding.switchDailyDifferentPlace.isChecked = HomePageTransfer.dailyDifferentPlace
        binding.differentPlaceController2 = HomePageTransfer.monthlyDifferentPlace
        binding.switchMonthlyDifferentPlace.isChecked = HomePageTransfer.monthlyDifferentPlace
        binding.switchDailyAgeRange.isChecked = HomePageTransfer.dailyAgeRange
        binding.switchMonthlyAgeRange.isChecked = HomePageTransfer.monthlyAgeRange
        binding.promotionController = HomePageTransfer.dailyPromotionCode
        binding.switchDailyPromotion.isChecked = HomePageTransfer.dailyPromotionCode
        binding.promotionController2 = HomePageTransfer.monthlyPromotionCode
        binding.switchMonthlyPromotion.isChecked = HomePageTransfer.monthlyPromotionCode

        binding.dividerController = PickUpDropOffTransfer.location.isNotEmpty()
        binding.dividerController2 = PickUpDropOffTransfer.location2.isNotEmpty()
        binding.dividerController3 = DateTimeTransfer.dailyDateRange.isNotEmpty()
        binding.dividerController5 = PickUpDropOffTransfer.location3.isNotEmpty()
        binding.dividerController6 = PickUpDropOffTransfer.location4.isNotEmpty()
        binding.dividerController7 = DateTimeTransfer.monthlyDate.isNotEmpty()
        binding.dividerController8 = HomePageTransfer.monthlyPromotionCode

        binding.etDailyPromotion.addTextChangedListener(this)
        binding.etMonthlyPromotion.addTextChangedListener(this)

        if (PickUpDropOffTransfer.location.isNotEmpty()) {
            binding.etDailyPickUp.setText(PickUpDropOffTransfer.location)
        }
        if (PickUpDropOffTransfer.location2.isNotEmpty()) {
            binding.etDailyDropOff.setText(PickUpDropOffTransfer.location2)
        }
        if (PickUpDropOffTransfer.location3.isNotEmpty()) {
            binding.etMonthlyPickUp.setText(PickUpDropOffTransfer.location3)
        }
        if (PickUpDropOffTransfer.location4.isNotEmpty()) {
            binding.etMonthlyDropOff.setText(PickUpDropOffTransfer.location4)
        }
        if (DateTimeTransfer.dailyDateRange.isNotEmpty()) {
            binding.dailyPickUpDrofOffDate = DateTimeTransfer.dailyDateRange
        }
        if (DateTimeTransfer.monthlyDate.isNotEmpty()) {
            binding.monthlyPickUpDrofOffDate = DateTimeTransfer.monthlyDate
        }
    }

    private fun disableLongListeners() {
        binding.etDailyPickUp.isLongClickable = false
        binding.etDailyPickUp.setTextIsSelectable(false)
        binding.etDailyDropOff.isLongClickable = false
        binding.etDailyDropOff.setTextIsSelectable(false)
        binding.etDailyPickUpDropOffDate.isLongClickable = false
        binding.etDailyPickUpDropOffDate.setTextIsSelectable(false)
    }

    private fun observer() {
        viewModel.getDailyEnableListener().observe(requireActivity()) {
            binding.tvHomePageTabDaily.text = it

        }
        viewModel.getMonthlyEnableListener().observe(requireActivity()) {
            binding.tvHomePageTabMonthly.text = it
        }
    }

    private fun keyListeners() {
        binding.etDailyPromotion.setOnKeyListener { _, keyCode, _ ->
            promotionController2 = keyCode == KeyEvent.KEYCODE_DEL
            false
        }
        binding.etMonthlyPromotion.setOnKeyListener { _, keyCode, _ ->
            promotionController3 = keyCode == KeyEvent.KEYCODE_DEL
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun touchListeners() {
        binding.cvHomePageFindCar.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    animationController(view)
                }
                MotionEvent.ACTION_UP -> {
                    animationController2(view)
                }
                MotionEvent.ACTION_CANCEL -> {
                    animationController2(view)
                }
            }

            false
        }
    }

    private fun animationController(view: View) {
        viewModel.viewAnimation(view)
    }

    private fun animationController2(view: View) {
        viewModel.viewAnimation2(view)
    }

    @SuppressLint("SimpleDateFormat")
    fun setHint() {
        val sdf = SimpleDateFormat("dd MMM HH:mm")
        val calendar = Calendar.getInstance()
        val date = sdf.format(calendar.time)
        calendar.timeInMillis = calendar.timeInMillis + (259200000)
        val date2 = sdf.format(calendar.time)
        binding.etDailyPickUpDropOffDate.hint = "$date - $date2"
        binding.etMonthlyPickUpDropOffDate.hint = "$date - 1 Ay"
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun changeListeners() {
        binding.switchDailyDifferentPlace.setOnCheckedChangeListener { _, b ->
            binding.differentPlaceController = b
            HomePageTransfer.dailyDifferentPlace = b

            if (b)
                binding.dailyPickUpText = "Alış Yeri"
            else
                binding.dailyPickUpText = "Alış ve Bırakış Yeri"
        }
        binding.switchDailyPromotion.setOnCheckedChangeListener { _, b ->
            binding.promotionController = b
            HomePageTransfer.dailyPromotionCode = b
        }
        binding.switchDailyAgeRange.setOnCheckedChangeListener { _, b ->
            HomePageTransfer.dailyAgeRange = b
        }
        binding.switchMonthlyDifferentPlace.setOnCheckedChangeListener { _, b ->
            binding.differentPlaceController2 = b
            HomePageTransfer.monthlyDifferentPlace = b

            if (b)
                binding.monthlyPickUpText = "Alış Yeri"
            else
                binding.monthlyPickUpText = "Alış ve Bırakış Yeri"
        }
        binding.switchMonthlyPromotion.setOnCheckedChangeListener { _, b ->
            binding.promotionController2 = b
            HomePageTransfer.monthlyPromotionCode = b
        }
        binding.switchMonthlyAgeRange.setOnCheckedChangeListener { _, b ->
            HomePageTransfer.monthlyAgeRange = b
        }
    }

    private fun focusChangeListeners() {
        binding.apply {
            etDailyPickUp.setOnFocusChangeListener { _, b ->
                dividerController = etDailyPickUp.text.toString().trim().isNotEmpty() || b
            }
            etDailyDropOff.setOnFocusChangeListener { _, b ->
                dividerController2 = etDailyDropOff.text.toString().trim().isNotEmpty() || b
            }
            etDailyPickUpDropOffDate.setOnFocusChangeListener { _, b ->
                dividerController3 = etDailyPickUpDropOffDate.text.toString().trim().isNotEmpty() || b
            }
            etDailyPromotion.setOnFocusChangeListener { _, b ->
                dividerController4 = etDailyPromotion.text.toString().trim().isNotEmpty() || b
            }
            etMonthlyPickUp.setOnFocusChangeListener { _, b ->
                dividerController5 = etMonthlyPickUp.text.toString().trim().isNotEmpty() || b
            }
            etMonthlyDropOff.setOnFocusChangeListener { _, b ->
                dividerController6 = etMonthlyDropOff.text.toString().trim().isNotEmpty() || b
            }
            etMonthlyPickUpDropOffDate.setOnFocusChangeListener { _, b ->
                dividerController7 = etMonthlyPickUpDropOffDate.text.toString().trim().isNotEmpty() || b
            }
            etMonthlyPromotion.setOnFocusChangeListener { _, b ->
                dividerController8 = etMonthlyPromotion.text.toString().trim().isNotEmpty() || b
            }
        }
    }

    fun tabDailyOnClick() {
        if (binding.tabController!! && formController) {
            binding.tabController = false
            viewModel.tabDailyOnClick()
            setFormAnimation()
        }
    }

    fun tabMonthlyOnClick() {
        if (!binding.tabController!! && formController) {
            binding.tabController = true
            viewModel.tabMonthlyOnClick()
            setFormAnimation2()
        }
    }

    fun dailyPickUpOnClick() {
        PickUpDropOffTransfer.controller = 1
        dailyNavigationOnClick(controller = 0)
    }

    fun dailyDropOffOnClick() {
        PickUpDropOffTransfer.controller = 2
        dailyNavigationOnClick(controller = 0)
    }

    fun monthlyPickUpOnClick() {
        PickUpDropOffTransfer.controller = 3
        monthlyNavigationOnClick(controller = 0)
    }

    fun monthlyDropOffOnClick() {
        PickUpDropOffTransfer.controller = 4
        monthlyNavigationOnClick(controller = 0)
    }

    fun dailyPickUpDropOffDate() {
        dailyNavigationOnClick(controller = 1)
    }

    fun monthlyPickUpDropOffDate() {
        monthlyNavigationOnClick(controller = 1)
    }

    fun findCarOnClick(dailyPickUp: String, dailyDropOff: String, dailyDate: String, dailyPromotionCode: String, monthlyPickUp: String, monthlyDropOff: String, monthlyDate: String, monthlyPromotionCode: String) {
        val rentalInformation = RentalInformation(tabController = binding.tabController!!, dailyPickUp = dailyPickUp, dailyDropOff = dailyDropOff, dailyDate = dailyDate, dailyPromotionCode = dailyPromotionCode, monthlyPickUp = monthlyPickUp, monthlyDropOff = monthlyDropOff, monthlyDate = monthlyDate, monthlyPromotionCode = monthlyPromotionCode)
        val strRentalInformation = Gson().toJson(rentalInformation)

        binding.apply {
            if ((rentalInformation.dailyPickUp.isNotEmpty() && rentalInformation.dailyDate.isNotEmpty()) || (rentalInformation.monthlyPickUp.isNotEmpty() && rentalInformation.monthlyDate.isNotEmpty())) {
                Intent(requireContext(), SearchCarActivity::class.java).run {
                    putExtra("rentalInformation", strRentalInformation)
                    startActivity(this)
                    requireActivity().finish()
                }
            }else {
                if (binding.tabController!!) {
                    if (rentalInformation.monthlyPickUp.isEmpty()) {
                        binding.etMonthlyPickUp.error = "Alış ve bırakış yerini seçiniz."
                    }
                    if (rentalInformation.monthlyDate.isEmpty()) {
                        binding.etMonthlyPickUpDropOffDate.error = "Alış ve bırakış tarihini seçiniz."
                    }
                }else {
                    if (rentalInformation.dailyPickUp.isEmpty()) {
                        binding.etDailyPickUp.error = "Alış ve bırakış yerini seçiniz."
                    }
                    if (rentalInformation.dailyDate.isEmpty()) {
                        binding.etDailyPickUpDropOffDate.error = "Alış ve bırakış tarihini seçiniz."
                    }
                }

                showWarningDialog()
            }
        }
    }

    private fun showWarningDialog() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.customizable_dialog, null, false)

        Dialog(requireContext()).apply {
            setCancelable(true)
            setContentView(view)
            size()
            window?.apply {
                setGravity(Gravity.CENTER and Gravity.CENTER_HORIZONTAL and Gravity.CENTER_VERTICAL)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            val lottie = view.findViewById(R.id.lottieCustomizableDialog) as LottieAnimationView
            val tvCustomizable = view.findViewById(R.id.tvCustomizableDialogDesc) as TextView

            tvCustomizable.text = if (binding.tabController!!) {
                getString(R.string.monthly_warning_dialog_desc)
            }else {
                getString(R.string.daily_warning_dialog_desc)
            }

            lottie.setAnimation(R.raw.warning)

            val actionOk = view.findViewById(R.id.
            btnCustomizableDialogOk) as Button
            actionOk.setOnClickListener {
                dismiss()
            }
            show()
        }
    }

    private fun setFormAnimation() {
        val translationX = ObjectAnimator.ofFloat(binding.linearLayoutMonthlyTabHolder, "translationX", 20.0f, binding.clHomePageTabHolder.width.toFloat())
        val translationX2 = ObjectAnimator.ofFloat(binding.linearLayoutDailyTabHolder, "translationX", -(binding.clHomePageTabHolder.width.toFloat() + 20.0f), 0.0f)

        AnimatorSet().apply {
            this.play(translationX)
            this.duration = 250
            this.doOnStart {
                formController = false
            }
            this.doOnEnd {
                binding.linearLayoutMonthlyTabHolder.visibility = View.GONE
            }
            this.start()
        }

        binding.linearLayoutDailyTabHolder.visibility = View.VISIBLE

        AnimatorSet().apply {
            this.play(translationX2)
            this.duration = 250
            this.start()
            this.doOnEnd {
                formController = true
            }
        }
    }

    private fun setFormAnimation2() {
        val translationX = ObjectAnimator.ofFloat(binding.linearLayoutDailyTabHolder, "translationX", 20.0f, -binding.clHomePageTabHolder.width.toFloat())
        val translationX2 = ObjectAnimator.ofFloat(binding.linearLayoutMonthlyTabHolder, "translationX", (binding.clHomePageTabHolder.width.toFloat() + 20.0f), 0.0f)

        AnimatorSet().apply {
            this.play(translationX)
            this.duration = 250
            this.doOnStart {
                formController = false
            }
            this.doOnEnd {
                binding.linearLayoutDailyTabHolder.visibility = View.GONE
            }
            this.start()
        }

        binding.linearLayoutMonthlyTabHolder.visibility = View.VISIBLE

        AnimatorSet().apply {
            this.play(translationX2)
            this.duration = 250
            this.start()
            this.doOnEnd {
                formController = true
            }
        }
    }

    private fun dailyNavigationOnClick(controller: Int) {
        HomePageTransfer.whichTab = false
        if (controller == 0)
            Navigation.findNavController(binding.etDailyPickUp).navigate(R.id.homePageToPickUp)
        else
            Navigation.findNavController(binding.root).navigate(R.id.homePageToDtPicker)
    }

    private fun monthlyNavigationOnClick(controller: Int) {
        HomePageTransfer.whichTab = true
        if (controller == 0)
            Navigation.findNavController(binding.etMonthlyDropOff).navigate(R.id.homePageToPickUp)
        else
            Navigation.findNavController(binding.etMonthlyPickUpDropOffDate).navigate(R.id.homePageToDateTime)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(edit: Editable) {
        if (binding.etDailyPromotion.isFocused && edit.length == 4 && !promotionController2) {
            edit.insert(4, "-")
            binding.promotionCode = edit.toString().trim()
        }
        if (binding.etMonthlyPromotion.isFocused && edit.length == 4 && !promotionController3) {
            edit.insert(4, "-")
            binding.promotionCode2 = edit.toString().trim()
        }

        binding.dividerController4 = binding.etDailyPromotion.text.toString().isNotEmpty()
        binding.dividerController8 = binding.etMonthlyPromotion.text.toString().isNotEmpty()
    }

    private fun Dialog.size() {
        this.window?.apply {
            setLayout((resources.displayMetrics.widthPixels * 0.89).toInt(), LayoutParams.WRAP_CONTENT)
        }
    }

}