package atlascience.bitmaptest.Activities.Settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.R;

/**
 * Created by Ilyas on 9/16/2017.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public  class GeneralPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    Context context;
    SharedPreferences prefs;
    SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setHasOptionsMenu(true);

        context = getActivity().getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(this);
        sessionManager= new SessionManager(context);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        for(int x = 0; x < getPreferenceScreen().getPreferenceCount(); x++){
            PreferenceCategory lol = (PreferenceCategory) getPreferenceScreen().getPreference(x);
            for(int y = 0; y < lol.getPreferenceCount(); y++){
                final Preference pref = lol.getPreference(y);
                pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        switch (preference.getKey()){
                            case "sign_out":
                                sessionManager.logoutUser();
                                break;
                        }
                        return false;
                    }

                });
            }
        }
    }
}

