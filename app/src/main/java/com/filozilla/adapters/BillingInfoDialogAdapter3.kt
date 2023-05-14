package com.filozilla.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.filozilla.R
import com.filozilla.models.County

@SuppressLint("InflateParams")
class BillingInfoDialogAdapter3(private val context: Context, private val list: List<County>): RecyclerView.Adapter<BillingInfoDialogAdapter3.CardViewDesignThingsHolder>() {
    val clickedText = MutableLiveData<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewDesignThingsHolder {
        return CardViewDesignThingsHolder(view = LayoutInflater.from(context).inflate(R.layout.row_text, null, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardViewDesignThingsHolder, position: Int) {
        val text = list[position]

        holder.tvRowText?.text = text.county

        holder.tvRowText?.setOnClickListener {
            clickedText.value = (it as TextView).text.toString().trim()
        }
    }

    inner class CardViewDesignThingsHolder(view: View): RecyclerView.ViewHolder(view) {
        var tvRowText: TextView? = null

        init {
            tvRowText = view.findViewById(R.id.tvRowText)
        }
    }
}