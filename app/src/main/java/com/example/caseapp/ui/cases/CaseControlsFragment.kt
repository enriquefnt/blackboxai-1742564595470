package com.example.caseapp.ui.cases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.caseapp.databinding.FragmentCaseControlsBinding
import com.example.caseapp.ui.controls.ChartPagerAdapter
import com.example.caseapp.ui.controls.ControlsAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CaseControlsFragment : Fragment() {

    private var _binding: FragmentCaseControlsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CaseDetailsViewModel by activityViewModels()
    private lateinit var controlsAdapter: ControlsAdapter
    private lateinit var chartPagerAdapter: ChartPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCaseControlsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is CaseDetailsViewModel.CaseDetailsUiState.Success -> {
                            val case = state.case

                            // Configurar adaptador de controles
                            controlsAdapter = ControlsAdapter(
                                case = case,
                                onControlClicked = { control ->
                                    findNavController().navigate(
                                        CaseDetailsFragmentDirections
                                            .actionCaseDetailsToControlEntry(
                                                caseId = case.caseId,
                                                controlId = control.controlId
                                            )
                                    )
                                },
                                calculateAge = { birthDate, currentDate ->
                                    viewModel.calculateAge(birthDate, currentDate)
                                },
                                calculateZScores = { control, case ->
                                    viewModel.calculateZScores(control, case)
                                }
                            )
                            binding.recyclerView.adapter = controlsAdapter

                            // Configurar adaptador de gráficos
                            chartPagerAdapter = ChartPagerAdapter(this@CaseControlsFragment, case)
                            binding.viewPagerCharts.adapter = chartPagerAdapter

                            // Configurar tabs de gráficos
                            TabLayoutMediator(
                                binding.tabLayoutCharts,
                                binding.viewPagerCharts
                            ) { tab, position ->
                                tab.text = when (position) {
                                    ChartPagerAdapter.TAB_WEIGHT -> "Peso/Edad"
                                    ChartPagerAdapter.TAB_HEIGHT -> "Talla/Edad"
                                    ChartPagerAdapter.TAB_BMI -> "IMC/Edad"
                                    else -> null
                                }
                            }.attach()

                            // Observar lista de controles
                            viewModel.controls.observe(viewLifecycleOwner) { controls ->
                                binding.tvEmptyState.visibility =
                                    if (controls.isEmpty()) View.VISIBLE else View.GONE
                                controlsAdapter.submitList(controls)
                            }
                        }
                        else -> {
                            // Los estados de carga y error son manejados por el fragmento padre
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = CaseControlsFragment()
    }
}