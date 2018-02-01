package com.example.rajeev.newer;

import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditNewsSource extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news_source);
    }
    public static class ArticlePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            return false;
        }
    }
}
