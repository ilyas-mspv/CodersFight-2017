package atlascience.bitmaptest.Activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

import atlascience.bitmaptest.R;

/**
 * Created by Ilyas on 19-Apr-17.
 */

public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
