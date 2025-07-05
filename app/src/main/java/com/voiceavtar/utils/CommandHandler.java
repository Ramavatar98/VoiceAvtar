package com.voiceavtar.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

public class CommandHandler {

    public static void handleCommand(Context context, String command) {
        try {
            command = command.toLowerCase().trim();
            Log.d("CommandHandler", "Processing: " + command);

            if (command.contains("कैमरा खोलो")) {
                openCamera(context);
            } else if (command.contains("कॉल करो")) {
                openDialer(context);
            } else if (command.contains("chrome खोलो") || command.contains("क्रोम खोलो")) {
                openChrome(context);
            } else {
                Toast.makeText(context, "Unrecognized command: " + command, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("CommandHandler", "Error processing command", e);
        }
    }

    private static void openCamera(Context context) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static void openDialer(Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static void openChrome(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.android.chrome");
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            // Open browser if Chrome not installed
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        }
    }
}
