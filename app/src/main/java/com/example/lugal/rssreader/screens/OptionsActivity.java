package com.example.lugal.rssreader.screens;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lugal.rssreader.Constants;
import com.example.lugal.rssreader.R;
import com.example.lugal.rssreader.screens.settings.SettingsActivity;

public abstract class OptionsActivity extends Activity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        String themeName = pref.getString("theme", "Light");
        if (themeName.equals("Light")) {
            setTheme(R.style.LightTheme);
        } else if (themeName.equals("Dark")) {
            setTheme(R.style.DarkTheme);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsMenuItem:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, Constants.SETTINGS_ACTIVITY_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == Constants.SETTINGS_ACTIVITY_CODE) {
            if (resultCode == Constants.SETTINGS_ACTIVITY_UPDATED) {
                finish();
                startActivity(getIntent());
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
