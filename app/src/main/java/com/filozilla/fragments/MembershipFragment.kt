package com.filozilla.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.filozilla.R
import com.filozilla.activities.MainActivity
import com.filozilla.databinding.FragmentMembershipBinding
import com.filozilla.interfaces.CommonInterface
import com.filozilla.repositories.SendEmailRepo
import com.filozilla.viewmodels.MembershipFragmentVM
import com.google.gson.Gson
import kotlinx.coroutines.*

class MembershipFragment : Fragment(), CommonInterface, TextWatcher {
    private lateinit var binding: FragmentMembershipBinding
    private lateinit var viewModel: MembershipFragmentVM
    private var sendEmailRepo = SendEmailRepo()

    // For SignIn
    private var formController = true ; private var pwController = false ; private var pwLengthController = false ; private var pwVisibilityController = false ; private var rememberMeController = false
    // For SignUp
    private var nameController = false ; private var emailController = false ; private var phoneController = false ; private var pwEqualsController = false ; private var pwLengthController2 = false ; private var pwLengthController3 = false ; private var pwVisibilityController2 = false ; private var pwVisibilityController3 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: MembershipFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMembershipBinding.inflate(inflater, container, false)
        binding.apply {
            fragmentMembershipObject = this@MembershipFragment
            tabController = false
            dividerController = false
            dividerController2 = false
            dividerController3 = false
            dividerController4 = false
            dividerController5 = false
            dividerController6 = false
            dividerController7 = false
            dividerController8 = false
            dividerController9 = false
            clearVisibility = false
            clearVisibility2 = false
            clearVisibility3 = false
            clearVisibility4 = false
            pwVisibility = false
            pwVisibility2 = false
            pwVisibility3 = false
            clController = false
            clController2 = false
            signInProgressVisibility = false
            signUpProgressVisibility = false
            signUp2ProgressVisibility = false
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
        touchListeners()
        focusChangeListeners()
        observer()
    }

    override fun initializeComponents() {
        binding.apply {
            ivSigninClearVisibility.bringToFront()
            ivSigninPwVisibility.bringToFront()
            ivSignupPwVisibility.bringToFront()
            ivSignupPwVisibility2.bringToFront()

            etSigninEmail.addTextChangedListener(this@MembershipFragment)
            etSigninPw.addTextChangedListener(this@MembershipFragment)
            etSignupName.addTextChangedListener(this@MembershipFragment)
            etSignupEmail.addTextChangedListener(this@MembershipFragment)
            etSignupPhone.addTextChangedListener(this@MembershipFragment)
            etSignupPw.addTextChangedListener(this@MembershipFragment)
            etSignupRePw.addTextChangedListener(this@MembershipFragment)
        }
    }

    private fun signinCheck() {
        emailController = binding.etSigninEmail.text.toString().trim().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(binding.etSigninEmail.text.toString().trim()).matches()
        pwController = binding.etSigninPw.text.toString().isNotEmpty()
        pwLengthController = binding.etSigninPw.text.toString().length >= 6

        binding.apply {
            clearVisibility = emailController
            pwVisibility = pwController
            dividerController3 = emailController && pwLengthController
            clController = emailController && pwLengthController
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun signUpCheck() {
        nameController = binding.etSignupName.text.toString().trim().length >= 5
        emailController = Patterns.EMAIL_ADDRESS.matcher(binding.etSignupEmail.text.toString().trim()).matches()
        phoneController = Patterns.PHONE.matcher(binding.etSignupPhone.text.toString().trim()).matches()
        pwLengthController2 = binding.etSignupPw.text.toString().length >= 6
        pwLengthController3 = binding.etSignupRePw.text.toString().length >= 6
        pwEqualsController = (binding.etSignupPw.text.toString()) == (binding.etSignupRePw.text.toString())

        binding.apply {
            clearVisibility2 = nameController
            clearVisibility3 = emailController
            clearVisibility4 = phoneController
            pwVisibility2 = binding.etSignupPw.text.toString().isNotEmpty()
            pwVisibility3 = binding.etSignupRePw.text.toString().isNotEmpty()
            dividerController9 = nameController && emailController && phoneController && pwLengthController2 && pwLengthController3 && pwEqualsController
            clController2 = nameController && emailController && phoneController && pwLengthController2 && pwLengthController3 && pwEqualsController
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun touchListeners() {
        binding.apply {
            clSigninGoogle.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        animationController(view, false)
                    }
                    MotionEvent.ACTION_UP -> {
                        animationController2(view, false)
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        animationController2(view, false)
                    }
                }

                false
            }
        }
    }

    private fun focusChangeListeners() {
        binding.apply {
            etSigninEmail.setOnFocusChangeListener { _, b ->
                dividerController = b
            }
            etSigninPw.setOnFocusChangeListener { _, b ->
                dividerController2 = b
            }
            etSignupName.setOnFocusChangeListener { _, b ->
                dividerController4 = b
            }
            etSignupEmail.setOnFocusChangeListener { _, b ->
                dividerController5 = b
            }
            etSignupPhone.setOnFocusChangeListener { _, b ->
                dividerController6 = b
            }
            etSignupPw.setOnFocusChangeListener { _, b ->
                dividerController7 = b
            }
            etSignupRePw.setOnFocusChangeListener { _, b ->
                dividerController8 = b
            }
        }
    }

    private fun observer() {
        viewModel.getTabSignin().observe(viewLifecycleOwner) {
            binding.tvSigninTabSignin.text = it
        }
        viewModel.getTabSignup().observe(viewLifecycleOwner) {
            binding.tvSigninTabSignup.text = it
        }
        viewModel.getSignInController().observe(viewLifecycleOwner) {
            when(it) {
                0, 3 -> {
                    showAlert("Lütfen e-posta veya şifrenizin doğru olduğundan emin olunuz.")
                    binding.signInProgressVisibility = false
                }
                2 -> {
                    viewModel.getUser()?.let { user ->
                        val subject = "Filozilla Üyelik Onaylama"
                        val body = "Merhaba ${user.fullName}, <a href=\"https://filozilla.com/webservices/update_status.php?token=${user.hashedEmail}\"><b>buraya tıklayarak</b></a> Filozilla üyeliğini tamamlayabilirsin. Hoşça kal!"
                        sendEmailRepo.sendEmail(email = user.email, fullName = user.fullName, subject = subject, body = body)
                    }?: showAlert(message = getString(R.string.send_email_failure))
                }
                1 -> {
                    viewModel.getUser()?.let { user ->
                        val strUser = Gson().toJson(user)
                        updatePrefMembershipInfo(strUser = strUser)
                    }
                }
            }
        }
        sendEmailRepo.getSendCheck().observe(viewLifecycleOwner) {
            when(it) {
                2, 3 -> showAlert(message = getString(R.string.send_email_failure))
                1 -> showAlert(message = getString(R.string.send_email_success))
            }

            binding.signInProgressVisibility = false
        }
        viewModel.getSignUpController().observe(viewLifecycleOwner) {
            when(it) {
                0, 2 -> {
                    showAlert(message = getString(R.string.error_sign_up))
                    binding.signUpProgressVisibility = false
                }
                1 -> {
                    clearInputs()
                    tabSigninOnClick()
                    showAlert(message = getString(R.string.success_sign_up))
                    binding.signUpProgressVisibility = false
                }
            }
        }
    }

    fun tabSigninOnClick() {
        if (binding.tabController!! && formController) {
            viewModel.tabSigninOnClick()
            binding.tabController = false
            setFormAnimation()
        }
    }

    fun tabSignupOnClick() {
        if (!binding.tabController!! && formController) {
            viewModel.tabSignupOnClick()
            binding.tabController = true
            setFormAnimation2()
        }
    }

    private fun setFormAnimation() {
        binding.linearSignupBody.visibility = View.GONE

        val translationX = ObjectAnimator.ofFloat(binding.linearSignupBody, "translationX", 20.0f, binding.clMembershipHolder.width.toFloat())
        val translationX2 = ObjectAnimator.ofFloat(binding.linearSigninBody, "translationX", -(binding.clMembershipHolder.width.toFloat() + 20.0f), 0.0f)

        AnimatorSet().apply {
            this.play(translationX)
            this.duration = 250
            this.doOnStart {
                formController = false
            }
            this.start()
        }

        binding.linearSigninBody.visibility = View.VISIBLE

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
        binding.linearSigninBody.visibility = View.GONE

        val translationX = ObjectAnimator.ofFloat(binding.linearSigninBody, "translationX", 20.0f, -binding.clMembershipHolder.width.toFloat())
        val translationX2 = ObjectAnimator.ofFloat(binding.linearSignupBody, "translationX", (binding.clMembershipHolder.width.toFloat() + 20.0f), 0.0f)

        AnimatorSet().apply {
            this.play(translationX)
            this.duration = 250
            this.doOnStart {
                formController = false
                binding.linearSignupBody.visibility = View.VISIBLE
            }
            this.start()
        }

        AnimatorSet().apply {
            this.play(translationX2)
            this.duration = 250
            this.start()
            this.doOnEnd {
                formController = true
            }
        }
    }

    fun rememberMeOnClick() {
        rememberMeController = if (rememberMeController) {
            binding.ivSigninRememberMe.setImageResource(R.drawable.check)
            false
        }else {
            binding.ivSigninRememberMe.setImageResource(R.drawable.check_fill)
            true
        }
    }

    fun forgotPwOnClick() {
        Navigation.findNavController(binding.tvSigninForgotPassword).navigate(R.id.signinToForgotPw)
    }

    fun signinOnClick(email: String, pw: String) {
        if (binding.clController!! && !binding.signInProgressVisibility!!) {
            binding.signInProgressVisibility = true
            signIn(email = email, pw = pw)
        }
    }

    fun googleOnClick() {

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    fun animationController(view: View, controller: Boolean) {
        if (!controller) {
            viewModel.viewAnimation(view)
            view.background = resources.getDrawable(R.drawable.card_social_bg2, null)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun animationController2(view: View, controller: Boolean) {
        if (!controller) {
            view.background = resources.getDrawable(R.drawable.card_social_bg, null)
        }
        if (controller && binding.clController!!) {
            viewModel.viewAnimation2(view)
        }else if (!controller) {
            viewModel.viewAnimation2(view)
        }
    }

    fun signupOnClick(fullName: String, email: String, phoneNumber: String, pw: String) {
        if (binding.clController2!! && !binding.signUpProgressVisibility!!) {
            binding.signUpProgressVisibility = true
            insert(fullName = fullName, email = email, phoneNumber = phoneNumber, pw = pw)
        }
    }

    fun pwVisibilityOnClick() {
        binding.apply {
            if (pwVisibilityController) {
                ivSigninPwVisibility.setImageResource(R.drawable.eye_close)
                etSigninPw.transformationMethod = PasswordTransformationMethod.getInstance()
                pwVisibilityController = false
            }else {
                ivSigninPwVisibility.setImageResource(R.drawable.eye)
                etSigninPw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                pwVisibilityController = true
            }

            etSigninPw.setSelection(etSigninPw.text.length)
        }
    }

    fun pwVisibility2OnClick() {
        binding.apply {
            if (pwVisibilityController2) {
                ivSignupPwVisibility.setImageResource(R.drawable.eye_close)
                etSignupPw.transformationMethod = PasswordTransformationMethod.getInstance()
                pwVisibilityController2 = false
            }else {
                ivSignupPwVisibility.setImageResource(R.drawable.eye)
                etSignupPw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                pwVisibilityController2 = true
            }

            etSignupPw.setSelection(etSignupPw.text.length)
        }
    }

    fun pwVisibility3OnClick() {
        binding.apply {
            if (pwVisibilityController3) {
                ivSignupPwVisibility2.setImageResource(R.drawable.eye_close)
                etSignupRePw.transformationMethod = PasswordTransformationMethod.getInstance()
                pwVisibilityController3 = false
            }else {
                ivSignupPwVisibility2.setImageResource(R.drawable.eye)
                etSignupRePw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                pwVisibilityController3 = true
            }

            etSignupRePw.setSelection(etSignupRePw.text.length)
        }
    }

    fun clearVisibilityOnClick() {
        if (binding.clearVisibility!!) {
            binding.etSigninEmail.text.clear()
        }
    }

    fun clearVisibility2OnClick() {
        if (binding.clearVisibility2!!) {
            binding.etSignupName.text.clear()
        }
    }

    fun clearVisibility3OnClick() {
        if (binding.clearVisibility3!!) {
            binding.etSignupEmail.text.clear()
        }
    }

    fun clearVisibility4OnClick() {
        if (binding.clearVisibility4!!) {
            binding.etSignupPhone.text.clear()
        }
    }

    private fun signIn(email: String, pw: String) {
        viewModel.signIn(email = email, pw = pw)
    }

    private fun updatePrefMembershipInfo(strUser: String) {
        requireContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit().run {
            clear()
            putInt("type", 1)
            putBoolean("controller", rememberMeController)
            putString("user", strUser)
            apply()
            toMainPage() // başka bir scope içine alabilirsin.
        }
    }

    private fun toMainPage() {
        binding.signInProgressVisibility = false

        Intent(requireContext(), MainActivity::class.java).apply {
            startActivity(this)
            requireActivity().finish()
        }
    }

    private fun clearInputs() {
        binding.etSignupName.text.clear()
        binding.etSignupEmail.text.clear()
        binding.etSignupPhone.text.clear()
        binding.etSignupPw.text.clear()
        binding.etSignupRePw.text.clear()
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(requireContext()).apply {
            this.setCancelable(false)
            this.setMessage(message)
            this.setPositiveButton(getString(R.string.okay)) { _, _ -> }
            this.create().show()
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(edit: Editable?) {
        CoroutineScope(Dispatchers.Main).launch {
            if (binding.tabController!!) signUpCheck() else signinCheck()
            this.cancel()
        }
    }

    private fun insert(fullName: String, email: String, phoneNumber: String, pw: String) {
        viewModel.signUp(fullName = fullName, email = email, phoneNumber = phoneNumber, pw = pw)
    }

    fun facebookOnClick() {
        toIntentPage(link = "https://www.facebook.com")
    }

    fun twitterOnClick() {
        toIntentPage(link = "https://www.twitter.com")
    }

    fun youtubeOnClick() {
        toIntentPage(link = "https://www.youtube.com")
    }

    fun linkedinOnClick() {
        toIntentPage(link = "https://www.linkedin.com")
    }

    private fun toIntentPage(link: String) {
        Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply {
            requireActivity().startActivity(this)
        }
    }

}