package com.example.caseapp.ui.controls

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.caseapp.R
import com.example.caseapp.data.model.Case
import com.example.caseapp.data.model.Control
import com.example.caseapp.databinding.ItemControlBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ControlsAdapter(
    private val case: Case,
    private val onControlClicked: (Control) -> Unit,
    private val calculateAge: (Date, Date) -> String,
    private val calculateZScores: (Control, Case) -> Map<String, Float>
) : ListAdapter<Control, ControlsAdapter.ControlViewHolder>(ControlDiffCallback()) {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControlViewHolder {
        val binding = ItemControlBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ControlViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ControlViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ControlViewHolder(
        private val binding: ItemControlBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onControlClicked(getItem(position))
                }
            }
        }

        fun bind(control: Control) {
            with(binding) {
                // Fecha y edad
                tvDate.text = dateFormat.format(control.fechaControl)
                tvAge.text = calculateAge(case.fechaNacimiento, control.fechaControl)

                // Medidas antropométricas
                tvWeight.text = root.context.getString(
                    R.string.weight_format,
                    control.peso
                )
                tvHeight.text = root.context.getString(
                    R.string.height_format,
                    control.talla
                )
                tvBMI.text = root.context.getString(
                    R.string.bmi_format,
                    control.imc
                )

                // Z-scores
                val zScores = calculateZScores(control, case)
                tvWeightZScore.text = root.context.getString(
                    R.string.weight_for_age_format,
                    zScores["zScorePeso"] ?: 0f
                )
                tvHeightZScore.text = root.context.getString(
                    R.string.height_for_age_format,
                    zScores["zScoreTalla"] ?: 0f
                )
                tvBMIZScore.text = root.context.getString(
                    R.string.bmi_for_age_format,
                    zScores["zScoreIMC"] ?: 0f
                )

                // Diagnóstico
                if (control.diagnostico.isNotBlank()) {
                    tvDiagnosis.visibility = View.VISIBLE
                    tvDiagnosis.text = root.context.getString(
                        R.string.diagnosis_format,
                        control.diagnostico
                    )
                } else {
                    tvDiagnosis.visibility = View.GONE
                }

                // Próxima cita
                control.proximoCitaRecomendada?.let { nextDate ->
                    tvNextAppointment.visibility = View.VISIBLE
                    tvNextAppointment.text = root.context.getString(
                        R.string.next_appointment_format,
                        dateFormat.format(nextDate)
                    )
                } ?: run {
                    tvNextAppointment.visibility = View.GONE
                }
            }
        }
    }

    private class ControlDiffCallback : DiffUtil.ItemCallback<Control>() {
        override fun areItemsTheSame(oldItem: Control, newItem: Control): Boolean {
            return oldItem.controlId == newItem.controlId
        }

        override fun areContentsTheSame(oldItem: Control, newItem: Control): Boolean {
            return oldItem == newItem
        }
    }
}