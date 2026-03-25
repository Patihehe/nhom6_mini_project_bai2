package com.example.nhom6_mini_project_bai2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhom6_mini_project_bai2.R;
import com.example.nhom6_mini_project_bai2.dal.AppDatabase;
import com.example.nhom6_mini_project_bai2.entities.Order;
import com.example.nhom6_mini_project_bai2.entities.OrderDetail;
import com.example.nhom6_mini_project_bai2.entities.Product;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView tvName, tvPrice, tvDescription;
    private Button btnAddToCart;
    private AppDatabase db;
    private SharedPreferences prefs;
    private int productId;
    private boolean pendingAdd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = AppDatabase.getInstance(this);
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        productId = getIntent().getIntExtra("product_id", -1);

        tvName = findViewById(R.id.tvDetailName);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvDescription = findViewById(R.id.tvDetailDescription);
        btnAddToCart = findViewById(R.id.btnDetailAddToCart);

        Product product = db.shoppingDao().getProductById(productId);
        if (product != null) {
            tvName.setText(product.productName);
            tvPrice.setText("$" + String.format("%.2f", product.price));
            tvDescription.setText(product.description);

            btnAddToCart.setOnClickListener(v -> handleAddToCart(product));
        } else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void handleAddToCart(Product product) {
        if (!prefs.getBoolean("is_logged_in", false)) {
            Toast.makeText(this, "Please login to add to cart", Toast.LENGTH_SHORT).show();
            pendingAdd = true;
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
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // If we were waiting for login, and now we are logged in, try adding again
        if (pendingAdd && prefs.getBoolean("is_logged_in", false)) {
            pendingAdd = false;
            Product product = db.shoppingDao().getProductById(productId);
            if (product != null) {
                handleAddToCart(product);
            }
        }
    }
}