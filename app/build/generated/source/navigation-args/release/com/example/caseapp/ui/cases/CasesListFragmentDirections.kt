package com.example.caseapp.ui.cases

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.caseapp.R
import kotlin.Int

public class CasesListFragmentDirections private constructor() {
  private data class ActionCasesListToCaseEntry(
    public val caseId: Int = 0,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_casesList_to_caseEntry

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putInt("caseId", this.caseId)
        return result
      }
  }

  private data class ActionCasesListToCaseDetails(
    public val caseId: Int,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_casesList_to_caseDetails

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putInt("caseId", this.caseId)
        return result
      }
  }

  private data class ActionCasesListToControlEntry(
    public val caseId: Int,
    public val controlId: Int = 0,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_casesList_to_controlEntry

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putInt("caseId", this.caseId)
        result.putInt("controlId", this.controlId)
        return result
      }
  }

  public companion object {
    public fun actionCasesListToCaseEntry(caseId: Int = 0): NavDirections =
        ActionCasesListToCaseEntry(caseId)

    public fun actionCasesListToCaseDetails(caseId: Int): NavDirections =
        ActionCasesListToCaseDetails(caseId)

    public fun actionCasesListToControlEntry(caseId: Int, controlId: Int = 0): NavDirections =
        ActionCasesListToControlEntry(caseId, controlId)
  }
}
