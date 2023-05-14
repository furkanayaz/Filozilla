package com.filozilla.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.filozilla.adapters.FilozillaCampaignsAdapter
import com.filozilla.adapters.FleetCampaignsAdapter
import com.filozilla.databinding.FragmentCampaignsBinding
import com.filozilla.interfaces.CommonInterface
import com.filozilla.models.Campaign
import com.filozilla.viewmodels.CampaignsFragmentVM

class CampaignsFragment: Fragment(), CommonInterface {
    private lateinit var binding: FragmentCampaignsBinding
    private lateinit var viewModel: CampaignsFragmentVM
    private lateinit var fleetCampaignList: ArrayList<Campaign>
    private lateinit var filozillaCampaignList: ArrayList<Campaign>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCampaignsBinding.inflate(layoutInflater, container, false)
        binding.apply {
            fragmentCampaignsObject = this@CampaignsFragment
            filozillaCampaignsController = false
            fleetCampaignsController = false
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: CampaignsFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
        touchListeners()
    }

    override fun initializeComponents() {
        setOtherChanges()
        initFleetRv()
        initFilozillaRv()
    }

    override fun touchListeners() {

    }

    private fun setOtherChanges() {

    }

    private fun initFleetRv() {
        with(binding.rvFleetCampaigns) {
            hasFixedSize()
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }.run {
            setFleetData()
        }
    }

    private fun initFilozillaRv() {
        with(binding.rvFilozillaCampaigns) {
            hasFixedSize()
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }.run {
            setFilozillaData()
        }
    }

    private fun setFilozillaData() {
        filozillaCampaignList = ArrayList()
        filozillaCampaignList.run {
            add(Campaign(imageLink = "https://filozilla.com/filozilla_campaign_images/example.jpeg", type = "Filozilla Kampanyaları", title = "250 TL Discount on 5-14 Day Rentals Until the End of the Year250 TL Discount on 5-14 Day Rentals Until the End of the Year", desc = "Tesla Motors was founded in 2002. Among its founder's is Elon Musk."))
            add(Campaign(imageLink = "https://filozilla.com/filozilla_campaign_images/example2.jpeg", type = "Filozilla Kampanyaları", title = "500 TL Discount on 15-29 Day Rentals Until the End of the Year", desc = "Tesla Motors was founded in 2002. Among its founder's is Elon Musk."))
            add(Campaign(imageLink = "https://filozilla.com/filozilla_campaign_images/example3.jpeg", type = "Filozilla Kampanyaları", title = "50% Off All Cars in Qatar - Special Discount for the World Cup", desc = "Tesla Motors was founded in 2002. Among its founder's is Elon Musk."))
        }

        initFilozillaAdapter()
    }

    private fun setFleetData() {
        fleetCampaignList = ArrayList()
        fleetCampaignList.run {
            add(Campaign(imageLink = "https://filozilla.com/fleet_campaign_images/tesla_model_x.jpeg", type = "Firma Kampanyaları", title = "Tesla Model X Introduced Now!", desc = "Tesla Motors was founded in 2002. Among its founder's is Elon Musk."))
            add(Campaign(imageLink = "https://filozilla.com/fleet_campaign_images/tesla_model_x2.jpeg", type = "Firma Kampanyaları", title = "Tesla Model X Introduced Now!", desc = "Tesla Motors was founded in 2002. Among its founder's is Elon Musk."))
            add(Campaign(imageLink = "https://filozilla.com/fleet_campaign_images/tesla_model_x3.jpeg", type = "Firma Kampanyaları", title = "Tesla Model X Introduced Now!", desc = "Tesla Motors was founded in 2002. Among its founder's is Elon Musk."))
        }

        initFleetAdapter()
    }

    private fun initFleetAdapter() {
        with(
            FleetCampaignsAdapter(
                context = requireContext(),
                fleetCampaignList = fleetCampaignList
            )
        ) {
            binding.rvFleetCampaigns.adapter = this@with
            binding.fleetCampaignsController = itemCount > 0
        }
    }

    private fun initFilozillaAdapter() {
        with(
            FilozillaCampaignsAdapter(
                context = requireContext(),
                filozillaCampaignList = filozillaCampaignList,
            )
        ) {
            binding.rvFilozillaCampaigns.adapter = this@with
            binding.filozillaCampaignsController = itemCount > 0
        }
    }

}