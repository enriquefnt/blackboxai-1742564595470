package com.example.caseapp.ui.cases

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.caseapp.R
import com.example.caseapp.databinding.FragmentCaseDetailsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class CaseDetailsFragment : Fragment() {

    private var _binding: FragmentCaseDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CaseDetailsViewModel by viewModels()
    private val args: CaseDetailsFragmentArgs by navArgs()
    private lateinit var pagerAdapter: CaseDetailsPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCaseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        setupViewPager()
        setupFab()
        observeViewModel()
        loadCase()
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.case_details_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_edit -> {
                        findNavController().navigate(
                            CaseDetailsFragmentDirections.actionCaseDetailsToEdit(args.caseId)
                        )
                        true
                    }
                    R.id.action_delete -> {
                        showDeleteConfirmationDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupViewPager() {
        pagerAdapter = CaseDetailsPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = CaseDetailsPagerAdapter.TAB_TITLES[position]
        }.attach()
    }

    private fun setupFab() {
        binding.fabAddControl.setOnClickListener {
            findNavController().navigate(
                CaseDetailsFragmentDirections.actionCaseDetailsToControlEntry(
                    caseId = args.caseId
                )
            )
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is CaseDetailsViewModel.CaseDetailsUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val case = state.case
                            binding.apply {
                                tvName.text = case.nombreCompleto
                                tvDocument.text = getString(
                                    R.string.document_number_format,
                                    case.numeroDocumento
                                )
                                tvAge.text = viewModel.calculateAge(case.fechaNacimiento)
                            }
                        }
                        is CaseDetailsViewModel.CaseDetailsUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            // Mostrar error
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(R.string.error)
                                .setMessage(state.message)
                                .setPositiveButton(R.string.ok) { _, _ ->
                                    findNavController().navigateUp()
                                }
                                .show()
                        }
                        CaseDetailsViewModel.CaseDetailsUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun loadCase() {
        viewModel.loadCase(args.caseId)
    }

    private fun showDeleteConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.confirm_delete)
            .setMessage(R.string.delete_case_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ ->
                // TODO: Implementar eliminación del caso
                findNavController().navigateUp()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}