package com.focusonme.tflitesample.extension.opencv

import com.orhanobut.logger.Logger
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import java.nio.ByteBuffer

fun ByteBuffer.saveImageToFile(filePath: String): String {
    val mat = Mat(512, 680, CvType.CV_32FC3, this)
    val result = Imgcodecs.imwrite(filePath, mat)
    Logger.d("imwrite to $filePath result: $result")
    return filePath
}