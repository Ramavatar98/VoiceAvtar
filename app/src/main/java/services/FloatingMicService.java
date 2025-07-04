package com.voiceavtar.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.voiceavtar.R;

public class FloatingMicService extends Service {

    private WindowManager windowManager;
    private View micOverlay;
    private boolean isListening = false;

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        micOverlay = LayoutInflater.from(this).inflate(R.layout.empty_layout, null);
        ImageView mic = new ImageView(this);
        mic.setImageResource(R.drawable.mic_icon);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                150, 150,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 100;
        params.y = 300;

        mic.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int)(event.getRawX() - initialTouchX);
                        params.y = initialY + (int)(event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(mic, params);
                        return true;

                    case MotionEvent.ACTION_UP:
                        toggleMicListening();
                        return true;
                }
                return false;
            }
        });

        windowManager.addView(mic, params);
    }

    private void toggleMicListening() {
        Intent intent = new Intent(this, VoiceListenerService.class);
        if (!isListening) {
            startService(intent);
        } else {
            stopService(intent);
        }
        isListening = !isListening;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (micOverlay != null) windowManager.removeView(micOverlay);
    }
}
