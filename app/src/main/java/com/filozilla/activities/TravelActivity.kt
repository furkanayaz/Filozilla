package com.filozilla.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.filozilla.databinding.ActivityTravelBinding
import com.filozilla.viewmodels.TravelActivityVM

class TravelActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTravelBinding
    private lateinit var viewModel: TravelActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTravelBinding.inflate(layoutInflater)
        binding.apply {
            activityTravelObject = this@TravelActivity
        }
        setContentView(binding.root)

        val tempViewModel: TravelActivityVM by viewModels()
        this.viewModel = tempViewModel
    }
}