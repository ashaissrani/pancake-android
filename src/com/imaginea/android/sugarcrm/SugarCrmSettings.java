package com.imaginea.android.sugarcrm;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.imaginea.android.sugarcrm.util.Util;

/**
 * <p>
 * SugarCrmSettings class.
 * </p>
 * 
 */
public class SugarCrmSettings extends PreferenceActivity {

    private static final String LOG_TAG = "SugarCrmSettings";

    private static final Map<String, String> savedSettings = new HashMap<String, String>();

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO fix the settings screen for android 2.0 and above
        findViewById(android.R.id.list).setBackgroundResource(R.drawable.bg);
        addPreferencesFromResource(R.xml.sugarcrm_settings);
    }

    /**
     * <p>
     * getUsername
     * </p>
     * 
     * @param context
     *            a {@link android.content.Context} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getUsername(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Util.PREF_USERNAME, null);
    }
    
    /**
     * <p>
     * getFetchRecordsSize
     * </p>
     * 
     * @param context
     *            a {@link android.content.Context} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getFetchRecordsSize(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Util.PREF_FETCH_RECORDS_SIZE, "2000");
    }

    /**
     * gets SugarCRM RestUrl, on production it returns empty url, if debuggable is set to "false" in
     * the manifest file.
     * 
     * // TODO - optimize once loaded instead of calling package manager everytime
     * 
     * @param context
     *            a {@link android.content.Context} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getSugarRestUrl(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo("com.imaginea.android.sugarcrm", ApplicationInfo.FLAG_DEBUGGABLE);
            if ((ApplicationInfo.FLAG_DEBUGGABLE & appInfo.flags) == ApplicationInfo.FLAG_DEBUGGABLE)
                return PreferenceManager.getDefaultSharedPreferences(context).getString(Util.PREF_REST_URL, context.getString(R.string.defaultUrl));
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        return PreferenceManager.getDefaultSharedPreferences(context).getString(Util.PREF_REST_URL, "");
    }

    /**
     * This methods saves the current settings, to be able to check later if settings changed
     * 
     * @param context
     *            a {@link android.content.Context} object.
     */
    public static void saveCurrentSettings(Context context) {
        savedSettings.clear();
        savedSettings.put(Util.PREF_REST_URL, getSugarRestUrl(context));
        savedSettings.put(Util.PREF_USERNAME, getUsername(context));
    }

    /**
     * This methods tells if the settings have changed.
     * 
     * @param context
     *            a {@link android.content.Context} object.
     * @return a boolean.
     */
    public static boolean currentSettingsChanged(Context context) {

        if (savedSettings.isEmpty()) {
            return false;
        }

        try {
            if (!getSugarRestUrl(context).equals(savedSettings.get(Util.PREF_REST_URL))) {
                return true;
            }

            if (!getUsername(context).equals(savedSettings.get(Util.PREF_USERNAME))) {
                return true;
            }

            return false;
        } finally {
            savedSettings.clear();
        }
    }

    /**
     * new method for back presses in android 2.0, instead of the standard mechanism defined in the
     * docs to handle legacy applications we use version code to handle back button... implement
     * onKeyDown for older versions and use Override on that.
     */
    public void onBackPressed() {
        // / super.onBackPressed();
        // TODO: onChange of settings, session has to be invalidated
        Log.i(LOG_TAG, "url - " + getSugarRestUrl(SugarCrmSettings.this));
        Log.i(LOG_TAG, "username - " + getUsername(SugarCrmSettings.this));

        // invalidate the session if the current settings changes
        if (currentSettingsChanged(SugarCrmSettings.this)) {
            SugarCrmApp app = ((SugarCrmApp) getApplicationContext());
            app.setSessionId(null);
        }

        finish();
    }
}
