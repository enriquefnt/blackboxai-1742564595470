package com.example.caseapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.caseapp.R
import com.example.caseapp.databinding.ActivityMainBinding
import com.example.caseapp.ui.login.LoginActivity
import com.example.caseapp.util.NetworkManager
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupToolbar()
        setupFab()
        observeViewModel()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.casesListFragment)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            navController.navigate(R.id.action_casesList_to_caseEntry)
        }

        // Ocultar/mostrar FAB según la pantalla actual
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.casesListFragment -> binding.fab.show()
                else -> binding.fab.hide()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is MainViewModel.MainUiState.Online -> {
                    showOfflineMode(false)
                }
                is MainViewModel.MainUiState.Offline -> {
                    showOfflineMode(true)
                }
            }
        }

        viewModel.syncState.observe(this) { state ->
            when (state) {
                is MainViewModel.SyncState.Loading -> {
                    binding.syncProgressIndicator.visibility = View.VISIBLE
                }
                is MainViewModel.SyncState.Success -> {
                    binding.syncProgressIndicator.visibility = View.GONE
                    showMessage(getString(R.string.sync_complete))
                }
                is MainViewModel.SyncState.Error -> {
                    binding.syncProgressIndicator.visibility = View.GONE
                    showMessage(state.message)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        setupSearchView(searchView)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sync -> {
                viewModel.syncData()
                true
            }
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupSearchView(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Manejar la búsqueda en el fragmento actual
                navController.currentDestination?.id?.let { destinationId ->
                    when (destinationId) {
                        R.id.casesListFragment -> {
                            // El fragmento de lista de casos manejará la búsqueda
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Manejar cambios en tiempo real si es necesario
                return true
            }
        })
    }

    private fun showOfflineMode(show: Boolean) {
        if (show) {
            Snackbar.make(
                binding.root,
                R.string.offline_mode,
                Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.sync) {
                viewModel.syncData()
            }.show()
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun logout() {
        viewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}