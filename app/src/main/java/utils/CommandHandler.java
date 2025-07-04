package com.voiceavtar.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class CommandHandler {

    public static void handleCommand(Context context, String command) {
        command = command.toLowerCase().trim();

        if (command.contains("कैमरा खोलो")) {
            Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        } else if (command.contains("कॉल करो")) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        } else if (command.contains("chrome खोलो") || command.contains("क्रोम खोलो")) {
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.android.chrome");
            if (launchIntent != null) {
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(launchIntent);
            }
        }
    }
}
