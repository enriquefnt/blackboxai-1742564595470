package com.example.caseapp.ui.cases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.caseapp.R
import com.example.caseapp.databinding.FragmentCasesListBinding
import com.example.caseapp.util.NetworkManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CasesListFragment : Fragment() {

    private var _binding: FragmentCasesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CasesViewModel by viewModels()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private val casesAdapter = CasesAdapter(
        onCaseClicked = { case ->
            findNavController().navigate(
                CasesListFragmentDirections.actionCasesListToCaseDetails(case.caseId)
            )
        },
        onAddControlClicked = { case ->
            findNavController().navigate(
                CasesListFragmentDirections.actionCasesListToControlEntry(case.caseId)
            )
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCasesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        setupSearchView()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = casesAdapter
            setHasFixedSize(true)
        }

        // Implementar swipe-to-delete si se requiere
        /*
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(...) = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val case = casesAdapter.currentList[viewHolder.adapterPosition]
                showDeleteConfirmationDialog(case)
            }
        }).attachToRecyclerView(binding.recyclerView)
        */
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is CasesViewModel.CasesUiState.Loading -> {
                            showLoading(true)
                        }
                        is CasesViewModel.CasesUiState.Success -> {
                            showLoading(false)
                            casesAdapter.submitList(state.cases)
                            updateEmptyState(state.cases.isEmpty())
                        }
                        is CasesViewModel.CasesUiState.Empty -> {
                            showLoading(false)
                            updateEmptyState(true)
                        }
                        is CasesViewModel.CasesUiState.Error -> {
                            showLoading(false)
                            showError(state.message)
                        }
                    }
                }
            }
        }

        // Observar el último control para cada caso
        casesAdapter.currentList.forEach { case ->
            viewModel.getLastControl(case.caseId).observe(viewLifecycleOwner) { control ->
                // Actualizar la vista del caso con la información del último control
                control?.let {
                    val position = casesAdapter.currentList.indexOf(case)
                    if (position != -1) {
                        casesAdapter.notifyItemChanged(position)
                    }
                }
            }
        }
    }

    private fun setupSearchView() {
        val searchView = activity?.findViewById<SearchView>(R.id.action_search)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearchQuery(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText.orEmpty())
                return true
            }
        })
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.isVisible = show
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.tvEmptyState.isVisible = isEmpty
        binding.recyclerView.isVisible = !isEmpty
    }

    private fun showError(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun showDeleteConfirmationDialog(case: com.example.caseapp.data.model.Case) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.confirm_delete)
            .setMessage(getString(R.string.delete_case_confirmation, case.nombre))
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteCase(case)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}