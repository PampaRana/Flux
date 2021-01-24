package com.velectico.rbm.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.velectico.rbm.BuildConfig
import java.io.File
import java.io.FileNotFoundException
import java.util.*

object ImagePicker {
    private const val TAG = "ImagePicker"
    private const val TEMP_IMG_NAME = "tempImage"
    private const val DEFAULT_MIN_WIDTH_QUALITY = 100
    private const val minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY

    //*********** Returns Intent with Options of Image Picker Apps like Gallery, Camera etc ********//
    fun getImagePickerIntent(context: Context): Intent {
        // Chooser Intent of Filesystem Options
        var chooserIntent: Intent? = null
        // List of Intents
        var pickerIntentsList: MutableList<Intent?> =
            ArrayList()


        // Filesystem Intent
        val galleryIntent: Intent
        if (Build.VERSION.SDK_INT <= 19) {
            galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)
        } else if (Build.VERSION.SDK_INT > 19) {
            galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        } else {
            galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        }
        val uriForFile = FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID.toString() + ".provider",
            getTempFile(context)
        )

        // Camera Intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra("return_data", true)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile)


        // Adding Filesystem and Camera Intents to IntentList
        pickerIntentsList = addIntentToList(context, pickerIntentsList, galleryIntent)
        pickerIntentsList = addIntentToList(context, pickerIntentsList, cameraIntent)


        // Initializing Chooser Intent
        chooserIntent = Intent(
            Intent.createChooser(
                pickerIntentsList.removeAt(pickerIntentsList.size - 1),
                "Select Source"
            )
        )

        // Adding IntentList of Camera and Filesystem Intents
        chooserIntent.putExtra(
            Intent.EXTRA_INITIAL_INTENTS,
            pickerIntentsList.toTypedArray()
            //pickerIntentsList.toTypedArray<Parcelable>(arrayOf<Parcelable>())
        )
        return chooserIntent
    }

    //*********** Adds Intents to the IntentList ********//
    private fun addIntentToList(
        context: Context,
        intentList: MutableList<Intent?>,
        intent: Intent
    ): MutableList<Intent?> {
        val resInfoList =
            context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.setPackage(packageName)
            intentList.add(targetedIntent)
        }
        return intentList
    }

    //*********** Returns Temp File ********//
    private fun getTempFile(context: Context): File {
        // Create New Temp File
        val imageFile =
            File(context.externalCacheDir, TEMP_IMG_NAME)

        // Create Dir for Temp File
        imageFile.parentFile.mkdir()
        return imageFile
    }

    //*********** Returns the User Selected Image as Bitmap ********//
    fun getImageFromResult(
        context: Context?,
        resultCode: Int,
        imageReturnedIntent: Intent?
    ): Bitmap? {
        var bitmap: Bitmap? = null
        val imageFile = getTempFile(context!!)
        if (resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri?
            val isCamera =
                imageReturnedIntent == null || imageReturnedIntent.data == null || imageReturnedIntent.data
                    .toString().contains(imageFile.toString())
            selectedImage = if (isCamera) {
                // From Camera
                FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID.toString() + ".provider",
                    imageFile
                )
            } else {
                // From Storage
                imageReturnedIntent!!.data
            }
            bitmap = getResizedImage(context, selectedImage)
        }
        return bitmap
    }

    //*********** Resize to avoid using too much Memory loading Big Images (2560*1920) ********//
    private fun getResizedImage(
        context: Context,
        selectedImage: Uri?
    ): Bitmap? {
        var resizedBitmap: Bitmap? = null
        val sampleSizes = intArrayOf(5, 4, 3, 2, 1)
        var i = 0
        do {
            resizedBitmap = decodeBitmap(context, selectedImage, sampleSizes[i])
            i++
        } while (resizedBitmap!!.width < minWidthQuality && i < sampleSizes.size)
        return resizedBitmap
    }

    //*********** Returns Bitmap Decoded from Uri ********//
    private fun decodeBitmap(
        context: Context,
        uri: Uri?,
        sampleSize: Int
    ): Bitmap? {
        var decodedBitmap: Bitmap? = null
        var fileDescriptor: AssetFileDescriptor? = null
        val bitmapOptions = BitmapFactory.Options()
        try {
            fileDescriptor = context.contentResolver.openAssetFileDescriptor(uri!!, "r")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        decodedBitmap = BitmapFactory.decodeFileDescriptor(
            fileDescriptor!!.fileDescriptor,
            null,
            bitmapOptions
        )
        return decodedBitmap
    }

    //*********** Used for the Rotation of Image ********//
    private fun getRotation(
        context: Context,
        imageUri: Uri,
        isCamera: Boolean
    ): Int {
        val rotation: Int
        rotation = if (isCamera) {
            getRotationFromCamera(context, imageUri)
        } else {
            getRotationFromGallery(context, imageUri)
        }
        return rotation
    }

    //*********** Used for the Rotation of Image ********//
    private fun getRotationFromCamera(
        context: Context,
        imageFile: Uri
    ): Int {
        var rotate = 0
        try {
            context.contentResolver.notifyChange(imageFile, null)
            val exif = ExifInterface(imageFile.path)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rotate
    }

    //*********** Used for the Rotation of Image ********//
    fun getRotationFromGallery(context: Context, imageUri: Uri?): Int {
        var result = 0
        var cursor: Cursor? = null
        val columns =
            arrayOf(MediaStore.Images.Media.ORIENTATION)
        try {
            cursor = context.contentResolver.query(imageUri!!, columns, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val orientationColumnIndex = cursor.getColumnIndex(columns[0])
                result = cursor.getInt(orientationColumnIndex)
            }
        } catch (e: Exception) {
            // Handle Exception
        } finally {
            cursor?.close()
        }
        return result
    }

    //*********** Rotates the Bitmap ********//
    private fun rotate(bm: Bitmap, rotation: Int): Bitmap {
        if (rotation != 0) {
            val matrix = Matrix()
            matrix.postRotate(rotation.toFloat())
            return Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
        }
        return bm
    }
}
