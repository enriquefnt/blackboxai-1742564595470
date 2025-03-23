package com.example.caseapp.ui.cases

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Int
import kotlin.jvm.JvmStatic

public data class CaseDetailsFragmentArgs(
  public val caseId: Int,
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
    public fun fromBundle(bundle: Bundle): CaseDetailsFragmentArgs {
      bundle.setClassLoader(CaseDetailsFragmentArgs::class.java.classLoader)
      val __caseId : Int
      if (bundle.containsKey("caseId")) {
        __caseId = bundle.getInt("caseId")
      } else {
        throw IllegalArgumentException("Required argument \"caseId\" is missing and does not have an android:defaultValue")
      }
      return CaseDetailsFragmentArgs(__caseId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): CaseDetailsFragmentArgs {
      val __caseId : Int?
      if (savedStateHandle.contains("caseId")) {
        __caseId = savedStateHandle["caseId"]
        if (__caseId == null) {
          throw IllegalArgumentException("Argument \"caseId\" of type integer does not support null values")
        }
      } else {
        throw IllegalArgumentException("Required argument \"caseId\" is missing and does not have an android:defaultValue")
      }
      return CaseDetailsFragmentArgs(__caseId)
    }
  }
}
