package com.example.nhom6_mini_project_bai2.dal;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nhom6_mini_project_bai2.entities.Category;
import com.example.nhom6_mini_project_bai2.entities.Order;
import com.example.nhom6_mini_project_bai2.entities.OrderDetail;
import com.example.nhom6_mini_project_bai2.entities.Product;
import com.example.nhom6_mini_project_bai2.entities.User;

import java.util.List;

@Dao
public interface ShoppingDao {
    // User
    @Insert
    long insertUser(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users WHERE userId = :userId")
    User getUserById(int userId);

    // Category
    @Insert
    void insertCategory(Category category);

    @Query("SELECT * FROM categories")
    List<Category> getAllCategories();

    // Product
    @Insert
    void insertProduct(Product product);

    @Query("SELECT * FROM products")
    List<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE categoryId = :categoryId")
    List<Product> getProductsByCategory(int categoryId);

    @Query("SELECT * FROM products WHERE productId = :productId")
    Product getProductById(int productId);

    // Order
    @Insert
    long insertOrder(Order order);

    @Update
    void updateOrder(Order order);

    @Query("SELECT * FROM orders WHERE userId = :userId AND status = 'Pending' LIMIT 1")
    Order getPendingOrderByUser(int userId);

    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    Order getOrderById(int orderId);

    // OrderDetail
    @Insert
    void insertOrderDetail(OrderDetail orderDetail);

    @Query("SELECT * FROM order_details WHERE orderId = :orderId")
    List<OrderDetail> getOrderDetailsByOrder(int orderId);
}