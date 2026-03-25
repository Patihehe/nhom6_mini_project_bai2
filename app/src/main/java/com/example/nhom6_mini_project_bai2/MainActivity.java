package com.example.nhom6_mini_project_bai2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);

        seedData();
        loadData();
    }

    private void seedData() {
        if (db.shoppingDao().getAllCategories().isEmpty()) {
            db.shoppingDao().insertCategory(new Category("Electronics"));
            db.shoppingDao().insertCategory(new Category("Clothing"));
            db.shoppingDao().insertCategory(new Category("Books"));

            List<Category> cats = db.shoppingDao().getAllCategories();
            db.shoppingDao().insertProduct(new Product("Smartphone", 500, "Flagship device with high-end specs.", cats.get(0).categoryId));
            db.shoppingDao().insertProduct(new Product("Laptop", 1200, "Powerful workhorse for developers.", cats.get(0).categoryId));
            db.shoppingDao().insertProduct(new Product("T-Shirt", 20, "100% Cotton, comfortable and durable.", cats.get(1).categoryId));
            db.shoppingDao().insertProduct(new Product("Jeans", 50, "Stylish blue denim jeans.", cats.get(1).categoryId));
            db.shoppingDao().insertProduct(new Product("Java Programming", 45, "Master Java with this comprehensive guide.", cats.get(2).categoryId));

            db.shoppingDao().insertUser(new User("admin", "123", "System Admin"));
            db.shoppingDao().insertUser(new User("user", "123", "Regular User"));
        }
    }

    private void loadData() {
        List<Category> categories = db.shoppingDao().getAllCategories();
        CategoryAdapter catAdapter = new CategoryAdapter(categories, (category, isSelected) -> {
            if (isSelected) {
                List<Product> filteredProducts = db.shoppingDao().getProductsByCategory(category.categoryId);
                setProductAdapter(filteredProducts);
            } else {
                // Show all if deselected
                setProductAdapter(db.shoppingDao().getAllProducts());
            }
        });
        rvCategories.setAdapter(catAdapter);

        List<Product> allProducts = db.shoppingDao().getAllProducts();
        setProductAdapter(allProducts);
    }


}