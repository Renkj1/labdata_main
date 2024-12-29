package com.example.labdata_main.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MaterialDao {
    @Query("SELECT * FROM material_properties WHERE type = :type")
    List<MaterialProperty> getPropertiesByType(String type);

    @Insert
    void insertProperty(MaterialProperty property);

    @Insert
    void insertMixDesign(MixDesign design);

    @Query("SELECT MAX(designGroup) FROM mix_designs")
    int getLatestDesignGroup();

    @Query("SELECT * FROM mix_designs WHERE designGroup = :groupId")
    List<MixDesign> getMixDesignsByGroup(int groupId);
}
