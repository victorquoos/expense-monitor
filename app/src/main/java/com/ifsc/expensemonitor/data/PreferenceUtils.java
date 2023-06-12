package com.ifsc.expensemonitor.data;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
    public static final String PREFERENCE_NAME = "NomePreferencia";
    public static final String KEY_ORDENACAO = "ordenacao";
    public static final String KEY_MOVE_PAID_TO_END = "move_paid_to_end";
    public static final String KEY_SETTINGS_SET = "settings_set";

    public static final String SORT_DATE = "date_asc";
    public static final String SORT_VALUE_ASC = "value_asc";
    public static final String SORT_VALUE_DESC = "value_desc";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    public void setPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        this.preferenceChangeListener = listener;
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    public void unregisterPreferenceChangeListener() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    public PreferenceUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setOrdenacao(String ordenacao) {
        editor.putString(KEY_ORDENACAO, ordenacao).apply();
    }

    public String getOrdenacao() {
        return sharedPreferences.getString(KEY_ORDENACAO, "data_asc");
    }

    public void setMovePaidToEnd(boolean movePaidToEnd) {
        editor.putBoolean(KEY_MOVE_PAID_TO_END, movePaidToEnd).apply();
    }

    public boolean getMovePaidToEnd() {
        return sharedPreferences.getBoolean(KEY_MOVE_PAID_TO_END, true);
    }

    public void setSettingsSet(boolean settingsSet) {
        editor.putBoolean(KEY_SETTINGS_SET, settingsSet).apply();
    }

    public boolean getSettingsSet() {
        return sharedPreferences.getBoolean(KEY_SETTINGS_SET, false);
    }

}

