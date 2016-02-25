package com.raystone.ray.popmoviesstage1;

import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ray on 2/1/2016.
 */
public class SettingActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener{


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        bindPreferenceValueToSummary(findPreference("sort"));

    }

    private void bindPreferenceValueToSummary(Preference preference)
    {
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(),""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        String stringValue = newValue.toString();
        if (preference instanceof ListPreference)
        {
            ListPreference listPreference = (ListPreference)preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0)
                preference.setSummary(listPreference.getEntries()[prefIndex]);
        }
        return true;
    }
}
