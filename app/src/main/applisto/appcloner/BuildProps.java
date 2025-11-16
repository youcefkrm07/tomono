package com.applisto.appcloner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.UUID;

/**
 * 1) Reflectively overwrites Build.* (affects reflective reads)
 * 2) Provides getter stubs used by the    smali/DEX rewriter.
 */
public final class BuildProps {

    /* Keys for the values stored per-clone */
    private static final String PREF_FILE = "ac_build_props";
    private static final String K_MANUF   = "mf";
    private static final String K_MODEL   = "mo";
    private static final String K_BRAND   = "br";
    private static final String K_DEVICE  = "dv";
    // --- Added Keys for More Info ---
    private static final String K_PRODUCT = "pr";
    private static final String K_BOARD = "bo";
    private static final String K_HARDWARE = "hw";
    private static final String K_DISPLAY = "di";
    private static final String K_ID = "id";
    private static final String K_FINGERPRINT = "fp";
    private static final String K_TAGS = "ta";
    private static final String K_TYPE = "ty";


    /* ---------- Hook installer ---------- */
    public static final class Hook {
        public static void install(Context ctx) {
            try {
                SharedPreferences p = ctx.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
                if (!p.contains(K_MANUF)) {           // first run: set Pixel 7 props
                    SharedPreferences.Editor e = p.edit();
                    // --- Google Pixel 7 Properties ---
                    e.putString(K_MANUF, "Google");
                    e.putString(K_MODEL,  "Pixel 7");
                    e.putString(K_BRAND,  "Google");
                    e.putString(K_DEVICE, "panther"); //
                    e.putString(K_PRODUCT, "panther"); //
                    e.putString(K_BOARD, "gs201"); //
                    e.putString(K_HARDWARE, "panther"); //
                    e.putString(K_DISPLAY, "TD1A.220804.031"); //
                    e.putString(K_ID, "TD1A.220804.031"); //
                    e.putString(K_FINGERPRINT, "google/panther/panther:13/TD1A.220804.031/9054341:user/release-keys"); //
                    e.putString(K_TAGS, "release-keys"); //
                    e.putString(K_TYPE, "user"); //
                    e.apply();
                }

                setField("MANUFACTURER", p.getString(K_MANUF, Build.MANUFACTURER));
                setField("MODEL",        p.getString(K_MODEL,  Build.MODEL));
                setField("BRAND",        p.getString(K_BRAND,  Build.BRAND));
                setField("DEVICE",       p.getString(K_DEVICE, Build.DEVICE));
                // --- Set Added Fields ---
                setField("PRODUCT", p.getString(K_PRODUCT, Build.PRODUCT));
                setField("BOARD", p.getString(K_BOARD, Build.BOARD));
                setField("HARDWARE", p.getString(K_HARDWARE, Build.HARDWARE));
                setField("DISPLAY", p.getString(K_DISPLAY, Build.DISPLAY));
                setField("ID", p.getString(K_ID, Build.ID));
                setField("FINGERPRINT", p.getString(K_FINGERPRINT, Build.FINGERPRINT));
                setField("TAGS", p.getString(K_TAGS, Build.TAGS));
                setField("TYPE", p.getString(K_TYPE, Build.TYPE));
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /* ---------- Getter stubs (for DEX-rewritten code) ---------- */
    public static String getManufacturer() { return Build.MANUFACTURER; }
    public static String getModel()        { return Build.MODEL; }
    public static String getBrand()        { return Build.BRAND; }
    public static String getDevice()       { return Build.DEVICE; }
    // --- Getters for Added Info ---
    public static String getProduct()      { return Build.PRODUCT; }
    public static String getBoard()        { return Build.BOARD; }
    public static String getHardware()     { return Build.HARDWARE; }
    public static String getDisplay()      { return Build.DISPLAY; }
    public static String getId()           { return Build.ID; }
    public static String getFingerprint()  { return Build.FINGERPRINT; }
    public static String getTags()         { return Build.TAGS; }
    public static String getType()         { return Build.TYPE; }

    /* ---------- Internals ---------- */
    private static void setField(String name, String value) throws Exception {
        Field f = Build.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(null, value);
    }

    private static String randomWord(String prefix) {
        return String.format(Locale.US, "%s_%s",
                prefix, UUID.randomUUID().toString().substring(0,8));
    }
}