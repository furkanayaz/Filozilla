package com.filozilla.activities

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import com.filozilla.R
import com.filozilla.databinding.ActivityMainBinding
import com.filozilla.databinding.TravelDialogBinding
import com.filozilla.datatransfers.DateTimeTransfer
import com.filozilla.datatransfers.OtherTransfer
import com.filozilla.datatransfers.PickUpDropOffTransfer
import com.filozilla.datatransfers.HomePageTransfer
import com.filozilla.interfaces.CommonInterface
import com.filozilla.models.User
import com.filozilla.viewmodels.MainActivityVM
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.util.regex.Pattern

@SuppressLint("ClickableViewAccessibility")
class MainActivity : AppCompatActivity(), CommonInterface {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityVM
    private var navHostFragment: NavHostFragment?= null
    private var navController: NavController? = null

    companion object {
        var user: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            activityMainObject = this@MainActivity
            bottomVisibility = true
            searchVisibility = true
            campaignsVisibility = false
            leasesVisibility = false
            otherVisibility = false
        }

        val tempViewModel: MainActivityVM by viewModels()
        viewModel = tempViewModel

        initializeComponents()
        touchListeners()
        //showTravelDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        HomePageTransfer.apply {
            whichTab = false
            dailyDifferentPlace = false
            dailyAgeRange = false
            dailyPromotionCode = false
            monthlyDifferentPlace = false
            monthlyAgeRange = false
            monthlyPromotionCode = false
        }
        PickUpDropOffTransfer.apply {
            controller = 0
            location = ""
            location2 = ""
            location3 = ""
            location4 = ""
        }
        DateTimeTransfer.apply {
            dailyDateRange = ""
            monthlyDate = ""
        }
        OtherTransfer.apply {
            accoutDetailController = false
            contactDetailController = false
            contractDetailController = false
            settingsDetailController = false
            aboutAppDetailController = false
        }
    }

    override fun initializeComponents() {
        getInfo()
        initNavigation()
    }

    private fun getInfo() {
        getSharedPreferences("user", Context.MODE_PRIVATE).run {
            user = Gson().fromJson(getString("user", ""), User::class.java)
        }
    }

    private fun initNavigation() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostMain) as NavHostFragment
        navController = navHostFragment?.navController

        navController?.addOnDestinationChangedListener { _, destination, _ ->
            val regex = "SearchFragment|CampaignsFragment|LeasesFragment|OtherFragment"
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(destination.label.toString())
            binding.bottomVisibility = matcher.matches()
            backStackController(label = destination.label.toString())
        }
    }

    private fun backStackController(label: String) {
        when(label) {
            "SearchFragment" -> bottomSearchOnClick()
            "CampaignsFragment" -> bottomCampaingsOnClick()
            "LeasesFragment" -> bottomLeasesOnClick()
            "OtherFragment" -> bottomOtherOnClick()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun touchListeners() {
        binding.linearBottomSearch.setOnTouchListener { view, event ->
            touchAnimation(action = event.action, view = view)
            false
        }
        binding.linearBottomCampaigns.setOnTouchListener { view, event ->
            touchAnimation(action = event.action, view = view)
            false
        }
        binding.linearBottomLeases.setOnTouchListener { view, event ->
            touchAnimation(action = event.action, view = view)
            false
        }
        binding.linearBottomOther.setOnTouchListener { view, event ->
            touchAnimation(action = event.action, view = view)
            false
        }
    }

    private fun touchAnimation(action: Int, view: View) {
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.2f).apply {
                    this.duration = 5
                    this.start()
                }
            }
            MotionEvent.ACTION_UP -> {
                ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1.0f).apply {
                    this.duration = 5
                    this.start()
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1.0f).apply {
                    this.duration = 5
                    this.start()
                }
            }
        }
    }

    private fun showTravelDialog() {
        val dialogBinding = TravelDialogBinding.inflate(layoutInflater)
        dialogBinding.ivTravelDialogClose.bringToFront()

        CoroutineScope(Dispatchers.Main).launch {
            delay(timeMillis = 1000)

            dialogBinding.clTravelDialogChangeApp.setOnTouchListener { view, motionEvent ->

                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> viewModel.viewAnimation3(view = view)
                    MotionEvent.ACTION_UP and MotionEvent.ACTION_CANCEL -> viewModel.viewAnimation4(view = view)
                }

                false
            }

            dialogBinding.ivTravelDialogClose.setOnTouchListener { view, motionEvent ->

                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> viewModel.viewAnimation3(view = view)
                    MotionEvent.ACTION_UP and MotionEvent.ACTION_CANCEL -> viewModel.viewAnimation4(view = view)
                }

                false
            }

            Dialog(this@MainActivity).apply {
                setCancelable(true)
                size()
                window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
                setContentView(dialogBinding.root)

                dialogBinding.clTravelDialogChangeApp.setOnClickListener {
                    toTravelPage()
                }
                dialogBinding.ivTravelDialogClose.setOnClickListener {
                    this.dismiss()
                }

                show()
            }
            this.cancel()
        }
    }

    private fun toTravelPage() {
        Intent(this, TravelActivity::class.java).run {
            startActivity(this)
        }
    }

    fun bottomSearchOnClick() {
        if (!binding.searchVisibility!!) {
            binding.apply {
                searchVisibility = true
                campaignsVisibility = false
                leasesVisibility = false
                otherVisibility = false
            }
            navController?.navigate(R.id.homePageFragment)
        }
    }

    fun bottomCampaingsOnClick() {
        if (!binding.campaignsVisibility!!) {
            binding.apply {
                searchVisibility = false
                campaignsVisibility = true
                leasesVisibility = false
                otherVisibility = false
            }
            navController?.navigate(R.id.campaignsFragment)
        }
    }

    fun bottomLeasesOnClick() {
        if (!binding.leasesVisibility!!) {
            binding.apply {
                searchVisibility = false
                campaignsVisibility = false
                leasesVisibility = true
                otherVisibility = false
            }
            navController?.navigate(R.id.leasesFragment)
        }
    }

    fun bottomOtherOnClick() {
        if (!binding.otherVisibility!!) {
            binding.apply {
                searchVisibility = false
                campaignsVisibility = false
                leasesVisibility = false
                otherVisibility = true
            }
            navController?.navigate(R.id.otherFragment)
        }
    }

    private fun Dialog.size() {
        this.window?.apply {
            setLayout((resources.displayMetrics.widthPixels * 0.89).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

}