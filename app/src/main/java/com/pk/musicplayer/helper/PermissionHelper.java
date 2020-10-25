package com.pk.musicplayer.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.pk.musicplayer.application.MyApplication;

public class PermissionHelper {

    /**
     * This method checks if permission is granted
     * If not, then ask for permission
     *
     * @param view     for SnackBar
     * @param fragment to access requirePermission() method
     */
    public static void checkPermission(View view, final Fragment fragment) {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        final int READ_EXTERNAL_PERMISSION_CODE = 10;

        Context context = MyApplication.getContext();

        // Return if permission is already granted
        if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
            return;

        // Show SnackBar if its NOT the first time asking for perm
        if (fragment.shouldShowRequestPermissionRationale(permission)) {

            Snackbar.make(view, "Read Media Storage Permission Required",
                    Snackbar.LENGTH_INDEFINITE).setAction("Allow", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                    fragment.requestPermissions(permission, READ_EXTERNAL_PERMISSION_CODE);
                }
            }).show();
        } else {

            String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
            fragment.requestPermissions(permissions, READ_EXTERNAL_PERMISSION_CODE);
        }

    }//TODO: Reload data when perm is granted
}
