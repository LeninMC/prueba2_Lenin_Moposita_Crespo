package com.nadershamma.apps.eventhandlers;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.widget.Toast;

import com.nadershamma.apps.androidfunwithflags.MainActivityMCLB;
import com.nadershamma.apps.androidfunwithflags.R;

import java.util.Set;

public class PreferenceChangeListenerMCLB implements OnSharedPreferenceChangeListener {
    private MainActivityMCLB mainActivityMCLB;

    public PreferenceChangeListenerMCLB(MainActivityMCLB mainActivityMCLB) {
        this.mainActivityMCLB = mainActivityMCLB;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        this.mainActivityMCLB.setPreferencesChangedMCLB(true);

        if (key.equals(this.mainActivityMCLB.getREGIONSMCLB())) {
            this.mainActivityMCLB.getQuizViewModel().setGuessRowsMCLB(sharedPreferences.getString(
                    MainActivityMCLB.CHOICES, null));
            this.mainActivityMCLB.getQuizFragment().resetQuizMCLB();
        } else if (key.equals(this.mainActivityMCLB.getCHOICESMCLB())) {
            Set<String> regions = sharedPreferences.getStringSet(this.mainActivityMCLB.getREGIONSMCLB(),
                    null);
            if (regions != null && regions.size() > 0) {
                this.mainActivityMCLB.getQuizViewModel().setRegionsSetMCLB(regions);
                this.mainActivityMCLB.getQuizFragment().resetQuizMCLB();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                regions.add(this.mainActivityMCLB.getString(R.string.default_region));
                editor.putStringSet(this.mainActivityMCLB.getREGIONSMCLB(), regions);
                editor.apply();
                Toast.makeText(this.mainActivityMCLB, R.string.default_region_message,
                        Toast.LENGTH_LONG).show();
            }
        }

        Toast.makeText(this.mainActivityMCLB, R.string.restarting_quiz,
                Toast.LENGTH_SHORT).show();
    }
}
