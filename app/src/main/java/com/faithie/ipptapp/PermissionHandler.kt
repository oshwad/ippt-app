package com.faithie.ipptapp

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHandler (
    private val activity: Activity
) {
    fun getPermissions(){
        if(!checkPermissions()){
            Log.d("permissions","permissions not granted")
            reqPermissions()
        }
    }

    private fun reqPermissions() {
        ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, 0)
    }

    private fun checkPermissions(): Boolean{
        return REQUIRED_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA
        )
    }
}
