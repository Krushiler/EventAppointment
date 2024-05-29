package com.krushiler.eventappointment.presentation.screens.login

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.krushiler.eventappointment.R
import com.krushiler.eventappointment.databinding.FragmentLoginBinding
import com.krushiler.eventappointment.presentation.util.collectFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModel()

    private val binding by viewBinding(FragmentLoginBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews() {
        binding.loginButton.setOnClickListener {
            viewModel.login(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
        }
        binding.registerButton.setOnClickListener {
            viewModel.register(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
        }
        binding.emailEditText.doAfterTextChanged { viewModel.inputChanged() }
        binding.passwordEditText.doAfterTextChanged { viewModel.inputChanged() }
    }

    private fun initObservers() {
        collectFlow(viewModel.errorState) {
            binding.emailLayout.error = it
        }
    }
}