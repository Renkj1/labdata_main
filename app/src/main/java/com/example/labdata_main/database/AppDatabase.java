package com.example.labdata_main.database;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.labdata_main.dao.MixRatioDao;
import com.example.labdata_main.dao.SpecimenDao;
import com.example.labdata_main.model.MixRatio;
import com.example.labdata_main.model.Specimen;

@Database(entities = {MixRatio.class, Specimen.class}, version = 4)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "AppDatabase";
    private static final String DATABASE_NAME = "labdata_db";
    private static AppDatabase instance;

    public abstract MixRatioDao mixRatioDao();
    public abstract SpecimenDao specimenDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d(TAG, "Running migration from version 1 to version 2");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d(TAG, "Running migration from version 2 to version 3");
            
            // 删除旧表（如果存在）
            database.execSQL("DROP TABLE IF EXISTS specimens");
            
            // 创建新表
            database.execSQL("CREATE TABLE IF NOT EXISTS specimens (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "mix_ratio_id INTEGER NOT NULL, " +
                    "mixing_temperature REAL NOT NULL, " +
                    "mixing_speed REAL NOT NULL, " +
                    "compaction_method TEXT DEFAULT '', " +
                    "creation_time INTEGER NOT NULL, " +
                    "cut_shape TEXT, " +
                    "cut_count INTEGER DEFAULT 1, " +
                    "length REAL DEFAULT 0, " +
                    "width REAL DEFAULT 0, " +
                    "height REAL DEFAULT 0, " +
                    "radius REAL DEFAULT 0, " +
                    "FOREIGN KEY(mix_ratio_id) REFERENCES mix_ratios(id) ON DELETE CASCADE)");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d(TAG, "Running migration from version 3 to version 4");
            
            // 删除旧表
            database.execSQL("DROP TABLE IF EXISTS specimens");
            
            // 重新创建表，包含新的索引
            database.execSQL("CREATE TABLE specimens (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "mix_ratio_id INTEGER NOT NULL, " +
                    "mixing_temperature REAL NOT NULL, " +
                    "mixing_speed REAL NOT NULL, " +
                    "compaction_method TEXT NOT NULL DEFAULT '', " +
                    "creation_time INTEGER NOT NULL, " +
                    "cut_shape TEXT, " +
                    "cut_count INTEGER NOT NULL DEFAULT 1, " +
                    "length REAL NOT NULL DEFAULT 0, " +
                    "width REAL NOT NULL DEFAULT 0, " +
                    "height REAL NOT NULL DEFAULT 0, " +
                    "radius REAL NOT NULL DEFAULT 0, " +
                    "FOREIGN KEY(mix_ratio_id) REFERENCES mix_ratios(id) ON DELETE CASCADE)");

            // 创建索引
            database.execSQL("CREATE INDEX index_specimens_mix_ratio_id ON specimens(mix_ratio_id)");
        }
    };

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            Log.d(TAG, "Creating new database instance");
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME)
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build();
            Log.d(TAG, "Database instance created");
        }
        return instance;
    }
}
