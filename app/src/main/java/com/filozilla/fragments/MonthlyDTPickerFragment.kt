package com.filozilla.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.filozilla.R
import com.filozilla.databinding.FragmentMonthlyDtPickerBinding
import com.filozilla.databinding.MonthPickerDialogBinding
import com.filozilla.datatransfers.DateTimeTransfer
import com.filozilla.viewmodels.DateTimeFragmentVM
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import java.util.Calendar
import java.util.Date

class MonthlyDTPickerFragment: Fragment() {
    private lateinit var binding: FragmentMonthlyDtPickerBinding
    private lateinit var viewModel: DateTimeFragmentVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: DateTimeFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMonthlyDtPickerBinding.inflate(layoutInflater, container, false)
        binding.apply {
            dtPickerObject = this@MonthlyDTPickerFragment
            backgroundImageVisibility = true
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
    }

    private fun initializeComponents() {
        getInfo()
        initBg()
        initVideo()
    }

    private fun getInfo() {
        createDatePicker()
    }

    private fun initBg() {
        binding.imageViewDt.bringToFront()

        Picasso.get()
            .load(Uri.parse("https://filozilla.com/introduction_images/togg.jpg"))
            .into(binding.imageViewDt)
    }

    private fun initVideo() {
        binding.apply {
            videoViewDt.setVideoURI(Uri.parse("https://filozilla.com/introduction_videos/introducing_togg.mp4"))
            videoViewDt.keepScreenOn = true
            videoViewDt.setOnPreparedListener {
                backgroundImageVisibility = false
                it.isLooping = true
                videoViewDt.start()
            }
        }
    }

    private fun createDatePicker() {
        val calendar = Calendar.getInstance()
        val tempDay = calendar.get(Calendar.DAY_OF_MONTH)
        val tempMonth = calendar.get(Calendar.MONTH)
        val tempYear = calendar.get(Calendar.YEAR)

        DatePickerDialog(requireContext(), { _, year, month, day_of_month ->
            createTimePicker(date = viewModel.editDate(year = year, month = month, day_of_month = day_of_month))
        }, tempYear, tempMonth, tempDay).also {
            it.datePicker.minDate = calendar.timeInMillis
            it.setCancelable(false)
            it.setOnCancelListener { clearInfo() ; toSearchPage() }
            it.show()
        }
    }

    private fun createTimePicker(date: Date?) {
        val calendar = Calendar.getInstance()
        val tempHour = calendar.get(Calendar.HOUR_OF_DAY)
        val tempMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(requireContext(), { _, hour, minute ->
            viewModel.editTime(date = date, hour = hour, minute = minute)

            createMonthPickerDialog()
        }, tempHour, tempMinute, true).also {
            it.setCancelable(false)
            it.setOnCancelListener { clearInfo() ; toSearchPage() }
            it.show()
        }
    }

    private fun createMonthPickerDialog() {
        BottomSheetDialog(requireContext()).apply {
            val dialogBinding = MonthPickerDialogBinding.inflate(layoutInflater)

            dialogBinding.run {
                ivMonthPickerClose.setOnClickListener {
                    clearInfo()
                    toSearchPage()
                    this@apply.dismiss()
                }

                tvShortTerm.setOnClickListener {
                    tvShortTerm.visibility = View.GONE
                    ivShortTermArrow.visibility = View.GONE
                    tvShortTerm1Month.visibility = View.VISIBLE
                    tvShortTerm2Months.visibility = View.VISIBLE
                    tvShortTerm3Months.visibility = View.VISIBLE
                    tvShortTerm4Months.visibility = View.VISIBLE
                    tvShortTerm5Months.visibility = View.VISIBLE
                    tvShortTerm6Months.visibility = View.VISIBLE
                }

                tvLongTerm.setOnClickListener {
                    tvLongTerm.visibility = View.GONE
                    ivLongTermArrow.visibility = View.GONE
                    tvShortTerm12Months.visibility = View.VISIBLE
                    tvShortTerm24Months.visibility = View.VISIBLE
                    tvShortTerm36Months.visibility = View.VISIBLE
                }

                tvShortTerm1Month.setOnClickListener {
                    DateTimeTransfer.monthlyDate += " - 1 Ay"
                    toSearchPage()
                    dismiss()
                }
                tvShortTerm2Months.setOnClickListener {
                    DateTimeTransfer.monthlyDate += " - 2 Ay"
                    toSearchPage()
                    dismiss()
                }
                tvShortTerm3Months.setOnClickListener {
                    DateTimeTransfer.monthlyDate += " - 3 Ay"
                    toSearchPage()
                    dismiss()
                }
                tvShortTerm4Months.setOnClickListener {
                    DateTimeTransfer.monthlyDate += " - 4 Ay"
                    toSearchPage()
                    dismiss()
                }
                tvShortTerm5Months.setOnClickListener {
                    DateTimeTransfer.monthlyDate += " - 5 Ay"
                    toSearchPage()
                    dismiss()
                }
                tvShortTerm6Months.setOnClickListener {
                    DateTimeTransfer.monthlyDate += " - 6 Ay"
                    toSearchPage()
                    dismiss()
                }
                tvShortTerm12Months.setOnClickListener {
                    DateTimeTransfer.monthlyDate += " - 12 Ay"
                    toSearchPage()
                    dismiss()
                }
                tvShortTerm24Months.setOnClickListener {
                    DateTimeTransfer.monthlyDate += " - 24 Ay"
                    toSearchPage()
                    dismiss()
                }
                tvShortTerm36Months.setOnClickListener {
                    DateTimeTransfer.monthlyDate += " - 36 Ay"
                    toSearchPage()
                    dismiss()
                }
            }

            setCancelable(false)
            setContentView(view = dialogBinding.root)
            show()
        }
    }

    private fun toSearchPage() = Navigation.findNavController(binding.root).navigate(R.id.dateTimeToSearch)

    private fun clearInfo() {
        DateTimeTransfer.run {
            monthlyDateMillis = 0L
            monthlyDate = ""
        }
    }

}