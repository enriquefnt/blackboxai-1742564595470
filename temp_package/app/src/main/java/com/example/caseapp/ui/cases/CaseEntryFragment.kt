package com.example.caseapp.ui.cases

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.caseapp.R
import com.example.caseapp.databinding.FragmentCaseEntryBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class CaseEntryFragment : Fragment() {

    private var _binding: FragmentCaseEntryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CaseEntryViewModel by viewModels()
    private val args: CaseEntryFragmentArgs by navArgs()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var selectedDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCaseEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        
        // Cargar caso si estamos en modo edición
        if (args.caseId != 0) {
            viewModel.loadCase(args.caseId)
        }
    }

    private fun setupViews() {
        with(binding) {
            // Configurar selector de fecha
            tilBirthDate.setEndIconOnClickListener {
                showDatePicker()
            }
            etBirthDate.setOnClickListener {
                showDatePicker()
            }

            // Configurar botón de guardar
            btnSave.setOnClickListener {
                saveCase()
            }

            // Limpiar errores al escribir
            etName.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) tilName.error = null
            }
            etDocument.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) tilDocument.error = null
            }
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CaseEntryViewModel.CaseEntryUiState.Initial -> {
                    // Estado inicial, no necesitamos hacer nada
                }
                is CaseEntryViewModel.CaseEntryUiState.Loading -> {
                    showLoading(true)
                }
                is CaseEntryViewModel.CaseEntryUiState.Success -> {
                    showLoading(false)
                    findNavController().navigateUp()
                }
                is CaseEntryViewModel.CaseEntryUiState.SuccessWithSync -> {
                    showLoading(false)
                    showMessage(state.message)
                    findNavController().navigateUp()
                }
                is CaseEntryViewModel.CaseEntryUiState.Loaded -> {
                    populateCase(state.case)
                }
                is CaseEntryViewModel.CaseEntryUiState.Error -> {
                    showLoading(false)
                    showError(state.message)
                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        selectedDate?.let { calendar.time = it }

        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(year, month, day)
                selectedDate = calendar.time
                binding.etBirthDate.setText(dateFormat.format(selectedDate))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            // No permitir fechas futuras
            datePicker.maxDate = System.currentTimeMillis()
        }.show()
    }

    private fun saveCase() {
        with(binding) {
            val name = etName.text.toString().trim()
            val document = etDocument.text.toString().trim()
            val birthDate = selectedDate
            val gender = when (rgGender.checkedRadioButtonId) {
                R.id.rbMale -> 1
                R.id.rbFemale -> 2
                else -> 0
            }

            // Validar campos
            var isValid = true

            if (name.isBlank()) {
                tilName.error = getString(R.string.required_field)
                isValid = false
            }

            if (document.isBlank()) {
                tilDocument.error = getString(R.string.required_field)
                isValid = false
            }

            if (birthDate == null) {
                tilBirthDate.error = getString(R.string.required_field)
                isValid = false
            }

            if (gender == 0) {
                showError(getString(R.string.select_gender))
                isValid = false
            }

            if (isValid && birthDate != null) {
                viewModel.saveCase(name, document, birthDate, gender)
            }
        }
    }

    private fun populateCase(case: com.example.caseapp.data.model.Case) {
        with(binding) {
            etName.setText(case.nombre)
            etDocument.setText(case.numeroDocumento)
            selectedDate = case.fechaNacimiento
            etBirthDate.setText(dateFormat.format(case.fechaNacimiento))
            
            when (case.sexo) {
                1 -> rbMale.isChecked = true
                2 -> rbFemale.isChecked = true
            }
        }
    }

    private fun showLoading(show: Boolean) {
        with(binding) {
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
            btnSave.isEnabled = !show
        }
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}