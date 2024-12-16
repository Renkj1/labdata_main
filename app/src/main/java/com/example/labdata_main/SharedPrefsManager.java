package com.example.labdata_main;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.example.labdata_main.model.Equipment;

public class SharedPrefsManager {
    private static final String PREFS_NAME = "LabDataPrefs";
    private static final String KEY_EQUIPMENT_LIST = "equipment_list";
    private static final String KEY_INITIALIZED = "initialized";
    
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public SharedPrefsManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveEquipmentList(List<Equipment> equipmentList) {
        String json = gson.toJson(equipmentList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EQUIPMENT_LIST, json);
        editor.apply();
    }

    public ArrayList<Equipment> getEquipmentList() {
        String json = sharedPreferences.getString(KEY_EQUIPMENT_LIST, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<Equipment>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void setInitialized(boolean initialized) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_INITIALIZED, initialized);
        editor.apply();
    }

    public boolean isInitialized() {
        return sharedPreferences.getBoolean(KEY_INITIALIZED, false);
    }

    public void clearAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
