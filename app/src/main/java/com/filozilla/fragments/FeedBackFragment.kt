package com.filozilla.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.filozilla.R
import com.filozilla.activities.MainActivity
import com.filozilla.databinding.FragmentFeedbackBinding
import com.filozilla.interfaces.CommonInterface
import com.filozilla.repositories.ViewChangesRepo
import com.filozilla.viewmodels.FeedBackFragmentVM

class FeedBackFragment: Fragment(), CommonInterface {
    private lateinit var binding: FragmentFeedbackBinding
    private lateinit var viewModel: FeedBackFragmentVM
    private lateinit var viewChangesRepo: ViewChangesRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: FeedBackFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFeedbackBinding.inflate(layoutInflater, container, false)
        binding.fragmentFeedBackObject = this
        binding.sadController = false
        binding.confusedController = false
        binding.happyController = false

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
        initRepos()
        changedListeners()
        touchListeners()
        observer()
    }

    override fun initializeComponents() {

    }

    private fun initRepos() {
        viewChangesRepo.changeStatusBarColor(color = R.color.green2)
    }

    private fun changedListeners() {
        binding.etFeedBack.addTextChangedListener { binding.clearVisibilityController = it.toString().trim().isNotEmpty() }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun touchListeners() {

    }

    private fun observer() {
        viewModel.getFeedBackController().observe(viewLifecycleOwner) {
            when(it) {
                0, 2 -> showToastMsg(msg = getString(R.string.error_send_feedback))
                1 -> {
                    showToastMsg(msg = getString(R.string.success_send_feedback))
                    clearInterface()
                }
            }
        }
    }

    fun backOnClick() {
        viewChangesRepo.changeStatusBarColor(color = R.color.second_color)

    }

    fun sadOnClick() {
        onSadController()
    }

    fun confusedOnClick() {
        onConfusedController()
    }

    fun happyOnClick() {
        onHappyController()
    }

    private fun onSadController() {
        binding.sadController = !binding.sadController!!
        binding.confusedController = false
        binding.happyController = false
        viewModel.viewAnimation(binding.ivFeedBackSad)
    }

    private fun onConfusedController() {
        binding.confusedController = !binding.confusedController!!
        binding.sadController = false
        binding.happyController = false
        viewModel.viewAnimation(binding.ivFeedBackConfused)
    }

    private fun onHappyController() {
        binding.happyController = !binding.happyController!!
        binding.sadController = false
        binding.confusedController = false
        viewModel.viewAnimation(binding.ivFeedBackHappy)
    }

    fun clearOnClick() = binding.etFeedBack.text.clear()

    fun sendOnClick(sadController: Boolean, confusedController: Boolean, happyController: Boolean, message: String) {
        val faceController = if (sadController) 1 else if (confusedController) 2 else if (happyController) 3 else 0

        MainActivity.user?.let { user ->
            if (faceController != 0 && message.length >= 10) {
                sendMail(email = user.email, fullName = user.fullName, faceController = faceController, msg = message)
            }else {
                if (faceController == 0)
                    showToastMsg(msg = getString(R.string.error_feedback_status))
                if (message.length < 10)
                    binding.etFeedBack.error = getString(R.string.error_feed_back)
            }
        }?: showToastMsg(msg = getString(R.string.error_account))
    }

    private fun sendMail(email: String, fullName: String, faceController: Int, msg: String) {
        viewModel.sendMail(email = email, fullName = fullName, faceController = faceController, msg = msg)
    }

    private fun clearInterface() {
        binding.sadController = false
        binding.confusedController = false
        binding.happyController = false
        binding.etFeedBack.text.clear()
    }

    private fun showToastMsg(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

}