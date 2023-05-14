package com.filozilla.adapters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.filozilla.R

class HomePageAdapter(private var context: Context): RecyclerView.Adapter<HomePageAdapter.CardViewDesignThingsHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewDesignThingsHolder {
        return CardViewDesignThingsHolder(LayoutInflater.from(context).inflate(R.layout.row_sample_find_car, parent, false))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: CardViewDesignThingsHolder, position: Int) {
        holder.clRow?.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewAnimation(view = view)
                }
                MotionEvent.ACTION_UP -> {
                    viewAnimation2(view = view)
                }
                MotionEvent.ACTION_CANCEL -> {
                    viewAnimation2(view = view)
                }
            }

            false
        }

        holder.clRow?.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    @SuppressLint("Recycle")
    fun viewAnimation(view: View) {
        val objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.50f)

        AnimatorSet().apply {
            this.play(objectAnimator)
            this.duration = 150
            this.start()
        }
    }

    fun viewAnimation2(view: View) {
        val objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.50f, 1.0f)

        AnimatorSet().apply {
            this.play(objectAnimator)
            this.duration = 150
            this.start()
        }
    }

    inner class CardViewDesignThingsHolder(view: View): RecyclerView.ViewHolder(view) {
        var clRow: ConstraintLayout? = null
        private var tvRowHeadName: TextView? = null ; private var tvRowHeadSimilar: TextView? = null
        private var clRowHeadCampaign: ConstraintLayout? = null
        private var tvDetailGear: TextView? = null ; private var tvDetailType: TextView? = null ; private var tvDetailFuel: TextView? = null ; private var tvDetailSeat: TextView? = null
        private var tvDetailDeliveryType: TextView? = null ; private var tvDetailDepositFee: TextView? = null ; private var tvDetailKMLimit: TextView? = null
        private var ivBottomFleet: ImageView? = null
        private var tvBottomRating: TextView? = null
        private var ivBottomRating: ImageView? = null ; private var ivBottomRating2: ImageView? = null ; private var ivBottomRating3: ImageView? = null ; private var ivBottomRating4: ImageView? = null ; private var ivBottomRating5: ImageView? = null
        private var tvBottomComment: TextView? = null
        private var tvBottomTotalPrice: TextView? = null ; private var tvBottomDailyPrice: TextView? = null
        private var clRowBottomClick: ConstraintLayout? = null

        init {
            clRow = view.findViewById(R.id.clRowSampleFindCarHolder)
            tvRowHeadName = view.findViewById(R.id.tvRowHeadName)
            tvRowHeadSimilar = view.findViewById(R.id.tvRowHeadSimilar)
            clRowHeadCampaign = view.findViewById(R.id.clRowHeadCampaign)
            tvDetailGear = view.findViewById(R.id.tvDetailGear)
            tvDetailType = view.findViewById(R.id.tvDetailType)
            tvDetailFuel = view.findViewById(R.id.tvDetailFuel)
            tvDetailSeat = view.findViewById(R.id.tvDetailSeat)
            tvDetailDeliveryType = view.findViewById(R.id.tvDetailDeliveryType)
            tvDetailDepositFee = view.findViewById(R.id.tvDetailDepositFee)
            tvDetailKMLimit = view.findViewById(R.id.tvDetailKMLimit)
            ivBottomFleet = view.findViewById(R.id.ivBottomFleet)
            tvBottomRating = view.findViewById(R.id.tvBottomRating)
            ivBottomRating = view.findViewById(R.id.ivBottomRating)
            ivBottomRating2 = view.findViewById(R.id.ivBottomRating2)
            ivBottomRating3 = view.findViewById(R.id.ivBottomRating3)
            ivBottomRating4 = view.findViewById(R.id.ivBottomRating4)
            ivBottomRating5 = view.findViewById(R.id.ivBottomRating5)
            tvBottomComment = view.findViewById(R.id.tvBottomComment)
            tvBottomTotalPrice = view.findViewById(R.id.tvBottomTotalPrice)
            tvBottomDailyPrice = view.findViewById(R.id.tvBottomDailyPrice)
            clRowBottomClick = view.findViewById(R.id.clRowBottomClick)
        }

    }
}