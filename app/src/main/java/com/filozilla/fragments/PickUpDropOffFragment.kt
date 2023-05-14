package com.filozilla.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.filozilla.adapters.PickUpDropOffAdapter
import com.filozilla.databinding.FragmentPickupDropoffBinding
import com.filozilla.interfaces.CommonInterface
import com.filozilla.models.Place
import com.filozilla.viewmodels.PickUpDropOffFragmentVM

class PickUpDropOffFragment: Fragment(), CommonInterface, TextWatcher {
    private lateinit var binding: FragmentPickupDropoffBinding
    private lateinit var viewModel: PickUpDropOffFragmentVM
    private lateinit var adapter: PickUpDropOffAdapter
    private lateinit var placeList: ArrayList<Place>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPickupDropoffBinding.inflate(layoutInflater, container, false)
        binding.fragmentPickUpObject = this

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: PickUpDropOffFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
        touchListeners()
    }

    override fun initializeComponents() {
        binding.etPickUpDropOffSearch.addTextChangedListener(this)
        setItems()
        initAdapter()
        initRv()
    }

    override fun touchListeners() {

    }

    private fun setItems() {
        placeList = ArrayList()
        placeList.add(Place("Türkiye", "Kocaeli", "Darıca", "Nenehatun Mahallesi", "Ertuğrul Gazi Caddesi", "Şehit Bülent Ünal Sok."))
        placeList.add(Place("Türkiye", "İstanbul", "Kadıköy", "Nusrettin Mahallesi", "Nusret Oğlu Caddesi", "Nusretin Babannesi Sok."))
        placeList.add(Place("Türkiye", "Bursa", "Gemlik", "TOGG Mahallesi", "Banane Caddesi", "Türkiye'nin Otomobili Sok."))
        placeList.add(Place("Türkiye", "İzmir", "Çeşme", "Güzeller Mahallesi", "Modeller Caddesi", "Barbara Palvin Sok."))
        placeList.add(Place("Türkiye", "Kocaeli", "Kandıra", "Gebeşler Mahallesi", "Çiftçiler Caddesi", "Şehit Bülent Ünal Sok."))
    }

    private fun initAdapter() {
        adapter = PickUpDropOffAdapter(requireContext(), placeList)
    }

    private fun initRv() {
        binding.rvPickUpDropOff.hasFixedSize()
        binding.rvPickUpDropOff.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvPickUpDropOff.adapter = adapter
    }

    fun backOnClick() {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(edit: Editable?) {
        val text = edit.toString().trim().lowercase()
        val filterPlaceList = ArrayList<Place>()

        placeList.forEach{
            if (it.country.trim().lowercase().contains(text) || it.province.trim().lowercase().contains(text) || it.district.trim().lowercase().contains(text) || it.neighborhood.trim().lowercase().contains(text) || it.road.trim().lowercase().contains(text) || it.street.trim().lowercase().contains(text)) {
                filterPlaceList.add(it)
                adapter.filterItems(filterPlaceList = filterPlaceList)
            }
        }
    }

}