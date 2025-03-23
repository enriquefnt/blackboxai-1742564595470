package com.example.caseapp.ui.cases

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Int
import kotlin.jvm.JvmStatic

public data class CaseEntryFragmentArgs(
  public val caseId: Int = 0,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putInt("caseId", this.caseId)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("caseId", this.caseId)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): CaseEntryFragmentArgs {
      bundle.setClassLoader(CaseEntryFragmentArgs::class.java.classLoader)
      val __caseId : Int
      if (bundle.containsKey("caseId")) {
        __caseId = bundle.getInt("caseId")
      } else {
        __caseId = 0
      }
      return CaseEntryFragmentArgs(__caseId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): CaseEntryFragmentArgs {
      val __caseId : Int?
      if (savedStateHandle.contains("caseId")) {
        __caseId = savedStateHandle["caseId"]
        if (__caseId == null) {
          throw IllegalArgumentException("Argument \"caseId\" of type integer does not support null values")
        }
      } else {
        __caseId = 0
      }
      return CaseEntryFragmentArgs(__caseId)
    }
  }
}
