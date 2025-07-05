package com.voiceavtar.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.voiceavtar.R;

public class FloatingMicService extends Service {

    private WindowManager windowManager;
    private ImageView micIcon;
    private boolean isListening = false;

    @Override
    public void onCreate() {
        super.onCreate();
        createFloatingIcon();
        startForeground(1, createNotification()); // Run as foreground service
    }

    private void createFloatingIcon() {
        micIcon = new ImageView(this);
        micIcon.setImageResource(R.drawable.mic_icon);

        int layoutFlag = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ?
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                WindowManager.LayoutParams.TYPE_PHONE;

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                150, 150, layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 200;
        params.y = 400;

        micIcon.setOnTouchListener(new View.OnTouchListener() {
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
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(micIcon, params);
                        return true;

                    case MotionEvent.ACTION_UP:
                        toggleMic();
                        return true;
                }
                return false;
            }
        });

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(micIcon, params);
    }

    private Notification createNotification() {
        String channelId = "floating_mic_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Floating Mic",
                    NotificationManager.IMPORTANCE_LOW
            );
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        return new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Voice Avtar Active")
                .setContentText("Floating mic is running")
                .setSmallIcon(R.drawable.mic_icon)
                .build();
    }

    private void toggleMic() {
        Intent intent = new Intent(this, VoiceListenerService.class);
        if (!isListening) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
            Toast.makeText(this, "🎤 Mic ON", Toast.LENGTH_SHORT).show();
        } else {
            stopService(intent);
            Toast.makeText(this, "🎙️ Mic OFF", Toast.LENGTH_SHORT).show();
        }
        isListening = !isListening;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (micIcon != null && windowManager != null) {
            windowManager.removeView(micIcon);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
