package com.example.lugal.rssreader.screens.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.lugal.rssreader.R;

public final class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        String themeName = pref.getString("theme", "Light");
        if (themeName.equals("Light")) {
            setTheme(R.style.LightTheme);
        } else if (themeName.equals("Dark")) {
            setTheme(R.style.DarkTheme);
        }
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        final ListPreference themeList = (ListPreference)findPreference("theme");
        themeList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull final Preference preference
                    , @NonNull final Object newValue) {
                final String theme = newValue.toString();
                final int index = themeList.findIndexOfValue(theme);
                if (index != -1) {
                    themeList.setSummary(themeList.getEntries()[index]);
                    themeList.setValueIndex(index);
                    if ("Light".equals(theme)) {
                        setTheme(R.style.LightTheme);
                    }
                    else if ("Dark".equals(theme)) {
                        setTheme(R.style.DarkTheme);
                    }
                    final Intent activityIntent = getIntent();
                    finish();
                    startActivity(activityIntent);
                }
                return false;
            }
        });
    }


}
