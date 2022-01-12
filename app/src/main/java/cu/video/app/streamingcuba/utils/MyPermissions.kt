package cu.video.app.streamingcuba.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object MyPermissions {
    fun isCallPermissionGranted(mActivity: Activity): Boolean {
        return if (ContextCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            //Toast.makeText(mActivity, "Conceda el permiso siguiente para una experiencia completa.", Toast.LENGTH_SHORT).show()

            ActivityCompat.requestPermissions(
                mActivity,
                arrayOf(Manifest.permission.CALL_PHONE), 100
            )
            false
        } else true
    }

    fun isContactsPermissionGranted(mActivity: Activity): Boolean {
        return if (ContextCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            //Toast.makeText(mActivity, "Conceda el permiso siguiente para una experiencia completa.", Toast.LENGTH_SHORT).show()

            ActivityCompat.requestPermissions(
                mActivity,
                arrayOf(Manifest.permission.READ_CONTACTS), 100
            )
            false
        } else true
    }

}