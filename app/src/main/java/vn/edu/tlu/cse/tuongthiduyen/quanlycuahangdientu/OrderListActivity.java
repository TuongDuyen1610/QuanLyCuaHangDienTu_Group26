package vn.edu.tlu.cse.tuongthiduyen.quanlycuahangdientu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderListActivity extends AppCompatActivity {
    private RecyclerView rvOrders;
    private DatabaseHelper dbHelper; // Sử dụng DatabaseHelper
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        rvOrders = findViewById(R.id.rvOrders);
        dbHelper = new DatabaseHelper(this); // Khởi tạo DatabaseHelper

        // Thiết lập RecyclerView
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        List<Order> orderList = dbHelper.getAllOrders();
        orderAdapter = new OrderAdapter(orderList);
        rvOrders.setAdapter(orderAdapter);
    }
}