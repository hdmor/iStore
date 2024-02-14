package com.istore.feature.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.MaterialColors
import com.istore.R
import com.istore.common.IActivity
import com.istore.common.convertDpToPixel
import com.istore.data.CartItemsCount
import com.istore.databinding.ActivityMainBinding
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : IActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        binding.mainBottomNavigation.setupWithNavController(navController)

        // Setup the ActionBar with navController and 3 top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home, R.id.cart, R.id.profile)
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCartItemsCountChangedEvent(cartItemsCount: CartItemsCount) {
        val badge = binding.mainBottomNavigation.getOrCreateBadge(R.id.cart)
        badge.badgeGravity = BadgeDrawable.BOTTOM_START
        badge.verticalOffset = convertDpToPixel(10f, this).toInt()
        badge.backgroundColor = MaterialColors.getColor(
            binding.mainBottomNavigation,
            com.google.android.material.R.attr.colorPrimary
        )
        badge.number = cartItemsCount.count
        badge.isVisible = cartItemsCount.count > 0
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCartItemsCount()
    }

}