package com.filozilla.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.filozilla.R
import com.filozilla.databinding.FragmentSecuritycodeBinding
import com.filozilla.interfaces.CommonInterface
import com.filozilla.viewmodels.SecurityCodeFragmentVM
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class SecurityCodeFragment : Fragment(), TextWatcher, CommonInterface {
    private lateinit var binding: FragmentSecuritycodeBinding
    private lateinit var viewModel: SecurityCodeFragmentVM
    private var views: ArrayList<EditText>? = null
    private var randomNumber = "" ; private var tryCounter = 0 ; private var email = "" ; private var fullName = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSecuritycodeBinding.inflate(inflater)
        binding.fragmentSecurityCodeObject = this
        binding.cvController = false

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: SecurityCodeFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
        changedListeners()
        focusListeners()
        keyListeners()
        touchListeners()
    }

    override fun initializeComponents() {
        initCustomDialog()
        val bundle: SecurityCodeFragmentArgs by navArgs()
        email = bundle.email
        fullName = bundle.fullName
        randomNumber = generateRandomNumber()
        val subject = "Filozilla Şifre Yenileme"
        val body = "Merhaba <b>${fullName}</b>, hesabına ait şifreni yenilemek için güvenlik kodun: <b>$randomNumber</b>"

        viewModel.sendEmail(email = email, fullName = fullName, subject = subject, body = body)

        viewModel.sendController.observe(requireActivity()) {
            when(it) {
                1 -> {
                    showAlert(message = getString(R.string.success_security_code))

                }
                2 -> {
                    showAlert(message = getString(R.string.error_occured_security_code))

                }
                3 -> {
                    showAlert(message = getString(R.string.not_security_code))

                }
            }
        }

        viewModel.time.observe(requireActivity()) {
            binding.time = it
        }

        views = arrayListOf(binding.etSecurityCodeDigit, binding.etSecurityCodeDigit2, binding.etSecurityCodeDigit3, binding.etSecurityCodeDigit4)
        binding.etSecurityCodeDigit.requestFocus()
    }

    private fun initCustomDialog() {

    }

    private fun changedListeners() {
        binding.etSecurityCodeDigit.addTextChangedListener(this)
        binding.etSecurityCodeDigit2.addTextChangedListener(this)
        binding.etSecurityCodeDigit3.addTextChangedListener(this)
        binding.etSecurityCodeDigit4.addTextChangedListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun touchListeners() {
        binding.cvSecurityCodeConfirm.setOnTouchListener { view, event ->
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
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun focusListeners() {
        binding.etSecurityCodeDigit.setOnFocusChangeListener { _, b ->
            if (b) {
                binding.etSecurityCodeDigit.background = resources.getDrawable(R.drawable.et_bg5, null)
            }else {
                binding.etSecurityCodeDigit.background = resources.getDrawable(R.drawable.et_bg4, null)
            }
        }

        binding.etSecurityCodeDigit2.setOnFocusChangeListener { _, b ->
            if (b) {
                binding.etSecurityCodeDigit2.background = resources.getDrawable(R.drawable.et_bg5, null)
            }else {
                binding.etSecurityCodeDigit2.background = resources.getDrawable(R.drawable.et_bg4, null)
            }
        }

        binding.etSecurityCodeDigit3.setOnFocusChangeListener { _, b ->
            if (b) {
                binding.etSecurityCodeDigit3.background = resources.getDrawable(R.drawable.et_bg5, null)
            }else {
                binding.etSecurityCodeDigit3.background = resources.getDrawable(R.drawable.et_bg4, null)
            }
        }

        binding.etSecurityCodeDigit4.setOnFocusChangeListener { _, b ->
            if (b) {
                binding.etSecurityCodeDigit4.background = resources.getDrawable(R.drawable.et_bg5, null)
            }else {
                binding.etSecurityCodeDigit4.background = resources.getDrawable(R.drawable.et_bg4, null)
            }
        }
    }

    private fun keyListeners() {
        binding.etSecurityCodeDigit2.setOnKeyListener { _, keyCode, _ ->

            if (keyCode == KeyEvent.KEYCODE_DEL) {
                binding.etSecurityCodeDigit.requestFocus()
            }

            return@setOnKeyListener false
        }

        binding.etSecurityCodeDigit3.setOnKeyListener { _, keyCode, _ ->

            if (keyCode == KeyEvent.KEYCODE_DEL) {
                binding.etSecurityCodeDigit2.requestFocus()
            }

            return@setOnKeyListener false
        }

        binding.etSecurityCodeDigit4.setOnKeyListener { _, keyCode, _ ->

            if (keyCode == KeyEvent.KEYCODE_DEL) {
                binding.etSecurityCodeDigit3.requestFocus()
            }

            return@setOnKeyListener false
        }
    }

    fun backOnClick() {
        showBackAlert()
    }

    fun timeOnClick() {
        if (viewModel.time.value.equals(getString(R.string.resend))) {
            randomNumber = generateRandomNumber()
            tryCounter = 0
            val subject = "Filozilla Şifre Yenileme"
            val body = "Merhaba <b>${fullName}</b>, hesabına ait şifreni yenilemek için güvenlik kodun: <b>$randomNumber</b>"

            viewModel.sendEmail(email = email, fullName = fullName, subject = subject, body = body)
        }
    }

    fun confirmOnClick() {
        if (binding.cvController!! && tryCounter < 3) {
            val enteredNumber = binding.etSecurityCodeDigit.text.toString() + binding.etSecurityCodeDigit2.text.toString() + binding.etSecurityCodeDigit3.text.toString() + binding.etSecurityCodeDigit4.text.toString()
            if (enteredNumber == randomNumber) {
                val id = SecurityCodeFragmentDirections.securityCodeToChangePw(email = email)
                Navigation.findNavController(binding.ivSecurityCodeBack).navigate(id)
            }else {
                showAlert(message = getString(R.string.error_security_code))
                tryCounter++
            }
        }
        if (tryCounter >= 3) {
            showAlert(message = getString(R.string.multi_error_security_code))
            toSplashPage()
        }
    }

    private fun animationController(view: View) {
        if (binding.cvController!!) {
            viewModel.viewAnimation(view)
        }
    }

    private fun animationController2(view: View) {
        if (binding.cvController!!) {
            viewModel.viewAnimation2(view)
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if ((views!![0]).text.toString().isNotEmpty() && (views!![1]).text.toString().isNotEmpty() && (views!![2]).text.toString().isNotEmpty() && (views!![3]).text.toString().isNotEmpty()) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        }

        if (!TextUtils.isEmpty((views!![0]).text.toString())) {
            (views!![1]).requestFocus()
        }

        if (!TextUtils.isEmpty((views!![1]).text.toString())) {
            (views!![2]).requestFocus()
        }

        if (!TextUtils.isEmpty((views!![2]).text.toString())) {
            (views!![3]).requestFocus()
        }

    }

    override fun afterTextChanged(edit: Editable) {
        CoroutineScope(Dispatchers.Main).launch {
            etCheck(this)
        }
    }

    private fun etCheck(coroutineScope: CoroutineScope) {
        val controller = binding.etSecurityCodeDigit.text.toString().isNotEmpty()
        val controller2 = binding.etSecurityCodeDigit2.text.toString().isNotEmpty()
        val controller3 = binding.etSecurityCodeDigit3.text.toString().isNotEmpty()
        val controller4 = binding.etSecurityCodeDigit4.text.toString().isNotEmpty()

        binding.cvController = controller && controller2 && controller3 && controller4 && !viewModel.time.equals(getString(R.string.resend))

        coroutineScope.cancel()
    }

    private fun generateRandomNumber(): String {
        return String.format("%04d", Random().nextInt(9999))
    }

    private fun toSplashPage() {
        Navigation.findNavController(requireView()).navigate(R.id.securityCodeToSplash)
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(requireContext()).apply {
            this.setCancelable(false)
            this.setMessage(message)
            this.setPositiveButton(getString(R.string.okay)) { _, _ -> }
            this.create().show()
        }
    }

    private fun showBackAlert() {
        AlertDialog.Builder(requireContext()).apply {
            this.setCancelable(false)
            this.setMessage(getString(R.string.back_repw))
            this.setPositiveButton(getString(R.string.close)) { _, _ ->
                try {
                    Navigation.findNavController(binding.ivSecurityCodeBack).navigate(R.id.securityCodeToForgotPw)
                }catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            this.setNegativeButton(getString(R.string.cancel2)) { _, _ -> }
            this.create().show()
        }
    }

}