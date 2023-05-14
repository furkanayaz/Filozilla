package com.filozilla.adapters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.filozilla.R
import com.filozilla.interfaces.CommonVMInterface
import com.filozilla.models.SearchCar
import com.squareup.picasso.Picasso

class SearchCarAdapter(private var context: Context, private var searchCarList: List<SearchCar>): RecyclerView.Adapter<SearchCarAdapter.CardViewDesignThingsHolder>(), CommonVMInterface {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewDesignThingsHolder {
        return CardViewDesignThingsHolder(LayoutInflater.from(context).inflate(R.layout.row_find_car, parent, false))
    }

    override fun getItemCount(): Int {
        return searchCarList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardViewDesignThingsHolder, position: Int) {
        var imgPosition = 0

        val searchCar = searchCarList[position]

        setOnTouchListeners(holder.clRowFindCarHolders!!)

        holder.clRowFindCarHolders?.setOnClickListener {

        }

        setCarImage(imgPosition = imgPosition, carImage = searchCar.carImage, ivRowFindCarImage = holder.ivRowFindCarImage!!)

        holder.ivRowFindCarConvert!!.setOnClickListener {
            imgPosition++

            if (imgPosition < searchCar.carImage.size)
                setCarImage(imgPosition = imgPosition, carImage = searchCar.carImage, ivRowFindCarImage = holder.ivRowFindCarImage!!)
            else
                imgPosition = 0
        }

        holder.tvRowFindCarSeat!!.text = "${searchCar.seat} Koltuk"
        holder.tvRowFindCarGear!!.text = searchCar.gear
        holder.tvRowFindCarFuel!!.text = searchCar.fuel
        holder.tvRowFindCarType!!.text = searchCar.carType
        holder.tvRowFindCarDetailTitle!!.text = searchCar.carName
        holder.tvRowFindCarDeliveryResult!!.text = searchCar.deliveryType
        holder.tvRowFindCarDepositResult!!.text = "₺ ${searchCar.deposit}"
        holder.tvRowFindCarKmResult!!.text = "${searchCar.km} KM"
        holder.rbRowFindCar!!.progress = searchCar.rating.toInt()
        holder.tvRowFindCarComments!!.text = "${searchCar.comments} Yorum"
        holder.tvRowFindCarRentalMonthly!!.text = "${searchCar.chosenDay} Günlük:"
        holder.tvRowFindCarRentalDailyPrice!!.text = "₺ ${searchCar.dailyPrice}"
        holder.tvRowFindCarRentalMonthlyPrice!!.text = "₺ ${searchCar.chosenPrice}"
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchListeners(view: ConstraintLayout) {
        view.setOnTouchListener { _, event ->
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
    }

    private fun setCarImage(imgPosition: Int, carImage: List<String>, ivRowFindCarImage: ImageView) {
        Picasso
            .get()
            .load(Uri.parse(carImage[imgPosition]))
            .into(ivRowFindCarImage)
    }

    inner class CardViewDesignThingsHolder(view: View): RecyclerView.ViewHolder(view) {
        var clRowFindCarHolders: ConstraintLayout? = null
        var ivRowFindCarImage: ImageView? = null
        var ivRowFindCarConvert: ImageView? = null
        var tvRowFindCarSeat: TextView? = null ; var tvRowFindCarGear: TextView? = null ; var tvRowFindCarFuel: TextView? = null ; var tvRowFindCarType: TextView? = null
        var tvRowFindCarDetailTitle: TextView? = null ; var tvRowFindCarDeliveryResult: TextView? = null ; var tvRowFindCarDepositResult: TextView? = null ; var tvRowFindCarKmResult: TextView? = null
        var ivRowFindCarCompany: ImageView? = null
        var rbRowFindCar: RatingBar? = null
        var tvRowFindCarComments: TextView? = null
        var tvRowFindCarRentalMonthly: TextView? = null
        var tvRowFindCarRentalDailyPrice: TextView? = null ; var tvRowFindCarRentalMonthlyPrice: TextView? = null
        var clRowFindCarRent: ConstraintLayout? = null

        init {
            clRowFindCarHolders = view.findViewById(R.id.clRowFindCarHolders)
            ivRowFindCarImage = view.findViewById(R.id.ivRowFindCarImage)
            ivRowFindCarConvert = view.findViewById(R.id.ivRowFindCarConvert)
            tvRowFindCarSeat = view.findViewById(R.id.tvRowFindCarSeat)
            tvRowFindCarGear = view.findViewById(R.id.tvRowFindCarGear)
            tvRowFindCarFuel = view.findViewById(R.id.tvRowFindCarFuel)
            tvRowFindCarType = view.findViewById(R.id.tvRowFindCarType)
            tvRowFindCarDetailTitle = view.findViewById(R.id.tvRowFindCarDetailTitle)
            tvRowFindCarDeliveryResult = view.findViewById(R.id.tvRowFindCarDeliveryResult)
            tvRowFindCarDepositResult = view.findViewById(R.id.tvRowFindCarDepositResult)
            tvRowFindCarKmResult = view.findViewById(R.id.tvRowFindCarKmResult)
            ivRowFindCarCompany = view.findViewById(R.id.ivRowFindCarCompany)
            rbRowFindCar = view.findViewById(R.id.rbRowFindCar)
            tvRowFindCarComments = view.findViewById(R.id.tvRowFindCarComments)
            tvRowFindCarRentalMonthly = view.findViewById(R.id.tvRowFindCarRentalMonthly)
            tvRowFindCarRentalDailyPrice = view.findViewById(R.id.tvRowFindCarRentalDailyPrice)
            tvRowFindCarRentalMonthlyPrice = view.findViewById(R.id.tvRowFindCarRentalMonthlyPrice)
            clRowFindCarRent = view.findViewById(R.id.clRowFindCarRent)
        }
    }

    override fun viewAnimation(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.97f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.97f)
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.80f)

        AnimatorSet().run {
            play(scaleX).with(scaleY).with(alpha)
            duration = 150
            start()
        }
    }

    override fun viewAnimation2(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.97f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.97f, 1.0f)
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 0.80f, 1.0f)

        AnimatorSet().run {
            play(scaleX).with(scaleY).with(alpha)
            duration = 150
            start()
        }
    }

}