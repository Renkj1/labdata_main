package com.example.labdata_main.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "material_properties")
public class MaterialProperty {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String type; // "asphalt", "sand", "stone"
    public String name;
    public String code;
}
