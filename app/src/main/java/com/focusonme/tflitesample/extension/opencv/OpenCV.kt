package com.focusonme.tflitesample.extension.opencv

import com.focusonme.tflitesample.util.createNewFile
import com.orhanobut.logger.Logger
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import java.nio.ByteBuffer

fun saveImageToFile(buffer: ByteBuffer?): String {
    val mat = Mat(512, 680, CvType.CV_8UC3, buffer)
    Logger.d(mat.size().toString())
    val filePath = createNewFile()
    val result = Imgcodecs.imwrite(filePath, mat)
    Logger.d("imwrite to $filePath result: $result")
    return filePath
}