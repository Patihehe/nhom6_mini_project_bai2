package com.example.nhom6_mini_project_bai2.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_details",
        foreignKeys = {
                @ForeignKey(entity = Order.class,
                        parentColumns = "orderId",
                        childColumns = "orderId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Product.class,
                        parentColumns = "productId",
                        childColumns = "productId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("orderId"), @Index("productId")})
public class OrderDetail {
    @PrimaryKey(autoGenerate = true)
    public int orderDetailId;
    public int orderId;
    public int productId;
    public int quantity;
    public double unitPrice;

    public OrderDetail(int orderId, int productId, int quantity, double unitPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
