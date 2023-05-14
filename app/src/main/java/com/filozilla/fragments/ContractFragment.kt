package com.filozilla.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.filozilla.databinding.FragmentContractBinding
import com.filozilla.viewmodels.ContractFragmentVM

class ContractFragment: Fragment() {
    private lateinit var binding: FragmentContractBinding
    private lateinit var viewModel: ContractFragmentVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ContractFragmentVM by viewModels()
        this.viewModel = tempViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentContractBinding.inflate(layoutInflater)
        binding.apply {
            fragmentContractObject = this@ContractFragment
            contractHead = ""
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getInformation()
        initComponents()
    }

    private fun getInformation() {
        val bundle: ContractFragmentArgs by navArgs()
        binding.contractHead = bundle.contractHead
    }

    private fun initComponents() {
        initWebView()
    }

    private fun initWebView() {
        binding.webViewContract.webViewClient = WebViewClient()
        binding.webViewContract.settings.javaScriptEnabled
        binding.webViewContract.loadUrl("https://filozilla.com/contracts/${viewModel.getPage(binding.contractHead!!)}")
    }

    fun backOnClick() {

    }
}