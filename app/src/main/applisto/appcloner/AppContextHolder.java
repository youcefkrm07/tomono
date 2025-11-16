package com.applisto.appcloner;

import android.content.Context;

/** Keeps a global ApplicationContext for classes that need it */
public final class AppContextHolder {
    private static Context app;
    public static void init(Context c) { app = c.getApplicationContext(); }
    public static Context get()       { return app; }
}