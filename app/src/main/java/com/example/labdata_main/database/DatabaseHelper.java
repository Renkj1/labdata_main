package com.example.labdata_main.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.labdata_main.model.Material;
import com.example.labdata_main.model.MixRatio;
import com.example.labdata_main.model.Project;
import java.util.List;

@Database(entities = {Project.class, MixRatio.class, Material.class}, version = 1)
public abstract class DatabaseHelper extends RoomDatabase {
    private static final String DATABASE_NAME = "labdata.db";
    private static DatabaseHelper instance;

    public abstract ProjectDao projectDao();
    public abstract MixRatioDao mixRatioDao();
    public abstract MaterialDao materialDao();

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    DatabaseHelper.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public List<Project> getAllProjects() {
        return projectDao().getAllProjects();
    }

    public List<MixRatio> getAllMixRatios() {
        return mixRatioDao().getAllMixRatios();
    }

    public List<Material> getAllMaterials() {
        return materialDao().getAllMaterials();
    }

    public void insertProject(Project project) {
        projectDao().insert(project);
    }

    public void insertMixRatio(MixRatio mixRatio) {
        mixRatioDao().insert(mixRatio);
    }

    public void insertMaterial(Material material) {
        materialDao().insert(material);
    }
}
