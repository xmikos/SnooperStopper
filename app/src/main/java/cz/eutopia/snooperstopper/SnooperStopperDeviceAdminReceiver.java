package cz.eutopia.snooperstopper;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class SnooperStopperDeviceAdminReceiver extends DeviceAdminReceiver {
    static final String TAG = SnooperStopperDeviceAdminReceiver.class.getSimpleName();
    private static int failedPasswordAttempts = 0;

    /** Called when this application is approved to be a device administrator. */
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context, R.string.switch_admin_on, Toast.LENGTH_LONG).show();
        Log.d(TAG, "onEnabled");
    }

    /** Called when this application is no longer the device administrator. */
    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Toast.makeText(context, R.string.switch_admin_off, Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDisabled");
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        super.onPasswordFailed(context, intent);
        failedPasswordAttempts += 1;
        Log.d(TAG, "onPasswordFailed: " + failedPasswordAttempts);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        int pref_key_max_attempts_limit = Integer.parseInt(SP.getString("pref_key_max_attempts_limit", "3"));
        if (failedPasswordAttempts >= pref_key_max_attempts_limit) {
            Log.d(TAG, "TOO MANY FAILED UNLOCK ATTEMPTS!!! SHUTTING DOWN!");
            failedPasswordAttempts = 0;
            if (!SuShell.shutdown()) {
                Log.d(TAG, "COULDN'T SHUT DOWN!");
            }
        }
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        super.onPasswordSucceeded(context, intent);
        Log.d(TAG, "onPasswordSucceeded");
        failedPasswordAttempts = 0;
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        super.onPasswordChanged(context, intent);
        Log.d(TAG, "onPasswordChanged");
        Intent passwordManagerIntent = new Intent(context, PasswordManagerActivity.class);
        passwordManagerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(passwordManagerIntent);
    }
}