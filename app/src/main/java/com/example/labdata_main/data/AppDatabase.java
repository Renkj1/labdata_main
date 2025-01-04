package com.example.labdata_main.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(
    entities = {MaterialProperty.class, MixDesign.class}, 
    version = 2,
    exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "labdata.db";

    public abstract MaterialDao materialDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 备份旧表
            database.execSQL("ALTER TABLE material_properties RENAME TO material_properties_old");
            
            // 创建新表
            database.execSQL("CREATE TABLE material_properties (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "type TEXT NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "code TEXT NOT NULL)");
            
            // 复制数据
            database.execSQL("INSERT INTO material_properties (id, type, name, code) " +
                    "SELECT id, type, name, code FROM material_properties_old");
            
            // 删除旧表
            database.execSQL("DROP TABLE material_properties_old");
        }
    };

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .allowMainThreadQueries() // 仅用于演示，实际应使用异步
                            .addMigrations(MIGRATION_1_2)
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

            // 添加砂子属性
            MaterialProperty sand1 = new MaterialProperty();
            sand1.type = "sand";
            sand1.name = "机制砂";
            sand1.code = "S01";
            db.materialDao().insertProperty(sand1);

            MaterialProperty sand2 = new MaterialProperty();
            sand2.type = "sand";
            sand2.name = "天然砂";
            sand2.code = "S02";
            db.materialDao().insertProperty(sand2);

            // 添加石料属性
            MaterialProperty stone1 = new MaterialProperty();
            stone1.type = "stone";
            stone1.name = "玄武岩";
            stone1.code = "R01";
            db.materialDao().insertProperty(stone1);

            MaterialProperty stone2 = new MaterialProperty();
            stone2.type = "stone";
            stone2.name = "花岗岩";
            stone2.code = "R02";
            db.materialDao().insertProperty(stone2);
        }
    }
}
