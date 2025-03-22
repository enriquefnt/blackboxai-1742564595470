package com.example.caseapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.caseapp.R
import com.example.caseapp.databinding.ActivityLoginBinding
import com.example.caseapp.ui.main.MainActivity
import com.example.caseapp.util.NetworkManager
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var networkManager: NetworkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkManager = NetworkManager.getInstance(this)

        // Verificar si ya hay una sesión activa
        if (viewModel.checkPreviousLogin()) {
            startMainActivity()
            return
        }

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        with(binding) {
            // Mostrar versión de la app
            tvVersion.text = getString(
                R.string.version_format,
                packageManager.getPackageInfo(packageName, 0).versionName
            )

            // Limpiar errores al escribir
            etUsername.addTextChangedListener {
                tilUsername.error = null
            }
            etPassword.addTextChangedListener {
                tilPassword.error = null
            }

            // Configurar botón de login
            btnLogin.setOnClickListener {
                val username = etUsername.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (validateInputs(username, password)) {
                    viewModel.login(username, password)
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginViewModel.LoginState.Initial -> {
                    showLoading(false)
                }
                is LoginViewModel.LoginState.Loading -> {
                    showLoading(true)
                }
                is LoginViewModel.LoginState.Success -> {
                    showLoading(false)
                    startMainActivity()
                }
                is LoginViewModel.LoginState.Error -> {
                    showLoading(false)
                    showError(state.message)
                }
            }
        }

        // Observar estado de la red
        networkManager.isNetworkAvailable.observe(this) { isAvailable ->
            if (!isAvailable) {
                showOfflineMode()
            }
        }
    }

    private fun validateInputs(username: String, password: String): Boolean {
        var isValid = true

        with(binding) {
            if (username.isBlank()) {
                tilUsername.error = getString(R.string.required_field)
                isValid = false
            }

            if (password.isBlank()) {
                tilPassword.error = getString(R.string.required_field)
                isValid = false
            }
        }

        return isValid
    }

    private fun showLoading(show: Boolean) {
        with(binding) {
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
            btnLogin.isEnabled = !show
            etUsername.isEnabled = !show
            etPassword.isEnabled = !show
        }
    }

    private fun showError(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun showOfflineMode() {
        Snackbar.make(
            binding.root,
            R.string.offline_mode,
            Snackbar.LENGTH_LONG
        ).setAction(R.string.sync) {
            // Intentar sincronizar cuando haya conexión
            viewModel.clearLoginState()
        }.show()
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearLoginState()
    }
}