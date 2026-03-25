package com.example.nhom6_mini_project_bai2.dal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.nhom6_mini_project_bai2.entities.Category;
import com.example.nhom6_mini_project_bai2.entities.Order;
import com.example.nhom6_mini_project_bai2.entities.OrderDetail;
import com.example.nhom6_mini_project_bai2.entities.Product;
import com.example.nhom6_mini_project_bai2.entities.User;

@Database(entities = {User.class, Category.class, Product.class, Order.class, OrderDetail.class},
        version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract ShoppingDao shoppingDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "shopping_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}