package com.example.nhom6_mini_project_bai2.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int categoryId;
    public String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
