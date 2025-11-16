package com.applisto.appcloner;

import android.content.ContentResolver;
import android.provider.Settings;

import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
//import com.swift.sandhook.HookLog;

import java.util.UUID;

@HookClass(Settings.Secure.class)
public class ChangeAndroidId {

    @HookMethod                        // ← no parameters in old version
    public static String getString(ContentResolver resolver, String name) {
        if ("android_id".equals(name)) {
          //  HookLog.d("AC: Returning spoofed android_id");  // ← single string
            return AndroidIdHolder.ID;
        }
        // call original method
        return (String) ChangeAndroidId_Backup.getString(resolver, name);
    }

    /* SandHook generates this backup class */
    public static class ChangeAndroidId_Backup {
        public static Object getString(ContentResolver r, String n) { return null; }
    }

    /* lazy random ID holder */
    private static final class AndroidIdHolder {
        private static final String ID = initId();
        private static String initId() {
            android.content.Context ctx = AppContextHolder.get();
            if (ctx == null) return "0123456789abcdef";
            android.content.SharedPreferences p =
                    ctx.getSharedPreferences("ac_ids", android.content.Context.MODE_PRIVATE);
            String id = p.getString("android_id", null);
            if (id == null) {
                id = UUID.randomUUID().toString().replace("-", "").substring(0,16);
                p.edit().putString("android_id", id).apply();
            }
            return id;
        }
    }
}