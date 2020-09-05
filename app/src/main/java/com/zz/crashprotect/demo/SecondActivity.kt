package com.zz.crashprotect.demo

import android.app.Activity
import android.os.Bundle
import android.util.Log

class SecondActivity :Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("ZZZZZZ",""+1/0)
    }
}