/* =========================================================
   VistaApplication.java  —  کلاس Application
   مسیر: app/src/main/java/app/vista/VistaApplication.java
   نسخه: 1.3.07
   ========================================================= */

package app.vista;

import android.app.Application;
import android.util.Log;

public class VistaApplication extends Application {

    private static final String TAG = "VistaApplication";

    public static final String APP_VERSION = "1.3.07";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "VistaApplication started - version: " + APP_VERSION);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "VistaApplication terminated");
    }
}
