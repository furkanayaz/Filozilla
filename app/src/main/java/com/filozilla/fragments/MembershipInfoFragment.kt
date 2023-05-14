package com.filozilla.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.filozilla.R
import com.filozilla.activities.MainActivity
import com.filozilla.activities.MembershipActivity
import com.filozilla.databinding.ChangePwDialogBinding
import com.filozilla.databinding.FragmentMembershipInfoBinding
import com.filozilla.interfaces.CommonInterface
import com.filozilla.models.User
import com.filozilla.repositories.CustomDialogRepo
import com.filozilla.repositories.ViewChangesRepo
import com.filozilla.viewmodels.MembershipInfoFragmentVM
import com.google.gson.Gson

class MembershipInfoFragment: Fragment(), CommonInterface {
    private lateinit var binding: FragmentMembershipInfoBinding
    private lateinit var viewModel: MembershipInfoFragmentVM
    private lateinit var customDialogRepo: CustomDialogRepo
    private lateinit var viewChangesRepo: ViewChangesRepo
    private var changePwDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: MembershipInfoFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMembershipInfoBinding.inflate(layoutInflater, container, false)
        binding.apply {
            fragmentMembershipInfoObject = this@MembershipInfoFragment
            creationDate = ""
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
        touchListeners()
    }

    override fun initializeComponents() {
        initRepos()
        setOtherChanges()
        observer()
    }

    override fun touchListeners() {

    }

    private fun initRepos() {
        customDialogRepo = CustomDialogRepo(context = requireContext())
        viewChangesRepo = ViewChangesRepo(activity = requireActivity(), context = requireContext())
    }

    private fun setOtherChanges() {
        MainActivity.user?.let { user ->
            binding.creationDate = viewModel.userCreationDate(date = user.iCreationDate)
            binding.etMembershipInfoName.setText(user.fullName)
            binding.etMembershipInfoEmail.setText(user.email)
            binding.etMembershipInfoDateOfBirth.setText(user.birthDate)
            binding.etMembershipInfoTcknPassport.setText(user.tcknPassport)
        }?: showToastMsg(msg = getString(R.string.error_account))
    }

    private fun observer() {
        viewModel.getMembershipInfoController().observe(viewLifecycleOwner) {
            when(it) {
                0, 2 -> {
                    customDialogRepo.getLoadingDialog()?.dismiss()
                    customDialogRepo.showWarningDialog(msg = getString(R.string.failure_dialog_desc))
                }
                1 -> {
                    val fullName = binding.etMembershipInfoName.text.toString().trim()
                    val birthDate = binding.etMembershipInfoDateOfBirth.text.toString().trim()
                    val tcknPassport = binding.etMembershipInfoTcknPassport.text.toString().trim()

                    updatePreference(fullName = fullName, birthDate = birthDate, tcknPassport = tcknPassport)
                }
            }
        }
        viewModel.getChangePwController().observe(viewLifecycleOwner) {
            when(it) {
                0, 2 -> {
                    customDialogRepo.getLoadingDialog()?.dismiss()
                    customDialogRepo.showWarningDialog(msg = getString(R.string.warning_change_pw2))
                }
                1 -> {
                    requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)!!.edit().apply {
                        clear()
                        apply()
                        customDialogRepo.getLoadingDialog()?.dismiss()
                        changePwDialog?.dismiss()
                        toMembershipPage()
                    }
                }
            }
        }
        customDialogRepo.getChosenDate().observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                binding.etMembershipInfoDateOfBirth.setText(it)
        }
    }

    fun backOnClick() {

    }

    fun changePwOnClick() {
        var controller = 0
        var controller2 = 0
        var controller3 = 0

        val dialogBinding = ChangePwDialogBinding.inflate(layoutInflater)

        dialogBinding.ivCpCurrentPwVisibility.setOnClickListener {
            if (controller%2 == 0) {
                dialogBinding.ivCpCurrentPwVisibility.setImageResource(R.drawable.eye)
                dialogBinding.etCpCurrentPw.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else {
                dialogBinding.ivCpCurrentPwVisibility.setImageResource(R.drawable.eye_close)
                dialogBinding.etCpCurrentPw.transformationMethod = PasswordTransformationMethod.getInstance()
            }

            dialogBinding.etCpCurrentPw.setSelection(dialogBinding.etCpCurrentPw.text.length)

            controller++
        }

        dialogBinding.ivCpNewPwVisibility.setOnClickListener {
            if (controller2%2 == 0) {
                dialogBinding.ivCpNewPwVisibility.setImageResource(R.drawable.eye)
                dialogBinding.etCpNewPw.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else {
                dialogBinding.ivCpNewPwVisibility.setImageResource(R.drawable.eye_close)
                dialogBinding.etCpNewPw.transformationMethod = PasswordTransformationMethod.getInstance()
            }

            dialogBinding.etCpNewPw.setSelection(dialogBinding.etCpNewPw.text.length)

            controller2++
        }

        dialogBinding.ivCpNewRePwVisibility.setOnClickListener {
            if (controller3%2 == 0) {
                dialogBinding.ivCpNewRePwVisibility.setImageResource(R.drawable.eye)
                dialogBinding.etCpNewRePw.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else {
                dialogBinding.ivCpNewRePwVisibility.setImageResource(R.drawable.eye_close)
                dialogBinding.etCpNewRePw.transformationMethod = PasswordTransformationMethod.getInstance()
            }

            dialogBinding.etCpNewRePw.setSelection(dialogBinding.etCpNewRePw.text.length)

            controller3++
        }

        dialogBinding.etCpCurrentPw.setOnFocusChangeListener { _, b ->
            changeDividerColor(view = dialogBinding.dividerCpDialog, controller = b)
        }
        dialogBinding.etCpNewPw.setOnFocusChangeListener { _, b ->
            changeDividerColor(view = dialogBinding.divider2CpDialog, controller = b)
        }
        dialogBinding.etCpNewRePw.setOnFocusChangeListener { _, b ->
            changeDividerColor(view = dialogBinding.divider3CpDialog, controller = b)
        }

        dialogBinding.etCpCurrentPw.addTextChangedListener {
            dialogBinding.ivCpCurrentPwVisibility.visibility = if (it.toString().isNotEmpty()) View.VISIBLE else View.GONE
        }
        dialogBinding.etCpNewPw.addTextChangedListener {
            dialogBinding.ivCpNewPwVisibility.visibility = if (it.toString().isNotEmpty()) View.VISIBLE else View.GONE
        }
        dialogBinding.etCpNewRePw.addTextChangedListener {
            dialogBinding.ivCpNewRePwVisibility.visibility = if (it.toString().isNotEmpty()) View.VISIBLE else View.GONE
        }

        changePwDialog = Dialog(requireContext()).apply {
            setCancelable(false)
            setContentView(dialogBinding.root)
            size()
            window?.apply {
                setGravity(Gravity.CENTER and Gravity.CENTER_HORIZONTAL and Gravity.CENTER_VERTICAL)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            show()

            dialogBinding.ivCpClose.setOnClickListener {
                dismiss()
            }

            dialogBinding.btnCp.setOnClickListener {
                MainActivity.user?.let { user ->
                    val currentPw = dialogBinding.etCpCurrentPw.text.toString()
                    val pw = dialogBinding.etCpNewPw.text.toString()
                    val rePw = dialogBinding.etCpNewRePw.text.toString()

                    if (currentPw != user.pw) {
                        customDialogRepo.showWarningDialog(msg = getString(R.string.warning_dialog_desc_2))
                        return@setOnClickListener
                    }
                    if ((pw != rePw) || pw.length < 6) {
                        customDialogRepo.showWarningDialog(msg = getString(R.string.warning_dialog_desc_3))
                        return@setOnClickListener
                    }

                    customDialogRepo.createLoadingDialog()
                    viewModel.updatePw(i_id = user.iId, pw = pw)

                }?: showToastMsg(msg = getString(R.string.error_account))
            }
        }
    }

    private fun changeDividerColor(view: View, controller: Boolean) {
        viewChangesRepo.changeBgColor(view = view, controller = controller)
    }

    fun dateOfBirthOnClick() {
        customDialogRepo.createDatePickerDialog()
    }

    fun deleteAccountOnClick() {
        Navigation.findNavController(binding.cvMembershipInfoDeleteAccount).navigate(R.id.editToDeleteAccount)
    }

    fun saveOnClick(fullName: String, birthDate: String, tcknPassport: String, email: String) {
        if (fullName.isNotEmpty() && birthDate.isNotEmpty() && tcknPassport.isNotEmpty() && email.isNotEmpty()) {
            customDialogRepo.createLoadingDialog()
            viewModel.saveMembershipInfo(fullName = fullName, birthDate = birthDate, tcknPassport = tcknPassport, email = email)
        }else {
            customDialogRepo.showWarningDialog(msg = getString(R.string.warning_dialog_desc))

            if (fullName.isEmpty()) {
                binding.etMembershipInfoName.error = "Ad soyad giriniz."
            }
            if (birthDate.isEmpty()) {
                binding.etMembershipInfoDateOfBirth.error = "Doğum tarihini giriniz."
            }
            if (tcknPassport.isEmpty()) {
                binding.etMembershipInfoTcknPassport.error = "TCKN/Pasaport giriniz."
            }
            if (email.isEmpty()) {
                binding.etMembershipInfoEmail.error = "E-posta giriniz." // Burası düzenlenmesi gerek (Uygulamada beklenmeyen hata oluştu falan diyip signout ve finish etmen lazım.)
            }
        }
    }

    private fun updatePreference(fullName: String, birthDate: String, tcknPassport: String) {
        var user = ""

        MainActivity.user?.let {
            it.fullName = fullName
            it.birthDate = birthDate
            it.tcknPassport = tcknPassport
            user = Gson().toJson(it, User::class.java)
        }?: showToastMsg(msg = getString(R.string.error_account))

        requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit().run {
            remove("user")
            putString("user", user)
            apply()

            customDialogRepo.getLoadingDialog()?.dismiss()
            customDialogRepo.showSuccessDialog(controller = false, msg = getString(R.string.success_dialog_desc))
        }

    }

    private fun toMembershipPage() {
        Intent(requireContext(), MembershipActivity::class.java).apply {
            requireActivity().startActivity(this)
            requireActivity().finish()
        }
    }

    private fun Dialog.size() {
        this.window?.apply {
            setLayout((context.resources.displayMetrics.widthPixels * 0.93).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    private fun showToastMsg(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

}