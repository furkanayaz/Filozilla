package com.filozilla.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.filozilla.R
import com.filozilla.databinding.PaymentMethodOptionDialogBinding
import com.filozilla.models.Payment

class PaymentMethodAdapter(var context: Context, private var paymentList: List<Payment>): RecyclerView.Adapter<PaymentMethodAdapter.CardViewDesignThingsHolder>() {

    companion object {
        val optionDialogController = MutableLiveData(0)
        var payment: Payment? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewDesignThingsHolder {
        return CardViewDesignThingsHolder(view = LayoutInflater.from(context).inflate(R.layout.row_payment_method, parent, false))
    }

    override fun getItemCount(): Int {
        return paymentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardViewDesignThingsHolder, position: Int) {
        val paymentObj = paymentList[position]

        if (paymentObj.number.first() == '6') {
            holder.logo?.setImageResource(R.drawable.discover)
        }else if (paymentObj.number.first() == '5') {
            holder.logo?.setImageResource(R.drawable.mastercard)
        }else if (paymentObj.number.first() == '4') {
            holder.logo?.setImageResource(R.drawable.visa)
        }else if (paymentObj.number.first() == '3') {
            holder.logo?.setImageResource(R.drawable.amex)
        }else if (paymentObj.number.first() == '2') {
            holder.logo?.setImageResource(R.drawable.mastercard)
        }else {
            holder.logo?.setImageResource(R.drawable.creditcard)
        }

        holder.number?.text = paymentObj.number
        holder.expire?.text = "Sona erme: ${paymentObj.expiryDate}"

        holder.holders?.setOnClickListener {
            showOptionDialog(payment = paymentObj)
        }
    }

    @SuppressLint("InflateParams")
    private fun showOptionDialog(payment: Payment) {
        PaymentMethodAdapter.payment = payment
        val dialogBinding = PaymentMethodOptionDialogBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(dialogBinding.root)
        val alertDialog = builder.create()
        alertDialog.show()

        dialogBinding.apply {
            tvPaymentMethodOptionEdit.setOnClickListener {
                optionDialogController.value = 1
                alertDialog.dismiss()
            }
            tvPaymentMethodOptionDelete.setOnClickListener {
                optionDialogController.value = 2
                alertDialog.dismiss()
            }
            tvPaymentMethodOptionClose.setOnClickListener {
                alertDialog.dismiss()
            }
        }
    }

    inner class CardViewDesignThingsHolder(view: View): RecyclerView.ViewHolder(view) {
        var holders: ConstraintLayout? = null
        var logo: ImageView? = null
        var number: TextView? = null
        var expire: TextView? = null

        init {
            holders = view.findViewById(R.id.clRowPaymentMethodHolders)
            logo = view.findViewById(R.id.ivRowPaymentMethodLogo)
            number = view.findViewById(R.id.tvRowPaymentMethodNumber)
            expire = view.findViewById(R.id.tvRowPaymentMethodExpire)
        }
    }
}