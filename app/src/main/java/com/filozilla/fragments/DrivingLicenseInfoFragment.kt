package com.filozilla.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.filozilla.R
import com.filozilla.activities.MainActivity
import com.filozilla.databinding.FragmentDrivingLicenseInfoBinding
import com.filozilla.interfaces.CommonInterface
import com.filozilla.models.User
import com.filozilla.repositories.CustomDialogRepo
import com.filozilla.viewmodels.DrivingLicenseInfoFragmentVM
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson

@SuppressLint("InflateParams")
class DrivingLicenseInfoFragment : Fragment(), CommonInterface {
    private lateinit var binding: FragmentDrivingLicenseInfoBinding
    private lateinit var viewModel: DrivingLicenseInfoFragmentVM
    private lateinit var customDialogRepo: CustomDialogRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: DrivingLicenseInfoFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDrivingLicenseInfoBinding.inflate(inflater, container, false)
        binding.apply {
            fragmentDrivingLicenseInfoObject = this@DrivingLicenseInfoFragment
            drivingChosenClass = getString(R.string.pls_choose)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
        touchListeners()
    }

    override fun initializeComponents() {
        initCustomDialog()
        setOtherChanges()
        observer()
    }

    private fun initCustomDialog() { customDialogRepo = CustomDialogRepo(context = requireContext()) }

    private fun setOtherChanges() {
        MainActivity.user?.let { user ->
            user.drivingNumber?.let { drivingNumber ->
                binding.etDrivingInfoNumber.setText(drivingNumber.toString())
            }

            if (user.drivingChosenClass.isNotEmpty()) {
                binding.drivingChosenClass = user.drivingChosenClass
            }

            binding.etDrivingInfoPickUp.setText(user.drivingPickUp)
            binding.etDrivingInfoIssueDate.setText(user.drivingIssueDate)
        }?: showToastMsg(msg = getString(R.string.error_account))
    }

    private fun observer() {
        viewModel.getDrivingLicenseInfoController().observe(viewLifecycleOwner) {
            when (it) {
                0, 2 -> {
                    customDialogRepo.getLoadingDialog()?.dismiss()
                    customDialogRepo.showWarningDialog(msg = getString(R.string.warning_dialog_desc_4))
                }
                1 -> {
                    val drivingNumber = binding.etDrivingInfoNumber.text.toString().trim().toInt()
                    val drivingPickUp = binding.etDrivingInfoPickUp.text.toString().trim()
                    val drivingIssueDate = binding.etDrivingInfoIssueDate.text.toString().trim()

                    updatePreference(drivingNumber = drivingNumber, drivingChosenClass = binding.drivingChosenClass!!, drivingPickUp = drivingPickUp, drivingIssueDate = drivingIssueDate)
                }
            }
        }
        customDialogRepo.getChosenDate().observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                binding.etDrivingInfoIssueDate.setText(it)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun touchListeners() {
        binding.apply {
            clDrivingInfoDrivingClassHolder.setOnTouchListener { view, motionEvent ->

                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        viewModel.viewAnimation(view = view)
                    }
                    MotionEvent.ACTION_UP -> {
                        viewModel.viewAnimation2(view = view)
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        viewModel.viewAnimation2(view = view)
                    }
                }

                false
            }
        }
    }

    fun backOnClick() {

    }

    fun drivingClassOnClick() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.driving_class_dialog, null, false)
        val rbDrivingClassB = view.findViewById<View>(R.id.rbDrivingClassB)
        val rbDrivingClassC = view.findViewById<View>(R.id.rbDrivingClassC)
        val rbDrivingClassD = view.findViewById<View>(R.id.rbDrivingClassD)
        val rbDrivingClassE = view.findViewById<View>(R.id.rbDrivingClassE)
        val rbDrivingClassUSB = view.findViewById<View>(R.id.rbDrivingClassUSB)

        when(binding.drivingChosenClass) {
            "B" -> (rbDrivingClassB as RadioButton).isChecked = true
            "C" -> (rbDrivingClassC as RadioButton).isChecked = true
            "D" -> (rbDrivingClassD as RadioButton).isChecked = true
            "E" -> (rbDrivingClassE as RadioButton).isChecked = true
            "USB" -> (rbDrivingClassUSB as RadioButton).isChecked = true
        }

        BottomSheetDialog(requireContext()).apply {
            setCancelable(true)
            window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
            setContentView(view = view)

            rbDrivingClassB.setOnClickListener {
                binding.drivingChosenClass = "B"
                dismissDialog(dialog = this)
            }
            rbDrivingClassC.setOnClickListener {
                binding.drivingChosenClass = "C"
                dismissDialog(dialog = this)
            }
            rbDrivingClassD.setOnClickListener {
                binding.drivingChosenClass = "D"
                dismissDialog(dialog = this)
            }
            rbDrivingClassE.setOnClickListener {
                binding.drivingChosenClass = "E"
                dismissDialog(dialog = this)
            }
            rbDrivingClassUSB.setOnClickListener {
                binding.drivingChosenClass = "USB"
                dismissDialog(dialog = this)
            }

            show()
        }
    }

    private fun dismissDialog(dialog: BottomSheetDialog) {
        object : CountDownTimer(250, 1000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                dialog.dismiss()
            }

        }.start()
    }

    fun issueDateOnClick() { customDialogRepo.createDatePickerDialog() }

    fun saveOnClick(drivingNumber: String, drivingPickUp: String, drivingIssueDate: String) {
        MainActivity.user?.let {
            if (drivingNumber.isNotEmpty() && binding.drivingChosenClass!!.isNotEmpty() && binding.drivingChosenClass!! != getString(R.string.pls_choose) && drivingPickUp.isNotEmpty() && drivingIssueDate.isNotEmpty()) {
                customDialogRepo.createLoadingDialog()
                viewModel.updateDrivingInfo(uid = it.dUid, drivingNumber = drivingNumber, drivingChosenClass = binding.drivingChosenClass!!, drivingPickUp = drivingPickUp, drivingIssueDate = drivingIssueDate)
            }else {
                if (drivingNumber.isEmpty()) {
                    binding.etDrivingInfoNumber.error = getString(R.string.driving_license_number)
                }
                if (drivingPickUp.isEmpty()) {
                    binding.etDrivingInfoPickUp.error = getString(R.string.driving_license_pickup)
                }
                if (drivingIssueDate.isEmpty()) {
                    binding.etDrivingInfoIssueDate.error = getString(R.string.driving_license_issue_date)
                }

                customDialogRepo.showWarningDialog(msg = getString(R.string.warning_dialog_desc_5))
            }
        }?: showToastMsg(msg = getString(R.string.error_account))
    }

    private fun updatePreference(drivingNumber: Int, drivingChosenClass: String, drivingPickUp: String, drivingIssueDate: String) {
        var newUser = ""

        MainActivity.user?.let {
            it.drivingNumber = drivingNumber
            it.drivingChosenClass = drivingChosenClass
            it.drivingPickUp = drivingPickUp
            it.drivingIssueDate = drivingIssueDate
            newUser = Gson().toJson(it, User::class.java)
        }?: showToastMsg(msg = getString(R.string.error_account))

        requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit().run {
            remove("user")
            putString("user", newUser)
            apply()

            customDialogRepo.getLoadingDialog()?.dismiss()
            customDialogRepo.showSuccessDialog(controller = false, msg = getString(R.string.success_dialog_desc2))
        }
    }

    private fun showToastMsg(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

}