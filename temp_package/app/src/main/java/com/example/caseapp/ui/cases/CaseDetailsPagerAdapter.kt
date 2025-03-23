package com.example.caseapp.ui.cases

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CaseDetailsPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            TAB_INFO -> CaseInfoFragment.newInstance()
            TAB_CONTROLS -> CaseControlsFragment.newInstance()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }

    companion object {
        const val TAB_INFO = 0
        const val TAB_CONTROLS = 1

        val TAB_TITLES = arrayOf(
            "Información",
            "Controles"
        )
    }
}