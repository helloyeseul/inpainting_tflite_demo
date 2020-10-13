package com.focusonme.tflitesample.util

import android.os.Environment
import android.text.format.DateFormat
import com.focusonme.tflitesample.App
import java.io.File
import java.util.*


private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

fun getDateTimeString(date: Date) = DateFormat.format(DATE_FORMAT, date).toString()

fun createTempFile(): String {
    val file = File.createTempFile("PNG_", ".png", App.context.cacheDir)

    return file.toString()
}

fun createNewFile(): String {
    val rootDirectory = File("${Environment.getExternalStorageDirectory()}/TFLite")
    if (!rootDirectory.exists()) rootDirectory.mkdir()

    return File(rootDirectory, getDateTimeString(Date()) + ".png").toString()
}
