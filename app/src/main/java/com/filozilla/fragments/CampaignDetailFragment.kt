package com.filozilla.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.filozilla.R
import com.filozilla.databinding.FragmentCampaignDetailBinding
import com.filozilla.models.Campaign
import com.filozilla.viewmodels.CampaignDetailFragmentVM
import com.squareup.picasso.Picasso

class CampaignDetailFragment : Fragment() {
    private lateinit var binding: FragmentCampaignDetailBinding
    private lateinit var viewModel: CampaignDetailFragmentVM
    private var campaign: Campaign? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: CampaignDetailFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCampaignDetailBinding.inflate(inflater)
        binding.apply {
            fragmentCampaignDetailObject = this@CampaignDetailFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startAnimation()
        getInformation()
        setInformation()
        initComponents()
        touchListeners()
    }

    private fun startAnimation() {
        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.fade
        )
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    private fun getInformation() {
        val bundle : CampaignDetailFragmentArgs by navArgs()
        campaign = bundle.campaign
    }

    private fun setInformation() {
        campaign?.let { model ->
            binding.apply {
                Picasso.get()
                    .load(Uri.parse(model.imageLink))
                    .into(ivCampaignDetail)
            }
        }
    }

    private fun initComponents() {
        campaign?.let { campaign ->
            binding.apply {
                tvCampaignDetailType.text = campaign.type
                tvCampaignDetailTitle.text = campaign.title
                tvCampaignDetailDesc.text = campaign.desc
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun touchListeners() {
        binding.cvCampaignDetail.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> viewModel.viewAnimation(view = view)
                MotionEvent.ACTION_UP and MotionEvent.ACTION_CANCEL -> viewModel.viewAnimation2(view = view)
            }

            false
        }
    }

    fun backOnClick() {

    }

    fun campaignDetailOnClick() {

    }

}