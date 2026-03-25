package com.example.nhom6_mini_project_bai2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom6_mini_project_bai2.activities.LoginActivity;
import com.example.nhom6_mini_project_bai2.activities.OrderActivity;
import com.example.nhom6_mini_project_bai2.activities.ProductDetailActivity;
import com.example.nhom6_mini_project_bai2.adapters.CategoryAdapter;
import com.example.nhom6_mini_project_bai2.adapters.ProductAdapter;
import com.example.nhom6_mini_project_bai2.dal.AppDatabase;
import com.example.nhom6_mini_project_bai2.entities.Category;
import com.example.nhom6_mini_project_bai2.entities.Order;
import com.example.nhom6_mini_project_bai2.entities.OrderDetail;
import com.example.nhom6_mini_project_bai2.entities.Product;
import com.example.nhom6_mini_project_bai2.entities.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView tvWelcome;
    private Button btnAuth, btnGoToCart;
    private RecyclerView rvCategories, rvProducts;
    private AppDatabase db;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);

        initViews();
        seedData();
        loadData();
        updateUI();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        btnAuth = findViewById(R.id.btnAuth);
        btnGoToCart = findViewById(R.id.btnGoToCart);
        rvCategories = findViewById(R.id.rvCategories);
        rvProducts = findViewById(R.id.rvProducts);

        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvProducts.setLayoutManager(new LinearLayoutManager(this));

        btnAuth.setOnClickListener(v -> {
            if (prefs.getBoolean("is_logged_in", false)) {
                // Logout
                prefs.edit().clear().apply();
                updateUI();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        btnGoToCart.setOnClickListener(v -> {
            int userId = prefs.getInt("user_id", -1);
            Order pendingOrder = db.shoppingDao().getPendingOrderByUser(userId);
            if (pendingOrder != null) {
                Intent intent = new Intent(this, OrderActivity.class);
                intent.putExtra("order_id", pendingOrder.orderId);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void setProductAdapter(List<Product> products) {
        ProductAdapter prodAdapter = new ProductAdapter(products, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("product_id", product.productId);
                startActivity(intent);
            }

            @Override
            public void onAddToCartClick(Product product) {
                handleAddToCart(product);
            }
        });
        rvProducts.setAdapter(prodAdapter);
    }

    private void handleAddToCart(Product product) {
        if (!prefs.getBoolean("is_logged_in", false)) {
            Toast.makeText(this, "Please login to add to cart", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        int userId = prefs.getInt("user_id", -1);
        Order pendingOrder = db.shoppingDao().getPendingOrderByUser(userId);
        if (pendingOrder == null) {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            long orderId = db.shoppingDao().insertOrder(new Order(userId, date, "Pending"));
            pendingOrder = db.shoppingDao().getOrderById((int) orderId);
        }

        db.shoppingDao().insertOrderDetail(new OrderDetail(pendingOrder.orderId, product.productId, 1, product.price));
        Toast.makeText(this, "Added " + product.productName + " to cart", Toast.LENGTH_SHORT).show();
        updateUI(); // Refresh UI to show Go to Cart button
    }

    private void updateUI() {
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        if (isLoggedIn) {
            int userId = prefs.getInt("user_id", -1);
            User user = db.shoppingDao().getUserById(userId);
            tvWelcome.setText("Hello, " + (user != null ? user.fullName : "User"));
            btnAuth.setText("Logout");

            Order pendingOrder = db.shoppingDao().getPendingOrderByUser(userId);
            btnGoToCart.setVisibility(pendingOrder != null ? View.VISIBLE : View.GONE);
        } else {
            tvWelcome.setText("Welcome!");
            btnAuth.setText("Login");
            btnGoToCart.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}