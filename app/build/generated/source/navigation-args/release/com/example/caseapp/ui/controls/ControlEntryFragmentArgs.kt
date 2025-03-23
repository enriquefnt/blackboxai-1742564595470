package com.example.caseapp.ui.controls

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Int
import kotlin.jvm.JvmStatic

public data class ControlEntryFragmentArgs(
  public val caseId: Int,
  public val controlId: Int = 0,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putInt("caseId", this.caseId)
    result.putInt("controlId", this.controlId)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("caseId", this.caseId)
    result.set("controlId", this.controlId)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): ControlEntryFragmentArgs {
      bundle.setClassLoader(ControlEntryFragmentArgs::class.java.classLoader)
      val __caseId : Int
      if (bundle.containsKey("caseId")) {
        __caseId = bundle.getInt("caseId")
      } else {
        throw IllegalArgumentException("Required argument \"caseId\" is missing and does not have an android:defaultValue")
      }
      val __controlId : Int
      if (bundle.containsKey("controlId")) {
        __controlId = bundle.getInt("controlId")
      } else {
        __controlId = 0
      }
      return ControlEntryFragmentArgs(__caseId, __controlId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): ControlEntryFragmentArgs {
      val __caseId : Int?
      if (savedStateHandle.contains("caseId")) {
        __caseId = savedStateHandle["caseId"]
        if (__caseId == null) {
          throw IllegalArgumentException("Argument \"caseId\" of type integer does not support null values")
        }
      } else {
        throw IllegalArgumentException("Required argument \"caseId\" is missing and does not have an android:defaultValue")
      }
      val __controlId : Int?
      if (savedStateHandle.contains("controlId")) {
        __controlId = savedStateHandle["controlId"]
        if (__controlId == null) {
          throw IllegalArgumentException("Argument \"controlId\" of type integer does not support null values")
        }
      } else {
        __controlId = 0
      }
      return ControlEntryFragmentArgs(__caseId, __controlId)
    }
  }
}
