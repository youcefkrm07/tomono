package com.applisto.appcloner;

import android.content.Context;
import java.io.File;
import android.util.Log; // <--- ADD THIS LINE

/**
 * Recursively chmods the app's files dir so every clone that shares
 * the same Linux UID can read/write it (world-writable as demo).
 * Works best when clones use android:sharedUserId="com.applisto.ac".
 */
public class AccessibleDataDirectory {

    public static final class Hook {
        public static void install(Context ctx) {
            try {
                // Make sure to get the correct directory. getFilesDir() is usually what you want for internal app files.
                // If you intended to make the *entire* data directory (/data/data/your.package.name) accessible,
                // that's usually ctx.getApplicationInfo().dataDir
                // However, getFilesDir() is more common for this type of operation.
                File filesDir = ctx.getFilesDir();
                if (filesDir != null) {
                    Log.d("AccessibleData", "Attempting to make accessible: " + filesDir.getAbsolutePath());
                    makeDirAccessible(filesDir);

                    // If you also want to do the parent of filesDir, which is usually app_HOME (/data/data/pkg/ )
                    // Be very careful with this, as it contains more than just 'files' (e.g., shared_prefs, databases, cache)
                    // File appDataDir = filesDir.getParentFile();
                    // if (appDataDir != null) {
                    //    Log.d("AccessibleData", "Attempting to make accessible (parent): " + appDataDir.getAbsolutePath());
                    //    makeDirAccessible(appDataDir);
                    // }

                } else {
                    Log.e("AccessibleData", "ctx.getFilesDir() returned null!");
                }
            } catch (Throwable t) {
                // It's good practice to log the exception too.
                Log.e("AccessibleData", "Error in install hook", t);
                t.printStackTrace();
            }
        }
    }

    /* ---------- recursion ---------- */
    private static void makeDirAccessible(File dir) {
        if (dir == null || !dir.exists()) {
            Log.w("AccessibleData", "Dir is null or does not exist: " + (dir != null ? dir.getAbsolutePath() : "null"));
            return;
        }
        if (!dir.isDirectory()) {
            Log.w("AccessibleData", "Path is not a directory: " + dir.getAbsolutePath());
            // If it's a file and you want to make it accessible directly:
            // Log.d("AccessibleData", "Making accessible (single file): " + dir.getAbsolutePath());
            // boolean r = dir.setReadable(true, false);
            // boolean w = dir.setWritable(true, false);
            // Log.d("AccessibleData", "Single File: " + dir.getName() + " R:" + r + " W:" + w);
            return;
        }

        Log.d("AccessibleData", "Making accessible (dir): " + dir.getAbsolutePath());
        boolean r = dir.setReadable(true, false); // Sets readable for everyone
        boolean w = dir.setWritable(true, false); // Sets writable for everyone
        boolean x = dir.setExecutable(true, false); // Sets executable for everyone
        Log.d("AccessibleData", "Dir: " + dir.getName() + " R:" + r + " W:" + w + " X:" + x);

        if (!x) { // For directories, executable is crucial for access
            Log.e("AccessibleData", "FAILED to make directory executable: " + dir.getAbsolutePath());
        }
        if (!r) {
            Log.w("AccessibleData", "FAILED to make directory readable: " + dir.getAbsolutePath());
        }
        if (!w) {
            Log.w("AccessibleData", "FAILED to make directory writable: " + dir.getAbsolutePath());
        }


        File[] children = dir.listFiles();
        if (children != null) {
            for (File f : children) {
                if (f.isDirectory()) {
                    makeDirAccessible(f);
                } else {
                    Log.d("AccessibleData", "Making accessible (file): " + f.getAbsolutePath());
                    boolean fr = f.setReadable(true, false);
                    boolean fw = f.setWritable(true, false);
                    // Files don't usually need 'executable' unless they are scripts or native binaries.
                    Log.d("AccessibleData", "File: " + f.getName() + " R:" + fr + " W:" + fw);
                    if (!fr || !fw) {
                         Log.e("AccessibleData", "FAILED to make file accessible: " + f.getAbsolutePath() + " R:"+fr + " W:"+fw);
                    }
                }
            }
        } else {
            Log.w("AccessibleData", "listFiles returned null for: " + dir.getAbsolutePath() + ". Check permissions for listing directory contents.");
        }
    }
}
