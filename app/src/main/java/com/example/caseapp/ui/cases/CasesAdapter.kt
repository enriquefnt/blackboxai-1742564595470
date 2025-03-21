package com.example.caseapp.ui.cases

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.caseapp.R
import com.example.caseapp.data.model.Case
import com.example.caseapp.databinding.ItemCaseBinding
import java.text.SimpleDateFormat
import java.util.*

class CasesAdapter(
    private val onCaseClicked: (Case) -> Unit,
    private val onAddControlClicked: (Case) -> Unit
) : ListAdapter<Case, CasesAdapter.CaseViewHolder>(CaseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val binding = ItemCaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CaseViewHolder(
        private val binding: ItemCaseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onCaseClicked(getItem(adapterPosition))
                }
            }

            binding.btnAddControl.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onAddControlClicked(getItem(adapterPosition))
                }
            }
        }

        fun bind(case: Case) {
            with(binding) {
                tvName.text = case.nombre
                tvDocument.text = itemView.context.getString(
                    R.string.document_number_format,
                    case.numeroDocumento
                )
                tvBirthDate.text = itemView.context.getString(
                    R.string.birth_date_format,
                    dateFormat.format(case.fechaNacimiento)
                )
                tvGender.text = itemView.context.getString(
                    R.string.gender_format,
                    if (case.sexo == 1) R.string.male else R.string.female
                )

                // Mostrar indicador de sincronización si es necesario
                ivSync.visibility = if (!case.synced) View.VISIBLE else View.GONE

                // Mostrar fecha del último control si existe
                tvLastControl.visibility = View.GONE // Se actualizará cuando implementemos la lógica de controles
            }
        }
    }

    private class CaseDiffCallback : DiffUtil.ItemCallback<Case>() {
        override fun areItemsTheSame(oldItem: Case, newItem: Case): Boolean {
            return oldItem.caseId == newItem.caseId
        }

        override fun areContentsTheSame(oldItem: Case, newItem: Case): Boolean {
            return oldItem == newItem
        }
    }
}