package com.zz.crashprotect

import android.app.Application
import com.zz.crashprotect.protect.ThreadProtect
import com.zz.crashprotect.protect.appprotect.AppProtect
import com.zz.crashprotect.protect.activitystartup.ActivityStartUpProtect

interface IProtect{

    fun protect(app:Application)
}

object CrashProtect {

    private val protectStrategyList = arrayListOf<IProtect>()

    init {
        protectStrategyList.add(AppProtect())
        protectStrategyList.add(ActivityStartUpProtect())
        protectStrategyList.add(ThreadProtect())
    }

    fun protectApp(app: Application) {
        protectStrategyList.forEach {
            it.protect(app)
        }
    }
}

