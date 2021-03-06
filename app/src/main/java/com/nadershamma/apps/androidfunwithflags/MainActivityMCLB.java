package com.nadershamma.apps.androidfunwithflags;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.nadershamma.apps.eventhandlers.PreferenceChangeListenerMCLB;
import com.nadershamma.apps.lifecyclehelpers.QuizViewModelMCLB;

public class MainActivityMCLB extends AppCompatActivity {
    public static final String CHOICES = "pref_numberOfChoices";
    public static final String REGIONS = "pref_regionsToInclude";
    private boolean deviceIsPhone = true;
    private boolean preferencesChanged = true;
    private MainActivityFragmentMCLB quizFragment;
    private QuizViewModelMCLB quizViewModelMCLB;
    private OnSharedPreferenceChangeListener preferencesChangeListener;

    private void setSharedPreferencesMCLB() {
        // set default values in the app's SharedPreferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences_mclb, false);

        // Register a listener for shared preferences changes
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(preferencesChangeListener);
    }

    private void screenSetUpMCLB() {
        if (getScreenSizeMCLB() == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                getScreenSizeMCLB() == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            deviceIsPhone = false;
        }
        if (deviceIsPhone) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.quizViewModelMCLB = ViewModelProviders.of(this).get(QuizViewModelMCLB.class);
        this.preferencesChangeListener = new PreferenceChangeListenerMCLB(this);
        setContentView(R.layout.activity_main_mclb);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setSharedPreferencesMCLB();
        this.screenSetUpMCLB();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (preferencesChanged) {
            this.quizFragment = (MainActivityFragmentMCLB) getSupportFragmentManager()
                    .findFragmentById(R.id.quizFragment);
            this.quizViewModelMCLB.setGuessRowsMCLB(PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(CHOICES, null));
            this.quizViewModelMCLB.setRegionsSetMCLB(PreferenceManager.getDefaultSharedPreferences(this)
                    .getStringSet(REGIONS, null));

            this.quizFragment.resetQuizMCLB();

            preferencesChanged = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main_mclb, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent preferencesIntent = new Intent(this, SettingsActivityMCLB.class);
        startActivity(preferencesIntent);
        return super.onOptionsItemSelected(item);
    }

    public int getScreenSizeMCLB() {
        return getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
    }

    public MainActivityFragmentMCLB getQuizFragment() {
        return this.quizFragment;
    }

    public QuizViewModelMCLB getQuizViewModel() {
        return quizViewModelMCLB;
    }

    public static String getCHOICESMCLB() {
        return CHOICES;
    }

    public static String getREGIONSMCLB() {
        return REGIONS;
    }

    public void setPreferencesChangedMCLB(boolean preferencesChanged) {
        this.preferencesChanged = preferencesChanged;
    }


}
