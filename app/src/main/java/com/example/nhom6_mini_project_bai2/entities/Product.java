package com.example.nhom6_mini_project_bai2.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "products",
        foreignKeys = @ForeignKey(entity = Category.class,
                parentColumns = "categoryId",
                childColumns = "categoryId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("categoryId")})
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int productId;
    public String productName;
    public double price;
    public String description;
    public int categoryId;

    public Product(String productName, double price, String description, int categoryId) {
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
    }
}
