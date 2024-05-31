package com.krushiler.eventappointment.presentation.screens.event_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.krushiler.eventappointment.R
import com.krushiler.eventappointment.databinding.FragmentEventDetailsBinding
import com.krushiler.eventappointment.presentation.util.collectFlow
import com.krushiler.eventappointment.presentation.util.setVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class EventDetailsFragment : Fragment(R.layout.fragment_event_details) {
    private val viewModel: EventDetailsViewModel by viewModel()

    private val binding by viewBinding(FragmentEventDetailsBinding::bind)
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        initViews()
        initObservers()
    }

    private fun initViews() {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.registerButton.setOnClickListener {
            viewModel.register()
        }

        binding.unregisterButton.setOnClickListener {
            viewModel.unregister()
        }
    }

    private fun initObservers() {
        collectFlow(viewModel.loadingState) {
            binding.loadingIndicator.setVisible(it)
        }
        collectFlow(viewModel.errorFlow) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
        collectFlow(viewModel.eventState) { event ->
            binding.contentView.setVisible(event != null)
            if (event == null) return@collectFlow
            binding.title.text = event.name
            binding.description.text = event.description
            binding.date.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(event.date)
            binding.owner.text = event.owner
            binding.registerButton.setVisible(!event.isMember)
            binding.unregisterButton.setVisible(event.isMember)
            binding.registeredMessage.setVisible(event.isMember)
        }
    }
}