package com.example.threads.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import okhttp3.MultipartBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object ImageConverter {
    private const val MAX_IMAGE_SIZE = 1024

    fun getFile(context: Context, uri: Uri): File? {
        val fileName = "temp_image.jpg"
        val file = File(context.filesDir, fileName)

        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.use { input ->
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeStream(input, null, options)

                // Calculate the sample size to reduce the image size
                options.inSampleSize = calculateInSampleSize(options, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE)
                options.inJustDecodeBounds = false

                val bitmap = BitmapFactory.decodeStream(input, null, options)
                val outputStream = FileOutputStream(file)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // Adjust quality here
                outputStream.close()

                return file
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}

