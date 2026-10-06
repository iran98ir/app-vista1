/* =========================================================
   SplashActivity.java  —  صفحه‌ی لود اولیه — Vista1 (MuMu)
   مسیر: app/src/main/java/app/vista/SplashActivity.java
   نسخه: 1.3.07
   ========================================================= */

package app.vista;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long MIN_SPLASH_TIME_MS = 1500L;
    private static final long MAX_SPLASH_TIME_MS = 8000L;

    private ImageView splashLogo;
    private TextView splashAppName;
    private TextView splashWelcome;
    private TextView splashLoadingText;
    private ProgressBar splashProgress;

    private long startTime;
    private boolean navigated = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startTime = System.currentTimeMillis();

        splashLogo = findViewById(R.id.splashLogo);
        splashAppName = findViewById(R.id.splashAppName);
        splashWelcome = findViewById(R.id.splashWelcome);
        splashLoadingText = findViewById(R.id.splashLoadingText);
        splashProgress = findViewById(R.id.splashProgress);

        // انیمیشن Fade-in برای همه اجزا
        animateFadeIn(splashLogo, 0);
        animateFadeIn(splashAppName, 200);
        animateFadeIn(splashWelcome, 400);
        animateFadeIn(splashLoadingText, 600);

        // شروع پیش‌بارگذاری
        startPreload();
    }

    private void animateFadeIn(View view, long delayMs) {
        if (view == null) return;
        view.setAlpha(0f);
        AlphaAnimation anim = new AlphaAnimation(0f, 1f);
        anim.setDuration(600);
        anim.setStartOffset(delayMs);
        anim.setFillAfter(true);
        view.startAnimation(anim);
    }

    private void startPreload() {
        PreloadManager.preload(getApplicationContext(), new PreloadManager.PreloadCallback() {
            @Override
            public void onPageLoaded() {
                long elapsed = System.currentTimeMillis() - startTime;
                long remaining = MIN_SPLASH_TIME_MS - elapsed;
                if (remaining < 0) remaining = 0;
                handler.postDelayed(() -> navigateToMain(true), remaining);
            }

            @Override
            public void onPageFailed() {
                long elapsed = System.currentTimeMillis() - startTime;
                long remaining = MIN_SPLASH_TIME_MS - elapsed;
                if (remaining < 0) remaining = 0;
                handler.postDelayed(() -> navigateToMain(false), remaining);
            }
        });

        // Timeout اضطراری
        handler.postDelayed(() -> {
            if (!navigated) {
                navigateToMain(false);
            }
        }, MAX_SPLASH_TIME_MS);
    }

    private void navigateToMain(boolean preloaded) {
        if (navigated) return;
        navigated = true;

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra("page_preloaded", preloaded);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
