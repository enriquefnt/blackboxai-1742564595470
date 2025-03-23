package com.example.caseapp.ui.cases

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.caseapp.R
import kotlin.Int

public class CaseDetailsFragmentDirections private constructor() {
  private data class ActionCaseDetailsToCaseEntry(
    public val caseId: Int = 0,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_caseDetails_to_caseEntry

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putInt("caseId", this.caseId)
        return result
      }
  }

  private data class ActionCaseDetailsToControlEntry(
    public val caseId: Int,
    public val controlId: Int = 0,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_caseDetails_to_controlEntry

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putInt("caseId", this.caseId)
        result.putInt("controlId", this.controlId)
        return result
      }
  }

  public companion object {
    public fun actionCaseDetailsToCaseEntry(caseId: Int = 0): NavDirections =
        ActionCaseDetailsToCaseEntry(caseId)

    public fun actionCaseDetailsToControlEntry(caseId: Int, controlId: Int = 0): NavDirections =
        ActionCaseDetailsToControlEntry(caseId, controlId)
  }
}
