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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.filozilla.R
import com.filozilla.fragments.CampaignsFragmentDirections
import com.filozilla.models.Campaign
import com.squareup.picasso.Picasso
import androidx.core.util.*
import androidx.navigation.fragment.FragmentNavigatorExtras

class FilozillaCampaignsAdapter(private val context: Context, private val filozillaCampaignList: List<Campaign>): RecyclerView.Adapter<FilozillaCampaignsAdapter.CardViewDesignThingsHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewDesignThingsHolder {
        return CardViewDesignThingsHolder(LayoutInflater.from(context).inflate(R.layout.row_filozilla_campaigns, parent, false))
    }

    override fun onBindViewHolder(holder: CardViewDesignThingsHolder, position: Int) {
        val campaign = filozillaCampaignList[position]

        touchListeners(holder.cvFilozillaRowCampaign)

        Picasso.get()
            .load(Uri.parse(campaign.imageLink))
            .into(holder.ivFilozillaRowCampaign)

        holder.tvFilozillaRowCampaignsTitle?.text = campaign.title

        holder.cvFilozillaRowCampaign?.setOnClickListener {
            val extras = FragmentNavigatorExtras(holder.ivFilozillaRowCampaign!! to holder.ivFilozillaRowCampaign!!.transitionName)
            val id = CampaignsFragmentDirections.campaignToCampaignDetail(campaign = campaign)
            Navigation.findNavController(holder.cvFilozillaRowCampaign!!).navigate(directions = id, navigatorExtras = extras)
        }
    }

    override fun getItemCount(): Int {
        return filozillaCampaignList.size
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
        var cvFilozillaRowCampaign: CardView? = null
        var ivFilozillaRowCampaign: ImageView? = null
        var tvFilozillaRowCampaignsTitle: TextView? = null

        init {
            cvFilozillaRowCampaign = view.findViewById(R.id.cvFilozillaRowCampaign)
            ivFilozillaRowCampaign = view.findViewById(R.id.ivFilozillaRowCampaign)
            tvFilozillaRowCampaignsTitle = view.findViewById(R.id.tvFilozillaRowCampaignsTitle)
        }
    }
}