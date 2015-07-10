package cz.eutopia.snooperstopper;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static final String TAG = MainActivity.class.getSimpleName();
    static final int ACTIVATION_REQUEST = 47; // identifies our request id
    DevicePolicyManager devicePolicyManager;
    ComponentName snooperStopperDeviceAdmin;
    SwitchPreference switchAdmin;

    private Handler updateSwitchAdminHandler = new Handler();
    private Runnable updateSwitchAdminRunnable = new Runnable() {
        @Override
        public void run() {
            updateSwitchAdmin();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show icon in ActionBar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_logo);

        PreferenceFragment preferenceFragment = (PreferenceFragment) getFragmentManager().findFragmentById(R.id.frag_settings);
        switchAdmin = (SwitchPreference) preferenceFragment.findPreference("pref_key_device_admin_enabled");
        switchAdmin.setOnPreferenceChangeListener(switchAdminOnChangeListener);

        // Initialize Device Policy Manager service and our receiver class
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        snooperStopperDeviceAdmin = new ComponentName(this, SnooperStopperDeviceAdminReceiver.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                setProgressBarIndeterminateVisibility(true);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    boolean canGainSu = SuShell.canGainSu(getApplicationContext());
                    return canGainSu;
                } catch (Exception e) {
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                setProgressBarIndeterminateVisibility(false);

                if (!result) {
                    Toast.makeText(MainActivity.this, R.string.cannot_get_su,
                                   Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }.execute();
    }

    Preference.OnPreferenceChangeListener switchAdminOnChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference pref, Object newValue) {
            boolean isChecked = (boolean) newValue;
            if (isChecked) {
                Log.d(TAG, "switchAdmin.isChecked() == true");
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, snooperStopperDeviceAdmin);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "Monitors failed unlock attempts and shutdowns phone if limit is reached");
                startActivityForResult(intent, ACTIVATION_REQUEST);
            } else {
                Log.d(TAG, "switchAdmin.isChecked() == false");
                devicePolicyManager.removeActiveAdmin(snooperStopperDeviceAdmin);
                updateSwitchAdminHandler.postDelayed(updateSwitchAdminRunnable, 1000);
            }
            return true;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        updateSwitchAdmin();
    }

    public void updateSwitchAdmin() {
        if (devicePolicyManager.isAdminActive(snooperStopperDeviceAdmin)) {
            switchAdmin.setChecked(true);
        } else {
            switchAdmin.setChecked(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVATION_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(TAG, "Administration enabled!");
                    switchAdmin.setChecked(true);
                } else {
                    Log.i(TAG, "Administration enable FAILED!");
                    switchAdmin.setChecked(false);
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
