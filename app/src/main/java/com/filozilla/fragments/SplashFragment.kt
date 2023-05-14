package com.filozilla.fragments

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.filozilla.R
import com.filozilla.activities.MainActivity
import android.util.*
import com.filozilla.databinding.FragmentSplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        binding.fragmentSplashObject = this

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initializeComponents()
    }

    fun initializeComponents() {
        CoroutineScope(Dispatchers.Main).launch {
            delayer()
        }
    }

    private suspend fun delayer() {
        delay(500)
        pageController()
    }

    private fun pageController() {
        requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)!!.run {
            if (getBoolean("controller", false))
                toMainPage(getInt("type", 0), getString("fullname", "")!!, getString("email", "")!!, getString("pw", "")!!)
            else
                toSigninPage()
        }
    }

    private fun toMainPage(type: Int, fullName: String, email: String, pw: String) {
        val pair = Pair((binding.ivSplash as View), "Icon")
        val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), pair)

        Intent(requireContext(), MainActivity::class.java).run {
            putExtra("type", type)
            putExtra("fullname", fullName)
            putExtra("email", email)
            putExtra("pw", pw)
            startActivity(this, options.toBundle())
            requireActivity().finish()
        }
    }

    private fun toSigninPage() {
        try {
            Navigation.findNavController(binding.root).navigate(R.id.splashToSignIn)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

}