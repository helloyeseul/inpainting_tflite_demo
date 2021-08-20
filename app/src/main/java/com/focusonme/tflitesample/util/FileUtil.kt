package com.focusonme.tflitesample.util

import android.content.Context
import android.text.format.DateFormat
import com.focusonme.tflitesample.App
import java.io.File
import java.util.*


private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

fun getDateTimeString(date: Date) = DateFormat.format(DATE_FORMAT, date).toString()

fun Context.createNewFile(): String {

    val mediaDir = externalMediaDirs.firstOrNull()?.let {
        File(it, "TFLite").apply { mkdirs() }
    }

    return File(
        if (null != mediaDir && mediaDir.exists()) mediaDir else filesDir,
        "${getDateTimeString(Date())}.jpg"
    ).toString()
}