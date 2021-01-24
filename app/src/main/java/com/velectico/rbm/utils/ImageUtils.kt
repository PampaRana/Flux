package com.velectico.rbm.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

//https://github.com/Manuaravind1989/KotlinImageUtils/blob/master/app/src/main/java/com/mobiledev/imageutils/MainActivity.kt
class ImageUtils(_context:Context,_activity:Activity,_frag:Fragment) {
    val context = _context;
    val activity = _activity;
    val fragment = _frag;
    val FINAL_TAKE_PHOTO = 1
    val FINAL_CHOOSE_PHOTO = 2
    var imgFilePath :  String? = null;


    fun displayImage(imagePath: String?,picture:ImageView?){
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            picture?.setImageBitmap(bitmap)
        }
        else {
            Toast.makeText(context, "Failed to get image", Toast.LENGTH_SHORT).show()
        }
    }

    fun imagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        val cursor = context.contentResolver.query(uri as Uri, null, selection, null, null )
        if (cursor != null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

    fun openAlbum(){
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        fragment.startActivityForResult(intent, FINAL_CHOOSE_PHOTO)
    }

    fun captureImageIntent(imgPath : Uri?){
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgPath)
        fragment.startActivityForResult(intent, FINAL_TAKE_PHOTO)
    }

    fun getImageUri(): Uri? {
        val outputImage = File(context.externalCacheDir, "output_image.jpg")
        if(outputImage.exists()) {
            outputImage.delete()
        }
        outputImage.createNewFile()
        imgFilePath = outputImage.absolutePath
        var imageUri = if(Build.VERSION.SDK_INT >= 24){
            FileProvider.getUriForFile(activity, context.getPackageName() +".provider", outputImage)
        } else {
            Uri.fromFile(outputImage)
        }
        return imageUri;
    }

    @TargetApi(19)
    fun handleImageOnKitkat(data: Intent?): String? {
        var imagePath: String? = null
        val uri = data!!.data
        if (DocumentsContract.isDocumentUri(activity, uri)){
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri?.authority){
                val id = docId.split(":")[1]
                val selsetion = MediaStore.Images.Media._ID + "=" + id
                imagePath = imagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selsetion)
            }
            else if ("com.android.providers.downloads.documents" == uri?.authority){
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
                imagePath = imagePath(contentUri, null)
            }
        }
        else if ("content".equals(uri?.scheme, ignoreCase = true)){
            imagePath = imagePath(uri, null)
        }
        else if ("file".equals(uri?.scheme, ignoreCase = true)){
            imagePath = uri?.path
        }
        return imagePath
    }
}