package com.example.labdata_main.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.labdata_main.database.Converters;
import java.util.List;

@Entity(tableName = "mix_ratios")
@TypeConverters({Converters.class})
public class MixRatio {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "creation_time")
    private long creationTime;
    
    @TypeConverters(Converters.class)
    @ColumnInfo(name = "materials")
    private List<MaterialItem> materials;

    // 默认构造函数，Room 将使用这个
    public MixRatio() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public List<MaterialItem> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MaterialItem> materials) {
        this.materials = materials;
    }
}
