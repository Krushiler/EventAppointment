package com.krushiler.eventappointment.presentation.screens.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.krushiler.eventappointment.R
import com.krushiler.eventappointment.databinding.FragmentHomeBinding
import com.krushiler.eventappointment.presentation.util.collectFlow

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = binding.homeNavigationContainer.getFragment<NavHostFragment>().navController
        binding.navigationBar.setupWithNavController(navController)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.popBackStack()
            }
        }

        collectFlow(navController.currentBackStack) {
            callback.isEnabled = navController.previousBackStackEntry != null
        }

        requireActivity().onBackPressedDispatcher.addCallback(owner = viewLifecycleOwner, callback)
    }
}