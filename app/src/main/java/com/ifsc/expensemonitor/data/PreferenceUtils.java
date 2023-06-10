package com.ifsc.expensemonitor.data;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
    private static final String PREFERENCE_NAME = "NomePreferencia";
    private static final String KEY_ORDENACAO = "ordenacao";
    private static final String KEY_MOVE_PAID_TO_END = "move_paid_to_end";
    private static final String KEY_SETTINGS_SET = "settings_set";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

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

