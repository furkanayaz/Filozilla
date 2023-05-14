package com.filozilla.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.filozilla.R
import com.filozilla.activities.MainActivity
import com.filozilla.databinding.EnterPwDialogBinding
import com.filozilla.databinding.FragmentDeleteaccountBinding
import com.filozilla.interfaces.CommonInterface
import com.filozilla.repositories.CustomDialogRepo
import com.filozilla.repositories.ViewChangesRepo
import com.filozilla.rooms.RoomDB
import com.filozilla.viewmodels.DeleteAccountFragmentVM

class DeleteAccountFragment: Fragment(), CommonInterface {
    private lateinit var binding: FragmentDeleteaccountBinding
    private lateinit var viewModel: DeleteAccountFragmentVM
    private lateinit var customDialogRepo: CustomDialogRepo
    private lateinit var viewChangesRepo: ViewChangesRepo
    private var msg = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: DeleteAccountFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDeleteaccountBinding.inflate(layoutInflater, container, false)
        binding.fragmentDeleteAccountObject = this
        binding.accountOtherVisibility = false

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
        touchListeners()
        observer()
    }

    override fun initializeComponents() {
        initRepos()
        checkedChangeListeners()
        changeStatusBarColor(color = R.color.red)
    }

    override fun touchListeners() {

    }

    fun backOnClick() {
        changeStatusBarColor(color = R.color.second_color)
    }

    fun deleteAccountOnClick() {
        binding.rgDeleteAccount.checkedRadioButtonId.run {
            if (this != -1) {
                val selectedRb = binding.root.findViewById(this) as RadioButton

                msg = if (selectedRb.id != R.id.rbDeleteAccountOption5)
                    selectedRb.text.toString()
                else
                    binding.etDeleteAccountOther.text.toString().trim()

                showEpDialog()
            }else {
                customDialogRepo.showWarningDialog(msg = getString(R.string.delete_account_unchosen))
            }
        }

    }

    private fun showEpDialog() {
        val dialogBinding = EnterPwDialogBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(dialogBinding.root)
        val alert = builder.create()

        dialogBinding.etEpCurrentPw.setOnFocusChangeListener { _, b ->
            viewChangesRepo.changeBgColor(view = dialogBinding.divEpDialog, controller = b)
        }

        dialogBinding.etEpCurrentPw.addTextChangedListener {
            dialogBinding.ivEpCurrentPwVisibility.visibility = if (it.toString().isNotEmpty()) View.VISIBLE else View.GONE
        }

        dialogBinding.ivEpClose.setOnClickListener {
            alert.dismiss()
        }

        dialogBinding.btnEpConfirm.setOnClickListener {
            val enteredPw = dialogBinding.etEpCurrentPw.text.toString()

            MainActivity.user?.let { user ->
                if (enteredPw == user.pw) {
                    alert.dismiss()
                    customDialogRepo.createLoadingDialog()
                    viewModel.deleteAccount()
                }else
                    customDialogRepo.showWarningDialog(msg = getString(R.string.warning_dialog_desc_7))
            }
        }

        alert.show()
    }

    private fun toOtherPage() = Navigation.findNavController(binding.root).navigate(R.id.deleteAccountToEdit)

    private fun observer() {
        viewModel.getDeleteAccountController().observe(viewLifecycleOwner) {
            when(it) {
                0, 2 -> {
                    customDialogRepo.getLoadingDialog()?.dismiss()
                    customDialogRepo.showWarningDialog(msg = getString(R.string.delete_account_error))
                }
                1 -> deletePaymentMethods()
            }
        }
        viewModel.getSendMailController().observe(viewLifecycleOwner) {
            when(it) {
                0, 2 -> customDialogRepo.getLoadingDialog()?.dismiss()
                1 -> deletePrefs()
            }
        }
    }

    private fun initRepos() {
        customDialogRepo = CustomDialogRepo(context = requireContext())
        viewChangesRepo = ViewChangesRepo(activity = requireActivity(), context = requireContext())
    }

    private fun changeStatusBarColor(color: Int) {
        viewChangesRepo.changeStatusBarColor(color = color)
    }

    private fun checkedChangeListeners() {
        binding.rbDeleteAccountOption5.setOnCheckedChangeListener { _, b ->
            binding.accountOtherVisibility = b
        }
    }

    private fun deletePaymentMethods() {
        RoomDB.accessDatabase(context = requireContext())
        viewModel.deletePaymentMethods(msg = msg)
    }

    private fun deletePrefs() {
        requireContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit().apply {
            clear()
            apply()

            customDialogRepo.getLoadingDialog()?.dismiss()
            requireActivity().finish()
        }
    }

}