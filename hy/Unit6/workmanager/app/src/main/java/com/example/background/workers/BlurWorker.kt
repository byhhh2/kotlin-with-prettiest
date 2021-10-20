package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import com.example.background.R
import timber.log.Timber

private const val TAG = "BlurWorker"

class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        val appContext = applicationContext

        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Blurring image", appContext)

        // This is an utility function added to emulate slower work.
        sleep()

        return try {
            if (TextUtils.isEmpty(resourceUri)) {
                Timber.e("Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = appContext.contentResolver

            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri)))

            val output = blurBitmap(picture, appContext)

            // Write bitmap to a temp file
            val outputUri = writeBitmapToFile(appContext, output)

            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

            Result.success(outputData)

//            val picture = BitmapFactory.decodeResource(
//                appContext.resources,
//                R.drawable.test)
//
//            val output = blurBitmap(picture, appContext)
//
//            // Write bitmap to a temp file
//            val outputUri = writeBitmapToFile(appContext, output)
//            Log.d(TAG,"${outputUri}")
//
//            makeStatusNotification("Output is $outputUri", appContext)
//
//            //Result.success()
//            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
//            Result.success(outputData)

        } catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur")
            Timber.e(throwable, "Error applying blur")
            Result.failure()
        }
    } //dowork
}