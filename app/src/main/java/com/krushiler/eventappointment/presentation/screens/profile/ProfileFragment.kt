package com.krushiler.eventappointment.presentation.screens.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.krushiler.eventappointment.R
import com.krushiler.eventappointment.databinding.FragmentProfileBinding
import com.krushiler.eventappointment.presentation.util.collectFlow
import com.krushiler.eventappointment.presentation.util.setVisible
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val viewModel: ProfileViewModel by viewModel()
    private val binding by viewBinding(FragmentProfileBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews() {
        binding.saveButton.setOnClickListener {
            viewModel.saveProfile(binding.nameEditText.text.toString())
        }
        binding.toolbar.menu.findItem(R.id.action_logout).setOnMenuItemClickListener {
            viewModel.logout()
            true
        }
    }

    private fun initObservers() {
        collectFlow(viewModel.errorFlow) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
        collectFlow(viewModel.loadingState) {
            binding.loadingIndicator.setVisible(it)
            binding.nameEditText.isEnabled = !it
            binding.saveButton.isEnabled = !it
        }
        collectFlow(viewModel.nameState) {
            if (binding.nameEditText.text.toString() != it) {
                binding.nameEditText.setText(it)
            }
        }
    }
}