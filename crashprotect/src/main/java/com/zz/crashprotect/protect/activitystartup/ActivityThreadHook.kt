package com.zz.crashprotect.protect.activitystartup

import android.app.Instrumentation
import com.zz.crashprotect.ReflexUtil

internal object ActivityThreadHook{

    fun hookInstrumentation(){

        try {
            val aClass = Class.forName("android.app.ActivityThread")
            val currentActivityThread = ReflexUtil.getMethod(
                aClass,
                "currentActivityThread"
            )!!
            val activity = currentActivityThread.invoke(null)
            val mInstrumentationField = ReflexUtil.getField(
                aClass,
                "mInstrumentation"
            )!!
            mInstrumentationField.isAccessible = true
            val mInstrumentation = mInstrumentationField[activity] as Instrumentation
            val evilInstrumentation =
                InstrumentationProxy(
                    mInstrumentation
                )
            mInstrumentationField[activity] = evilInstrumentation
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}