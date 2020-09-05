package com.zz.crashprotect.protect.activitystartup

import android.app.Application
import com.zz.crashprotect.IProtect

class ActivityStartUpProtect :IProtect{

    override fun protect(app: Application) {
        ActivityThreadHook.hookInstrumentation()
    }

}