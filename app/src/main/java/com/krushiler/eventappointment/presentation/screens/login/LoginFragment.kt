package com.krushiler.eventappointment.presentation.screens.login

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.krushiler.eventappointment.R
import com.krushiler.eventappointment.databinding.FragmentLoginBinding
import com.krushiler.eventappointment.presentation.util.collectFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModel()

    private val binding by viewBinding(FragmentLoginBinding::bind)

    private lateinit var credentialManager: CredentialManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        credentialManager = CredentialManager.create(requireContext())
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
        binding.googleSignInButton.setOnClickListener {
            lifecycleScope.launch {

                val googleIdOption = GetSignInWithGoogleOption.Builder(getString(R.string.credentials_client_id))
                    .build()

                val signInRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = try {
                    credentialManager.getCredential(request = signInRequest, context = requireContext())
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@launch
                }
                val credential = result.credential

                val authCredential =
                    if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)

                        GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    } else {
                        null
                    }

                if (authCredential != null) {
                    viewModel.signInWithAuthCredential(authCredential)
                }
            }
        }
    }

    private fun initObservers() {
        collectFlow(viewModel.errorState) {
            binding.emailLayout.error = it
        }
        collectFlow(viewModel.loadingState) {
            binding.loginButton.isEnabled = !it
            binding.registerButton.isEnabled = !it
            binding.googleSignInButton.isEnabled = !it
        }
    }
}