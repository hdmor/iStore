package com.istore.feature.auth

import android.os.Bundle
import android.util.Log
import android.window.OnBackInvokedDispatcher
import androidx.activity.addCallback
import com.istore.R
import com.istore.common.IActivity
import com.istore.common.TAG
import com.istore.databinding.ActivityAuthBinding

class AuthActivity : IActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_auth_fragment_container, LoginFragment())
            }.commit()
    }

}