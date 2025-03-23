package com.example.caseapp.ui.charts

import android.os.Bundle
import com.example.caseapp.R
import com.example.caseapp.data.model.Control

class BMIForAgeChartFragment : BaseChartFragment() {

    override fun getMeasurement(control: Control): Float {
        return control.zScoreIMC ?: 0f
    }

    override fun getDataSetLabel(): String {
        return getString(R.string.bmi_for_age)
    }

    override fun getLineColor(): Int {
        return R.color.chart_line_bmi
    }

    companion object {
        fun newInstance(caseId: Int, sex: Int) = BMIForAgeChartFragment().apply {
            arguments = createArgs(caseId, sex)
        }
    }
}