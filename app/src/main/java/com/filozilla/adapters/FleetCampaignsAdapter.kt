package com.filozilla.adapters

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.filozilla.R
import com.filozilla.models.Campaign
import com.squareup.picasso.Picasso

class FleetCampaignsAdapter(private val context: Context, private val fleetCampaignList: List<Campaign>): RecyclerView.Adapter<FleetCampaignsAdapter.CardViewDesignThingsHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewDesignThingsHolder {
        return CardViewDesignThingsHolder(LayoutInflater.from(context).inflate(R.layout.row_fleet_campaign, parent, false))
    }

    override fun onBindViewHolder(holder: CardViewDesignThingsHolder, position: Int) {
        val campaign = fleetCampaignList[position]

        touchListeners(holder.cvFleetRowCampaign)

        Picasso.get()
            .load(Uri.parse(campaign.imageLink))
            .into(holder.ivFleetRowCampaign)

        holder.tvFleetRowCampaignTitle?.text = campaign.title
        holder.tvFleetRowCampaignDesc?.text = campaign.desc

        holder.cvFleetRowCampaign?.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return fleetCampaignList.size
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun touchListeners(view: CardView?) {
        view?.setOnTouchListener { _, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.25f).apply {
                        this.duration = 25
                        this.start()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    ObjectAnimator.ofFloat(view, "alpha", 0.25f, 1.0f).apply {
                        this.duration = 25
                        this.start()
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    ObjectAnimator.ofFloat(view, "alpha", 0.25f, 1.0f).apply {
                        this.duration = 25
                        this.start()
                    }
                }
            }

            false
        }
    }

    inner class CardViewDesignThingsHolder(view: View): RecyclerView.ViewHolder(view) {
        var cvFleetRowCampaign: CardView? = null
        var ivFleetRowCampaign: ImageView? = null
        var tvFleetRowCampaignTitle: TextView? = null ; var tvFleetRowCampaignDesc: TextView? = null

        init {
            cvFleetRowCampaign = view.findViewById(R.id.cvFleetRowCampaign)
            ivFleetRowCampaign = view.findViewById(R.id.ivFleetRowCampaign)
            tvFleetRowCampaignTitle = view.findViewById(R.id.tvFleetRowCampaignTitle)
            tvFleetRowCampaignDesc = view.findViewById(R.id.tvFleetRowCampaignDesc)
        }
    }
}