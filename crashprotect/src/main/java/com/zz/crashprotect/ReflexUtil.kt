package com.zz.crashprotect

import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

internal object ReflexUtil {

    internal fun getField(clazz: Class<*>, fieldName: String): Field? {
        try {
            val getDeclaredField =
                Class::class.java.getDeclaredMethod("getDeclaredField", String::class.java)
            getDeclaredField.isAccessible = true
            return getDeclaredField.invoke(clazz, fieldName) as Field
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return null
    }

    internal fun getMethod(clazz: Class<*>, methodName: String, vararg params: Class<*>): Method? {
        try {
            val getDeclaredMethod =
                Class::class.java.getDeclaredMethod(
                    "getDeclaredMethod",
                    String::class.java,
                    params.javaClass
                )
            getDeclaredMethod.isAccessible = true
            return getDeclaredMethod.invoke(clazz, methodName, params) as Method
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getFields(clazz: Class<*>): Array<Field> {
        try {
            val getDeclaredFields =
                Class::class.java.getDeclaredMethod("getDeclaredFields")
            getDeclaredFields.isAccessible = true
            return getDeclaredFields.invoke(clazz) as Array<Field>
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return arrayOf()
    }


    internal fun <T : Any> copyTo(fromObj: T, toObj: T) {
        val fields = getFields(fromObj.javaClass)
        fields.forEach {
            it.isAccessible = true
            it.set(toObj, it.get(fromObj))
        }
    }
}