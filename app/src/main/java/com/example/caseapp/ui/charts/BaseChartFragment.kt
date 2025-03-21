package com.example.caseapp.ui.charts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.caseapp.R
import com.example.caseapp.data.model.Case
import com.example.caseapp.data.model.Control
import com.example.caseapp.databinding.FragmentChartBinding
import com.example.caseapp.ui.cases.CaseDetailsViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

abstract class BaseChartFragment : Fragment() {

    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    protected val viewModel: CaseDetailsViewModel by activityViewModels()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChart()
        observeData()
    }

    private fun setupChart() {
        with(binding.chart) {
            description.isEnabled = false
            legend.isEnabled = true
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return dateFormat.format(Date(value.toLong()))
                    }
                }
                labelRotationAngle = -45f
                setDrawGridLines(false)
            }

            axisLeft.apply {
                setDrawGridLines(true)
                enableGridDashedLine(10f, 10f, 0f)
                axisMinimum = -4f
                axisMaximum = 4f
            }

            axisRight.isEnabled = false
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is CaseDetailsViewModel.CaseDetailsUiState.Success -> {
                        val case = state.case
                        viewModel.controls.observe(viewLifecycleOwner) { controls ->
                            updateChart(controls, case)
                        }
                    }
                    else -> {
                        // Los estados de carga y error son manejados por el fragmento padre
                    }
                }
            }
        }
    }

    private fun updateChart(controls: List<Control>, case: Case) {
        val entries = controls.map { control ->
            Entry(
                control.fechaControl.time.toFloat(),
                getMeasurement(control)
            )
        }

        val dataSet = LineDataSet(entries, getDataSetLabel()).apply {
            color = ContextCompat.getColor(requireContext(), getLineColor())
            setCircleColor(color)
            lineWidth = 2f
            circleRadius = 4f
            setDrawValues(false)
            mode = LineDataSet.Mode.LINEAR
            setDrawHighlightIndicators(false)
        }

        // Agregar líneas de referencia
        val referenceLines = mutableListOf<LineDataSet>()
        val dates = controls.map { it.fechaControl.time.toFloat() }
        val minDate = dates.minOrNull() ?: 0f
        val maxDate = dates.maxOrNull() ?: 0f

        // Línea de z-score 0
        referenceLines.add(createReferenceLine(
            minDate, maxDate, 0f,
            Color.GRAY, "Normal"
        ))

        // Líneas de z-score ±2 (riesgo)
        referenceLines.add(createReferenceLine(
            minDate, maxDate, 2f,
            Color.YELLOW, "+2 DE"
        ))
        referenceLines.add(createReferenceLine(
            minDate, maxDate, -2f,
            Color.YELLOW, "-2 DE"
        ))

        // Líneas de z-score ±3 (alto riesgo)
        referenceLines.add(createReferenceLine(
            minDate, maxDate, 3f,
            Color.RED, "+3 DE"
        ))
        referenceLines.add(createReferenceLine(
            minDate, maxDate, -3f,
            Color.RED, "-3 DE"
        ))

        binding.chart.data = LineData(referenceLines + dataSet)
        binding.chart.invalidate()
    }

    private fun createReferenceLine(
        minX: Float,
        maxX: Float,
        yValue: Float,
        color: Int,
        label: String
    ): LineDataSet {
        val entries = listOf(
            Entry(minX, yValue),
            Entry(maxX, yValue)
        )
        return LineDataSet(entries, label).apply {
            this.color = color
            lineWidth = 1f
            setDrawCircles(false)
            setDrawValues(false)
            enableDashedLine(10f, 5f, 0f)
            setDrawHighlightIndicators(false)
        }
    }

    protected abstract fun getMeasurement(control: Control): Float
    protected abstract fun getDataSetLabel(): String
    protected abstract fun getLineColor(): Int

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_CASE_ID = "case_id"
        private const val ARG_SEX = "sex"

        protected fun createArgs(caseId: Int, sex: Int) = Bundle().apply {
            putInt(ARG_CASE_ID, caseId)
            putInt(ARG_SEX, sex)
        }
    }
}