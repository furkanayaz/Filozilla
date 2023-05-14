package com.filozilla.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.filozilla.R
import com.filozilla.databinding.FragmentChangepwBinding
import com.filozilla.responses.CRUD
import com.filozilla.retrofits.RetrofitUtils
import com.filozilla.viewmodels.ChangePwFragmentVM
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePwFragment : Fragment(), TextWatcher {
    private lateinit var binding: FragmentChangepwBinding
    private lateinit var viewModel: ChangePwFragmentVM
    private var pwVisibility = false
    private var pwVisibility2 = false
    private var email: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChangepwBinding.inflate(inflater, container, false)
        binding.fragmentChangePwObject = this
        binding.cvController = false
        binding.pwVisibility = false
        binding.pwVisibility2 = false

        initializeComponents()
        touchListeners()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ChangePwFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    fun initializeComponents() {
        binding.ivChangePwVisibility.bringToFront()
        binding.ivChangePwVisibility2.bringToFront()
        binding.etChangePw.addTextChangedListener(this)
        binding.etChangePw2.addTextChangedListener(this)

        val bundle: ChangePwFragmentArgs by navArgs()
        email = bundle.email
    }

    @SuppressLint("ClickableViewAccessibility")
    fun touchListeners() {
        binding.cvChangePw.setOnTouchListener { view, event ->
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
        if (binding.cvController!!) {
            viewModel.viewAnimation(view)
        }
    }

    private fun animationController2(view: View) {
        if (binding.cvController!!) {
            viewModel.viewAnimation2(view)
        }
    }

    fun changePwOnClick(pw: String) {
        if (binding.cvController!!) {
            changePw(pw)
        }
    }

    private fun changePw(pw: String) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitUtils.getRetrofitDaoInterface().forgotPw(email = email, pw = pw).enqueue(object : Callback<CRUD> {
                override fun onResponse(call: Call<CRUD>, response: Response<CRUD>) {
                    when (response.body()?.success) {
                        0 -> {
                            this@launch.cancel()
                            showAlert("Şifreniz değiştirilemedi.")
                        }
                        1 -> {
                            showSnackBar(this@launch)
                        }
                    }
                }

                override fun onFailure(call: Call<CRUD>, t: Throwable) {
                    showAlert("Şifre değiştirilirken hata meydana geldi.")
                }

            })
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(edit: Editable?) {
        CoroutineScope(Dispatchers.Main).launch {
            cvCheck(this)
        }
    }

    private fun cvCheck(coroutineScope: CoroutineScope) {
        binding.pwVisibility = binding.etChangePw.text.toString().isNotEmpty()
        binding.pwVisibility2 = binding.etChangePw2.text.toString().isNotEmpty()
        binding.cvController = (binding.etChangePw.text.toString() == binding.etChangePw2.text.toString()) && (binding.etChangePw.text.toString().length >= 6)

        coroutineScope.cancel()
    }

    fun closeOnClick() {
        showCloseAlert()
    }

    fun pwVisibilityOnClick() {
        if (pwVisibility) {
            binding.ivChangePwVisibility.setImageResource(R.drawable.eye_close)
            binding.etChangePw.transformationMethod = PasswordTransformationMethod.getInstance()
            pwVisibility = false
        }else {
            binding.ivChangePwVisibility.setImageResource(R.drawable.eye)
            binding.etChangePw.transformationMethod = HideReturnsTransformationMethod.getInstance()
            pwVisibility = true
        }
    }

    fun pwVisibility2OnClick() {
        if (pwVisibility2) {
            binding.ivChangePwVisibility2.setImageResource(R.drawable.eye_close)
            binding.etChangePw2.transformationMethod = PasswordTransformationMethod.getInstance()
            pwVisibility2 = false
        }else {
            binding.ivChangePwVisibility2.setImageResource(R.drawable.eye)
            binding.etChangePw2.transformationMethod = HideReturnsTransformationMethod.getInstance()
            pwVisibility2 = true
        }
    }

    fun showAlert(message: String) {
        AlertDialog.Builder(requireContext()).apply {
            this.setCancelable(false)
            this.setMessage(message)
            this.setPositiveButton("TAMAM") { _, _ -> }
            this.create().show()
        }
    }

    fun showSnackBar(coroutineScope: CoroutineScope) {
        clearInput()
        Snackbar.make(binding.cvChangePw, "Şifreniz başarıyla değiştirildi.", Snackbar.LENGTH_INDEFINITE).apply {
            this.setAction("TAMAM") {
                toSplashPage()
                coroutineScope.cancel()
            }
            this.show()
        }
    }

    private fun toSplashPage() {
        Navigation.findNavController(binding.cvChangePw).navigate(R.id.changePwToSplash)
    }

    private fun clearInput() {
        binding.etChangePw.text.clear()
        binding.etChangePw2.text.clear()
    }

    private fun showCloseAlert() {
        AlertDialog.Builder(requireContext()).apply {
            this.setCancelable(false)
            this.setMessage("Uygulamadan çıkmak istediğinize emin misiniz ?")
            this.setPositiveButton("ÇIK") { _, _ -> toSplashPage() }
            this.setNegativeButton("İPTAL") { _, _ -> }
            this.create().show()
        }
    }

}