package com.example.caseapp.ui.charts

import android.os.Bundle
import com.example.caseapp.R
import com.example.caseapp.data.model.Control

class WeightForAgeChartFragment : BaseChartFragment() {

    override fun getMeasurement(control: Control): Float {
        return control.zScorePeso ?: 0f
    }

    override fun getDataSetLabel(): String {
        return getString(R.string.weight_for_age)
    }

    override fun getLineColor(): Int {
        return R.color.chart_line_weight
    }

    companion object {
        fun newInstance(caseId: Int, sex: Int) = WeightForAgeChartFragment().apply {
            arguments = createArgs(caseId, sex)
        }
    }
}