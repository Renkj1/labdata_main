package com.example.labdata_main;

import android.app.Application;
import com.example.labdata_main.database.AppDatabase;

public class LabDataApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化数据库
        AppDatabase.getInstance(this);
    }
}
