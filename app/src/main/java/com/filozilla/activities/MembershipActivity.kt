package com.filozilla.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.filozilla.databinding.ActivityMembershipBinding

class MembershipActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMembershipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembershipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            activityMembershipObject = this@MembershipActivity
        }
    }

}