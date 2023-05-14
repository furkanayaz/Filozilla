package com.filozilla.fragments

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.filozilla.R
import com.filozilla.activities.MainActivity
import com.filozilla.adapters.PaymentMethodAdapter
import com.filozilla.databinding.FragmentPaymentMethodBinding
import com.filozilla.databinding.NewPaymentMethodDialogBinding
import com.filozilla.interfaces.CommonInterface
import com.filozilla.models.Payment
import com.filozilla.rooms.RoomDB
import com.filozilla.viewmodels.PaymentMethodFragmentVM
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class PaymentMethodFragment: Fragment(), CommonInterface {
    private lateinit var binding: FragmentPaymentMethodBinding
    private lateinit var viewModel: PaymentMethodFragmentVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: PaymentMethodFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPaymentMethodBinding.inflate(inflater, null, false)
        binding.apply {
            fragmentPaymentMethodObject = this@PaymentMethodFragment
            emptyController = false
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
        touchListeners()
    }

    override fun initializeComponents() {
        getPaymentMethods()
        observer()
        initRoom()
        initRv()
    }

    override fun touchListeners() {

    }

    private fun getPaymentMethods() {
        MainActivity.user?.let { user ->
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getItems(i_id = user.iId)
                this.cancel()
            }
        }
    }

    private fun observer() {
        viewModel.getPaymentList().observe(viewLifecycleOwner) {
            binding.rvPaymentMethod.adapter = PaymentMethodAdapter(context = requireContext(), paymentList = it)
            binding.emptyController = it.isEmpty()
        }

        PaymentMethodAdapter.run {
            optionDialogController.observe(viewLifecycleOwner) { controller ->
                payment?.let { payment ->
                    when(controller) {
                        1 -> {
                            showNewPaymentMethodDialog(payment = payment)
                            optionDialogController.value = 0
                        }
                        2 -> {
                            viewModel.deleteItem(payment = payment)
                            optionDialogController.value = 0
                        }
                    }
                }
            }
        }
    }

    private fun initRoom() {
        RoomDB.accessDatabase(context = requireContext())
    }

    private fun initRv() {
        binding.rvPaymentMethod.run {
            this.hasFixedSize()
            this.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    fun backOnClick() {

    }

    fun addOnClick() = showNewPaymentMethodDialog(payment = null)

    @SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility", "InflateParams")
    private fun showNewPaymentMethodDialog(payment: Payment?) {
        val dialogBinding = NewPaymentMethodDialogBinding.inflate(LayoutInflater.from(requireContext()))

        var current = ""
        val nonDigits = Regex("\\D")

        dialogBinding.apply {
            root.minHeight = Resources.getSystem().displayMetrics.heightPixels

            ivPaymentMethodFullnameClear.setOnClickListener { etPaymentMethodFullname.text.clear() }
            ivPaymentMethodNumberClear.setOnClickListener { etPaymentMethodCardNumber.text.clear() }
            ivPaymentMethodExpiryDateClear.setOnClickListener { etPaymentMethodExpireDate.text.clear() }
            ivPaymentMethodSecurityCodeClear.setOnClickListener { etPaymentMethodSecurityCode.text.clear() }

            payment?.let {
                etPaymentMethodFullname.setText(it.fullName)
                etPaymentMethodCardNumber.setText(it.number)
                etPaymentMethodExpireDate.setText(it.expiryDate)
                etPaymentMethodSecurityCode.setText(it.securityCode.toString())

                ivPaymentMethodFullnameClear.visibility = if (it.fullName.isNotEmpty()) { View.VISIBLE } else { View.GONE }
                ivPaymentMethodNumberClear.visibility = if (it.number.isNotEmpty()) { View.VISIBLE } else { View.GONE }
                ivPaymentMethodExpiryDateClear.visibility = if (it.expiryDate.isNotEmpty()) { View.VISIBLE } else { View.GONE }
                ivPaymentMethodSecurityCodeClear.visibility = if (it.securityCode.toString().isNotEmpty()) { View.VISIBLE } else { View.GONE }
            }

            etPaymentMethodFullname.setOnFocusChangeListener { _, b ->
                if (b) {
                    etPaymentMethodFullname.background = requireContext().getDrawable(R.drawable.et_bg13)
                }else {
                    etPaymentMethodFullname.background = requireContext().getDrawable(R.drawable.et_bg12)
                }
            }

            etPaymentMethodCardNumber.setOnFocusChangeListener { _, b ->
                if (b) {
                    etPaymentMethodCardNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.cc_logo_2, 0, 0, 0)
                    etPaymentMethodCardNumber.background = requireContext().getDrawable(R.drawable.et_bg13)
                }else {
                    etPaymentMethodCardNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.cc_logo, 0, 0, 0)
                    etPaymentMethodCardNumber.background = requireContext().getDrawable(R.drawable.et_bg12)
                }
            }

            etPaymentMethodExpireDate.setOnFocusChangeListener { _, b ->
                if (b) {
                    etPaymentMethodExpireDate.background = requireContext().getDrawable(R.drawable.et_bg13)
                }else {
                    etPaymentMethodExpireDate.background = requireContext().getDrawable(R.drawable.et_bg12)
                }
            }

            etPaymentMethodSecurityCode.setOnFocusChangeListener { _, b ->
                if (b) {
                    etPaymentMethodSecurityCode.background = requireContext().getDrawable(R.drawable.et_bg13)
                }else {
                    etPaymentMethodSecurityCode.background = requireContext().getDrawable(R.drawable.et_bg12)
                }
            }

            etPaymentMethodFullname.addTextChangedListener {
                val controller = it.toString().trim().isNotEmpty()
                ivPaymentMethodFullnameClear.visibility = if (controller) {
                    View.VISIBLE
                }else {
                    View.GONE
                }
            }

            etPaymentMethodCardNumber.addTextChangedListener {
                val controller = it.toString().trim().isNotEmpty()
                ivPaymentMethodNumberClear.visibility = if (controller) {
                    View.VISIBLE
                }else {
                    View.GONE
                }
                if (it.toString() != current) {
                    val userInput = it.toString().replace(nonDigits,"")
                    if (userInput.length <= 16) {
                        current = userInput.chunked(4).joinToString(" ")
                        it!!.filters = arrayOfNulls<InputFilter>(0)
                    }
                    it!!.replace(0, it.length, current, 0, current.length)
                }
            }

            etPaymentMethodExpireDate.addTextChangedListener {
                val controller = it.toString().trim().isNotEmpty()
                ivPaymentMethodExpiryDateClear.visibility = if (controller) {
                    View.VISIBLE
                }else {
                    View.GONE
                }
            }

            etPaymentMethodSecurityCode.addTextChangedListener {
                val controller = it.toString().trim().isNotEmpty()
                ivPaymentMethodSecurityCodeClear.visibility = if (controller) {
                    View.VISIBLE
                }else {
                    View.GONE
                }
            }
        }

        val sheet = BottomSheetDialog(requireContext()).also {
            it.setCancelable(false)
            it.behavior.isDraggable = false
            it.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            it.setContentView(view = dialogBinding.root)
            it.show()
        }

        dialogBinding.apply {
            ivNewPaymentMethodClose.setOnClickListener {
                sheet.dismiss()
            }

            cvPaymentMethodSave.setOnTouchListener { _, motionEvent ->
                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> viewModel.viewAnimation(view = cvPaymentMethodSave)
                    MotionEvent.ACTION_UP -> viewModel.viewAnimation2(view = cvPaymentMethodSave)
                    MotionEvent.ACTION_CANCEL -> viewModel.viewAnimation2(view = cvPaymentMethodSave)
                }
                false
            }

            cvPaymentMethodSave.setOnClickListener {
                val fullName = etPaymentMethodFullname.text.toString().trim()
                val number = etPaymentMethodCardNumber.text.toString().trim()
                val expiryDate = etPaymentMethodExpireDate.text.toString().trim()
                val securityCode = etPaymentMethodSecurityCode.text.toString().trim()

                if (fullName.length >= 5 && number.length >= 15 && expiryDate.length == 5 && securityCode.length == 3) {
                    MainActivity.user?.let { user ->
                        if (payment == null) {
                            viewModel.insertItem(payment = Payment(id= 0, iId = user.iId, fullName = fullName, number = number, expiryDate = expiryDate, securityCode = securityCode.toInt()))
                        }else {
                            viewModel.updateItem(payment = Payment(id= payment.id, iId = user.iId, fullName = fullName, number = number, expiryDate = expiryDate, securityCode = securityCode.toInt()))
                        }
                    }
                    sheet.dismiss()
                }

                if (fullName.length < 5) {
                    ivPaymentMethodFullnameClear.visibility = View.GONE
                    etPaymentMethodFullname.error = getString(R.string.payment_fullname_error)
                }
                if (number.length < 15) {
                    ivPaymentMethodNumberClear.visibility = View.GONE
                    etPaymentMethodCardNumber.error = getString(R.string.payment_number_error)
                }
                if (expiryDate.length < 5) {
                    ivPaymentMethodExpiryDateClear.visibility = View.GONE
                    etPaymentMethodExpireDate.error = getString(R.string.payment_expirydate_error)
                }
                if (securityCode.length < 3) {
                    ivPaymentMethodSecurityCodeClear.visibility = View.GONE
                    etPaymentMethodSecurityCode.error = getString(R.string.payment_securitycode_error)
                }
            }
        }
    }

}