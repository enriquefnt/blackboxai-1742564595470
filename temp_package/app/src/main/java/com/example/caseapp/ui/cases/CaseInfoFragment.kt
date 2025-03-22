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
import com.example.caseapp.R
import com.example.caseapp.databinding.FragmentCaseInfoBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CaseInfoFragment : Fragment() {

    private var _binding: FragmentCaseInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CaseDetailsViewModel by activityViewModels()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCaseInfoBinding.inflate(inflater, container, false)
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
                            with(binding) {
                                // Información personal
                                tvBirthDate.text = getString(
                                    R.string.birth_date_format,
                                    dateFormat.format(case.fechaNacimiento)
                                )
                                tvGender.text = getString(
                                    R.string.gender_format,
                                    when (case.sexo) {
                                        1 -> getString(R.string.male)
                                        2 -> getString(R.string.female)
                                        else -> ""
                                    }
                                )

                                // Último control
                                viewModel.controls.observe(viewLifecycleOwner) { controls ->
                                    if (controls.isNotEmpty()) {
                                        val lastControl = controls.first()
                                        tvLastControlDate.text = getString(
                                            R.string.control_date_format,
                                            dateFormat.format(lastControl.fechaControl)
                                        )
                                        tvLastWeight.text = getString(
                                            R.string.weight_format,
                                            lastControl.peso
                                        )
                                        tvLastHeight.text = getString(
                                            R.string.height_format,
                                            lastControl.talla
                                        )
                                    }
                                }

                                // Metadatos
                                tvCreatedDate.text = getString(
                                    R.string.created_date_format,
                                    dateFormat.format(case.fechaCreacion)
                                )
                                tvModifiedDate.text = getString(
                                    R.string.modified_date_format,
                                    dateFormat.format(case.fechaModificacion)
                                )
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
        fun newInstance() = CaseInfoFragment()
    }
}