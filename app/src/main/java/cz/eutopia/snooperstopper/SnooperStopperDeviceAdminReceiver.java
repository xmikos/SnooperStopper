package cz.eutopia.snooperstopper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
        if (failedPasswordAttempts >= 3) {
            Log.d(TAG, "TOO MANY FAILED UNLOCK ATTEMPTS!!! REBOOTING!");
            //Intent cameraIntent = new Intent(context, CameraActivity.class);
            //cameraIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(cameraIntent);
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
        //DialogFragment dialog = new PasswordChangedDialogFragment();
        //dialog.show(((Activity) context).getFragmentManager(), "passwordChangedDialog");
        Intent passwordManagerIntent = new Intent(context, PasswordManagerActivity.class);
        passwordManagerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(passwordManagerIntent);
    }

    public static class PasswordChangedDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.password_changed_dialog_title)
                    .setMessage(R.string.password_changed_dialog_message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User accepted dialog
                            Intent intent = new Intent(getActivity(), PasswordManagerActivity.class);
                            startActivity(intent);
                            dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dismiss();
                        }
                    });
            return builder.create();
        }
    }
}