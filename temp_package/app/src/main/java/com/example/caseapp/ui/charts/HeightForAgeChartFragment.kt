package com.example.caseapp.ui.charts

import android.os.Bundle
import com.example.caseapp.R
import com.example.caseapp.data.model.Control

class HeightForAgeChartFragment : BaseChartFragment() {

    override fun getMeasurement(control: Control): Float {
        return control.zScoreTalla ?: 0f
    }

    override fun getDataSetLabel(): String {
        return getString(R.string.height_for_age)
    }

    override fun getLineColor(): Int {
        return R.color.chart_line_height
    }

    companion object {
        fun newInstance(caseId: Int, sex: Int) = HeightForAgeChartFragment().apply {
            arguments = createArgs(caseId, sex)
        }
    }
}