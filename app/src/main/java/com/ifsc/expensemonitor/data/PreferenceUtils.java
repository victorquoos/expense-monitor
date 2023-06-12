package com.ifsc.expensemonitor.data;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    public static final String PREFERENCES_NAME = "my_preferences";
    public static final String ORDINATION = "ordination";
    public static final String MOVE_PAID_TO_END = "move_paid_to_end";
    public static final String DARK_MODE = "dark_mode";

    public static final String DEFAULT_ORDINATION = "date_asc";
    public static final boolean DEFAULT_MOVE_PAID_TO_END = false;
=======
    public static final String PREFERENCE_NAME = "NomePreferencia";
    public static final String KEY_ORDENACAO = "ordenacao";
    public static final String KEY_MOVE_PAID_TO_END = "move_paid_to_end";
    public static final String KEY_SETTINGS_SET = "settings_set";
>>>>>>> 13ef0d5 (menu de opções finalizado)
=======
    public static final String PREFERENCES_NAME = "my_preferences";
    public static final String ORDINATION = "ordination";
    public static final String MOVE_PAID_TO_END = "move_paid_to_end";
    public static final String ALARM_SET = "alarm_set";

    public static final String DEFAULT_ORDINATION = "date_asc";
    public static final boolean DEFAULT_MOVE_PAID_TO_END = false;
>>>>>>> d6b8565 (preferencias padrão)

    public static final String SORT_DATE = "date_asc";
    public static final String SORT_VALUE_ASC = "value_asc";
    public static final String SORT_VALUE_DESC = "value_desc";
<<<<<<< HEAD

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    public PreferenceUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setDefaultSettings();
    }

    private void setDefaultSettings() {
        if (!sharedPreferences.contains(ORDINATION)) {
            setOrdenacao(DEFAULT_ORDINATION);
        }

        if (!sharedPreferences.contains(MOVE_PAID_TO_END)) {
            setMovePaidToEnd(DEFAULT_MOVE_PAID_TO_END);
        }

        if (!sharedPreferences.contains(DARK_MODE)) {
            setDarkMode(false);  // por padrão, o modo escuro está desligado
        }
    }

    public void setPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        this.preferenceChangeListener = listener;
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    public void unregisterPreferenceChangeListener() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    public void setOrdenacao(String ordenacao) {
        editor.putString(ORDINATION, ordenacao).apply();
    }

    public String getOrdenacao() {
        return sharedPreferences.getString(ORDINATION, "data_asc");
    }

    public void setMovePaidToEnd(boolean movePaidToEnd) {
        editor.putBoolean(MOVE_PAID_TO_END, movePaidToEnd).apply();
    }

    public boolean getMovePaidToEnd() {
        return sharedPreferences.getBoolean(MOVE_PAID_TO_END, true);
    }

    public void setDarkMode(boolean isEnabled) {
        editor.putBoolean(DARK_MODE, isEnabled).apply();
    }

    public boolean getDarkMode() {
        return sharedPreferences.getBoolean(DARK_MODE, false);
    }
=======
    private static final String PREFERENCE_NAME = "NomePreferencia";
    private static final String KEY_ORDENACAO = "ordenacao";
    private static final String KEY_MOVE_PAID_TO_END = "move_paid_to_end";
    private static final String KEY_SETTINGS_SET = "settings_set";
=======
>>>>>>> 13ef0d5 (menu de opções finalizado)

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    public PreferenceUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setDefaultSettings();
    }

    private void setDefaultSettings() {
        if (!sharedPreferences.contains(ORDINATION)) {
            setOrdenacao(DEFAULT_ORDINATION);
        }

        if (!sharedPreferences.contains(MOVE_PAID_TO_END)) {
            setMovePaidToEnd(DEFAULT_MOVE_PAID_TO_END);
        }
    }

    public void setPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        this.preferenceChangeListener = listener;
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    public void unregisterPreferenceChangeListener() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    public void setOrdenacao(String ordenacao) {
        editor.putString(ORDINATION, ordenacao).apply();
    }

    public String getOrdenacao() {
        return sharedPreferences.getString(ORDINATION, "data_asc");
    }

    public void setMovePaidToEnd(boolean movePaidToEnd) {
        editor.putBoolean(MOVE_PAID_TO_END, movePaidToEnd).apply();
    }

    public boolean getMovePaidToEnd() {
        return sharedPreferences.getBoolean(MOVE_PAID_TO_END, true);
    }
<<<<<<< HEAD

    public void setSettingsSet(boolean settingsSet) {
        editor.putBoolean(KEY_SETTINGS_SET, settingsSet).apply();
    }

    public boolean getSettingsSet() {
        return sharedPreferences.getBoolean(KEY_SETTINGS_SET, false);
    }

>>>>>>> 8e57fcd (iniciando menu de opções)
=======
>>>>>>> d6b8565 (preferencias padrão)
}

