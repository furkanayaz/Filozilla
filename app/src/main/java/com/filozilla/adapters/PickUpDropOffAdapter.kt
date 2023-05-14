package com.filozilla.adapters

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.filozilla.R
import com.filozilla.datatransfers.PickUpDropOffTransfer
import com.filozilla.interfaces.CommonVMInterface
import com.filozilla.models.Place

class PickUpDropOffAdapter(private var context: Context, private var placeList: List<Place>): RecyclerView.Adapter<PickUpDropOffAdapter.CardViewDesignThingsHolder>(), CommonVMInterface {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewDesignThingsHolder {
        return CardViewDesignThingsHolder(LayoutInflater.from(context).inflate(R.layout.row_place, parent, false))
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: CardViewDesignThingsHolder, position: Int) {
        val place = placeList[position]

        holder.tvRowPlaceTitle?.text = "${place.province.uppercase()} - ${place.district}"
        holder.tvRowPlaceDesc?.text = "${place.neighborhood} ${place.road} ${place.street}"


        holder.clRowPlaceHolder?.setOnTouchListener { view, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewAnimation(view)
                }
                MotionEvent.ACTION_UP -> {
                    viewAnimation2(view)
                }
                MotionEvent.ACTION_CANCEL -> {
                    viewAnimation2(view)
                }
            }

            false
        }

        holder.clRowPlaceHolder?.setOnClickListener {
            if (PickUpDropOffTransfer.controller == 1) {
                PickUpDropOffTransfer.location = holder.tvRowPlaceTitle?.text.toString()
            }
            if (PickUpDropOffTransfer.controller == 2) {
                PickUpDropOffTransfer.location2 = holder.tvRowPlaceTitle?.text.toString()
            }
            if (PickUpDropOffTransfer.controller == 3) {
                PickUpDropOffTransfer.location3 = holder.tvRowPlaceTitle?.text.toString()
            }
            if (PickUpDropOffTransfer.controller == 4) {
                PickUpDropOffTransfer.location4 = holder.tvRowPlaceTitle?.text.toString()
            }

            Navigation.findNavController(holder.clRowPlaceHolder!!).navigate(R.id.pickUpToSearch)
        }
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    inner class CardViewDesignThingsHolder(view: View): RecyclerView.ViewHolder(view) {
        var clRowPlaceHolder: ConstraintLayout? = null
        var tvRowPlaceTitle: TextView? = null ; var tvRowPlaceDesc: TextView? = null

        init {
            clRowPlaceHolder = view.findViewById(R.id.clRowPlaceHolder)
            tvRowPlaceTitle = view.findViewById(R.id.tvRowPlaceTitle)
            tvRowPlaceDesc = view.findViewById(R.id.tvRowPlaceDesc)
        }
    }

    override fun viewAnimation(view: View) {
        ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.5f).apply {
            this.duration = 150
            this.start()
        }
    }

    override fun viewAnimation2(view: View) {
        ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1.0f).apply {
            this.duration = 150
            this.start()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterItems(filterPlaceList: List<Place>) {
        placeList = filterPlaceList
        notifyDataSetChanged()
    }

}