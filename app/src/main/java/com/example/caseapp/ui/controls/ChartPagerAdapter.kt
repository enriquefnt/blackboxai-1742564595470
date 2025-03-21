package com.example.caseapp.ui.controls

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.caseapp.data.model.Case
import com.example.caseapp.ui.charts.BMIForAgeChartFragment
import com.example.caseapp.ui.charts.HeightForAgeChartFragment
import com.example.caseapp.ui.charts.WeightForAgeChartFragment

class ChartPagerAdapter(
    fragment: Fragment,
    private val case: Case
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            TAB_WEIGHT -> WeightForAgeChartFragment.newInstance(case.caseId, case.sexo)
            TAB_HEIGHT -> HeightForAgeChartFragment.newInstance(case.caseId, case.sexo)
            TAB_BMI -> BMIForAgeChartFragment.newInstance(case.caseId, case.sexo)
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }

    companion object {
        const val TAB_WEIGHT = 0
        const val TAB_HEIGHT = 1
        const val TAB_BMI = 2
    }
}