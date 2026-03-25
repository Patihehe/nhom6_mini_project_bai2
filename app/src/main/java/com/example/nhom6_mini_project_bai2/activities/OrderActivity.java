package com.example.nhom6_mini_project_bai2.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom6_mini_project_bai2.adapters.OrderDetailAdapter;
import com.example.nhom6_mini_project_bai2.R;
import com.example.nhom6_mini_project_bai2.dal.AppDatabase;
import com.example.nhom6_mini_project_bai2.entities.Order;
import com.example.nhom6_mini_project_bai2.entities.OrderDetail;

import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView rvOrderItems;
    private TextView tvTotal, tvOrderTitle;
    private Button btnCheckout;
    private AppDatabase db;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        db = AppDatabase.getInstance(this);
        orderId = getIntent().getIntExtra("order_id", -1);

        rvOrderItems = findViewById(R.id.rvOrderItems);
        tvTotal = findViewById(R.id.tvTotal);
        tvOrderTitle = findViewById(R.id.tvOrderTitle);
        btnCheckout = findViewById(R.id.btnCheckout);

        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));

        loadOrderDetails();

        btnCheckout.setOnClickListener(v -> {
            Order order = db.shoppingDao().getOrderById(orderId);
            if (order != null) {
                order.status = "Paid";
                db.shoppingDao().updateOrder(order);
                Toast.makeText(this, "Order Paid Successfully!", Toast.LENGTH_LONG).show();
                tvOrderTitle.setText("Invoice (Paid)");
                btnCheckout.setEnabled(false);
                btnCheckout.setText("PAID");
            }
        });
    }

    private void loadOrderDetails() {
        List<OrderDetail> details = db.shoppingDao().getOrderDetailsByOrder(orderId);
        OrderDetailAdapter adapter = new OrderDetailAdapter(details, db);
        rvOrderItems.setAdapter(adapter);

        double total = 0;
        for (OrderDetail d : details) {
            total += (d.unitPrice * d.quantity);
        }
        tvTotal.setText("Total: $" + String.format("%.2f", total));

        Order order = db.shoppingDao().getOrderById(orderId);
        if (order != null && "Paid".equals(order.status)) {
            tvOrderTitle.setText("Invoice (Paid)");
            btnCheckout.setEnabled(false);
            btnCheckout.setText("PAID");
        }
    }
}
