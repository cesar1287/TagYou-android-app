package comcesar1287.github.tagyou.controller.util;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

public class BeaconMessageReceiver extends IntentService{

    private static final int MESSAGES_NOTIFICATION_ID = 1;
    private static final int NUM_MESSAGES_IN_NOTIFICATION = 5;

    private static final String TAG = "BeaconMessageReceiver";

    public BeaconMessageReceiver() {
        super("BeaconMessageReceiver");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            Nearby.Messages.handleIntent(intent, new MessageListener() {
                @Override
                public void onFound(Message message) {
                    Log.i(TAG, "Found message via PendingIntent: " + message);
                }

                @Override
                public void onLost(Message message) {
                    Log.i(TAG, "Lost message via PendingIntent: " + message);
                }
            });
        }
    }
}
