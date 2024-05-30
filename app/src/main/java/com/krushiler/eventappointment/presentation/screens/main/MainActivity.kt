package com.krushiler.eventappointment.presentation.screens.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.krushiler.eventappointment.R
import com.krushiler.eventappointment.databinding.ActivityMainBinding
import com.krushiler.eventappointment.presentation.util.collectFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModel()
    private val binding by viewBinding(ActivityMainBinding::bind)
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = binding.rootNavigationContainer.getFragment<NavHostFragment>().navController

        collectFlow(viewModel.authState) {
            if (!it || navController.currentDestination?.id == R.id.loginFragment) {
                navController.popBackStack()
                navController.navigate(
                    if (it) R.id.homeFragment else R.id.loginFragment,
                    args = null,
                    navOptions = NavOptions.Builder().apply {
                        setEnterAnim(com.google.android.material.R.anim.abc_fade_in)
                        setExitAnim(com.google.android.material.R.anim.abc_fade_out)
                    }.build()
                )
            }
        }
    }
}