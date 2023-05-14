package com.filozilla.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.filozilla.R
import com.filozilla.databinding.FragmentForgotpwBinding
import com.filozilla.interfaces.CommonInterface
import com.filozilla.viewmodels.ForgotPwFragmentVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ForgotPwFragment : Fragment(), CommonInterface, TextWatcher {
    private lateinit var binding: FragmentForgotpwBinding
    private lateinit var viewModel: ForgotPwFragmentVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentForgotpwBinding.inflate(inflater, container, false)
        binding.fragmentForgotPwObject = this
        binding.clearVisibility = false
        binding.cvController = false

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ForgotPwFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
        touchListeners()
        observer()
    }

    override fun initializeComponents() {
        binding.ivForgotPwClearVisibility.bringToFront()
        binding.etForgotPwEmail.addTextChangedListener(this)
    }

    private fun cvCheck(coroutineScope: CoroutineScope, edit: Editable) {
        binding.clearVisibility = edit.toString().trim().isNotEmpty()
        binding.cvController = Patterns.EMAIL_ADDRESS.matcher(binding.etForgotPwEmail.text.toString().trim()).matches()

        coroutineScope.cancel()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun touchListeners() {
        binding.cvForgotPw.setOnTouchListener { view, event ->
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

    private fun observer() {
        viewModel.getValidateController().observe(viewLifecycleOwner) {
            when(it) {
                0 -> {
                    showAlert("E-Posta adresiniz doğrulanamadı.")
                }
                1 -> {
                    toSecurityCodePage(email = viewModel.getEmail(), fullName = viewModel.getFullName())
                }
                2 -> {
                    showAlert("E-Posta adresiniz doğrulanırken hata meydana geldi.")
                }
            }
        }
    }

    private fun animationController(view: View) {
        if (view.id == binding.clForgotPwBackHolder.id) {
            viewModel.viewAnimation(view)
        }else {
            if (binding.cvController!!) {
                viewModel.viewAnimation(view)
            }
        }
    }

    private fun animationController2(view: View) {
        if (view.id == binding.clForgotPwBackHolder.id) {
            viewModel.viewAnimation2(view)
        }else {
            if (binding.cvController!!) {
                viewModel.viewAnimation2(view)
            }
        }
    }

    fun backOnClick() {
        try {
            Navigation.findNavController(binding.ivForgotPwBack).navigate(R.id.forgotPwToSignIn)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearVisibilityOnClick() {
        if (binding.clearVisibility!!) {
            binding.etForgotPwEmail.text.clear()
        }
    }

    fun forgotPwOnClick(email: String) {
        if (binding.cvController!!) {
            inquiryEmail(email = email)
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(edit: Editable?) {
        CoroutineScope(Dispatchers.Main).launch {
            cvCheck(this, edit!!)
        }
    }

    private fun inquiryEmail(email: String) {
        viewModel.validateEmail(email = email)
    }

    private fun toSecurityCodePage(email: String, fullName: String) {
        val id = ForgotPwFragmentDirections.forgotPwToSecurityCode(email = email, fullName = fullName)
        Navigation.findNavController(binding.cvForgotPw).navigate(id)
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(requireContext()).apply {
            this.setCancelable(false)
            this.setMessage(message)
            this.setPositiveButton("TAMAM") { _, _ -> }
            this.create().show()
        }
    }

}