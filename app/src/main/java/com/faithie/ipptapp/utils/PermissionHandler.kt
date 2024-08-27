package com.faithie.ipptapp.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHandler(private val activity: Activity) {

    // Initialise permissions
    fun initPermissions(){
        if(!hasRequiredPermissions()){
            Log.d("PermissionCheck", "Requesting required permissions")
            requestPermissions()
            Log.d("PermissionCheck", if (hasRequiredPermissions()) "All granted" else "Some denied")
        }else{
            Log.d("Permission Check", "All permissions already granted")
        }
    }

    // Check if app has the required permissions in the companion object
    private fun hasRequiredPermissions(): Boolean {
        //.all returns false if anyone of the isPermissionGranted returns false
        return ALL_PERMISSIONS.all {
            val isPermissionGranted = ContextCompat.checkSelfPermission(
                activity,
                it
            ) == PackageManager.PERMISSION_GRANTED

            Log.d("PermissionCheck", "$it is ${if (isPermissionGranted) "granted" else "denied"}")

            // Last line is what is returned to the .all function
            isPermissionGranted
        }
    }

    // Used to request the needed permissions, listed in the companion object
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            activity,
            ALL_PERMISSIONS,
            0
        )
    }

    // Contains the required permissions for the app
    // Add further permissions here if needed
    companion object {
        private val ALL_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}