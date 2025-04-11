package vn.edu.tlu.cse.tuongthiduyen.quanlycuahangdientu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerActivity extends AppCompatActivity {
    private RecyclerView rvProducts;
    private Button btnViewCart;
    private DatabaseHelper dbHelper; // Thay ShoppingDAO
    private ProductAdapter productAdapter;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        rvProducts = findViewById(R.id.rvProducts);
        btnViewCart = findViewById(R.id.btnViewCart);
        dbHelper = new DatabaseHelper(this); // Khởi tạo DatabaseHelper

        userEmail = getIntent().getStringExtra("user_email");

        // Thiết lập RecyclerView
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        List<Product> productList = dbHelper.getAllProducts();
        // phân quyền thêm sp cho khách hàng
        productAdapter = new ProductAdapter(productList, true, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onAddToCartClick(Product product) {
                //Gọi để thêm sp vào giỏ hàng.() default 1
                dbHelper.addToCart(userEmail, product.getId(), 1);
                Toast.makeText(CustomerActivity.this, "Đã thêm " + product.getName() + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEditClick(Product product) {
                // NOT
            }

            @Override
            public void onDeleteClick(Product product) {
                // NOT
            }
        });
        // Gắn adapter vào RecyclerView để hiển thị, adapter render cuon từng dòng sản phẩm
        // hiển thị lên màn hình NV -> DUYỆT TỪNG SP, KÈM NÚT SỬA XÓA; KHÁCH HÀNG -> DUYỆT TỪNG SP, KÈM NÚT THÊM VÀO GIỎ HÀNG
        rvProducts.setAdapter(productAdapter);
        //Xem cart -> CartActivity xđ giỏ hàng userEmail
        btnViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerActivity.this, CartActivity.class);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
            }
        });
    }
}