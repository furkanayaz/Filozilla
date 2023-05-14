package com.filozilla.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.filozilla.databinding.FragmentLeasesBinding
import com.filozilla.interfaces.CommonInterface
import com.filozilla.viewmodels.LeasesFragmentVM

class LeasesFragment: Fragment(), CommonInterface {
    private lateinit var binding: FragmentLeasesBinding
    private lateinit var viewModel: LeasesFragmentVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: LeasesFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLeasesBinding.inflate(layoutInflater, container, false)
        binding.apply {
            fragmentLeasesObject = this@LeasesFragment
            emptyController = true
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
        touchListeners()
    }

    override fun initializeComponents() {
        setOtherChanges()
    }

    override fun touchListeners() {

    }

    private fun setOtherChanges() {

    }

}