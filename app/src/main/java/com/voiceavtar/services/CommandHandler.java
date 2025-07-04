package com.voiceavtar.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class CommandHandler {

    public static void handleCommand(Context context, String command) {
        command = command.toLowerCase();

        if (command.contains("कैमरा")) {
            Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        if (command.contains("कॉल")) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        if (command.contains("chrome") || command.contains("क्रोम")) {
            Intent launchIntent = context.getPackageManager()
                    .getLaunchIntentForPackage("com.android.chrome");
            if (launchIntent != null) {
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(launchIntent);
            }
        }

        // और कमांड्स जोड़ सकते हो: flashlight, WhatsApp खोलो, etc.
    }
}
