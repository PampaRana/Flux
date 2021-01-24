package com.velectico.rbm.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

object CheckPermissions {
    const val PERMISSIONS_REQUEST_CAMERA = 300
    const val PERMISSIONS_REQUEST_IMAGE = 302
    fun is_CAMERA_PermissionGranted(context: Context?): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            context!!.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        } else {
            // Permission is granted by default
            true
        }
    }

    fun is_STORAGE_PermissionGranted(context: Context?): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            context!!.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else {
            // Permission is granted by default
            true
        }
    }

    fun is_PHONE_STATE_PermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        } else {
            // Permission is granted by default
            true
        }
    }

    fun is_EXTERNAl_STORAGE_PermissionGranted(context: Context?): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            context!!.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else {
            // Permission is granted by default
            true
        }
    }
}