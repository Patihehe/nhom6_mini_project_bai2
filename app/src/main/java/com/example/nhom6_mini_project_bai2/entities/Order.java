package com.example.nhom6_mini_project_bai2.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "userId",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("userId")})
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int orderId;
    public int userId;
    public String orderDate;
    public String status; // e.g., "Pending", "Paid"

    public Order(int userId, String orderDate, String status) {
        this.userId = userId;
        this.orderDate = orderDate;
        this.status = status;
    }
}
