package com.zz.crashprotect.demo

import android.app.Application
import android.content.Context
import com.zz.crashprotect.CrashProtect

class MyApplication :Application(){
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        CrashProtect.protectApp(this)
    }
}