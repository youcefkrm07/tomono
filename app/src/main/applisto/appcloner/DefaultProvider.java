package com.applisto.appcloner;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.swift.sandhook.SandHook;
import com.swift.sandhook.SandHookConfig;

public class DefaultProvider extends ContentProvider {

    @Override public boolean onCreate() {
        Context ctx = getContext();
        AppContextHolder.init(ctx);

        // ─── initialise SandHook (very old version) ───
        try {
            SandHookConfig.DEBUG = false;
            // Remove the init() call - it's private in this version
            SandHook.addHookClass(ChangeAndroidId.class);   // this auto-initializes
        } catch (Throwable t) { t.printStackTrace(); }

        BuildProps.Hook.install(ctx);
        AccessibleDataDirectory.Hook.install(ctx);
        return true;
    }

    /* unused stubs */
    public Cursor  query (Uri u,String[]p,String s,String[]sa,String so){return null;}
    public String  getType(Uri u){return null;}
    public Uri     insert (Uri u, ContentValues v){return null;}
    public int     delete (Uri u,String s,String[]sa){return 0;}
    public int     update (Uri u,ContentValues v,String s,String[]sa){return 0;}
}
