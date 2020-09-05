package com.zz.crashprotect

import java.lang.Exception

 internal class CrashException(cause: Throwable?) : Exception(cause) {

    private val createTime = System.currentTimeMillis()

    /**
     * 结合上个异常进行分析
     * @return true可继续保护 false 没救了
     */
    fun analysis(preException: CrashException?): Boolean {
        if (isSystemException()) {//是系统自己发出的异常，不要保护
            return false
        }
        if (preException == null) {//上个异常为空，进行保护
            return true
        }
        if (createTime - preException.createTime < 100) {//与上个异常相差100ms内、我们认为是连环异常
            return false
        }
        return true
    }

    /**
     * 如何任务栈全部是系统类、则异常为系统异常
     */
     fun isSystemException(): Boolean {
        val first = cause?.stackTrace?.first() ?: return false
        val classLoader = Class.forName(first.className).classLoader
        return classLoader == null || classLoader == Exception::class.java.classLoader
    }

}