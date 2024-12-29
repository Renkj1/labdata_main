package com.example.labdata_main.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
    entities = {MaterialProperty.class, MixDesign.class}, 
    version = 1,
    exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract MaterialDao materialDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "labdata.db")
                            .allowMainThreadQueries() // 仅用于演示，实际应使用异步
                            .build();
                    
                    // 初始化默认材料属性
                    initializeDefaultProperties(INSTANCE);
                }
            }
        }
        return INSTANCE;
    }

    private static void initializeDefaultProperties(AppDatabase db) {
        // 检查是否已经初始化
        if (db.materialDao().getPropertiesByType("asphalt").isEmpty()) {
            // 添加沥青属性
            MaterialProperty asphalt1 = new MaterialProperty();
            asphalt1.type = "asphalt";
            asphalt1.name = "沥青·中国石化";
            asphalt1.code = "A01";
            db.materialDao().insertProperty(asphalt1);

            MaterialProperty asphalt2 = new MaterialProperty();
            asphalt2.type = "asphalt";
            asphalt2.name = "沥青·中国石油";
            asphalt2.code = "A02";
            db.materialDao().insertProperty(asphalt2);

            // 添加石子属性
            MaterialProperty stone1 = new MaterialProperty();
            stone1.type = "stone";
            stone1.name = "石灰岩";
            stone1.code = "R01";
            db.materialDao().insertProperty(stone1);

            MaterialProperty stone2 = new MaterialProperty();
            stone2.type = "stone";
            stone2.name = "玄武岩";
            stone2.code = "R02";
            db.materialDao().insertProperty(stone2);

            // 添加沙子属性
            MaterialProperty sand = new MaterialProperty();
            sand.type = "sand";
            sand.name = "沙子·标准砂";
            sand.code = "S01";
            db.materialDao().insertProperty(sand);
        }
    }
}
