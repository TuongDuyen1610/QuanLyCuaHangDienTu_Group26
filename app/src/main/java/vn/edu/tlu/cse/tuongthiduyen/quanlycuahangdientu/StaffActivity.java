package vn.edu.tlu.cse.tuongthiduyen.quanlycuahangdientu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
// quản lý sản phẩm (thêm, sửa, xóa), điều hướng đến danh sách đơn hàng
public class StaffActivity extends AppCompatActivity {
    private EditText etProductName, etProductType, etProductPrice, etProductQuantity;
    private Button btnAddProduct, btnViewOrders;
    private RecyclerView rvProducts;
    private DatabaseHelper dbHelper; // Sử dụng DatabaseHelper thay vì ShoppingDAO
    private ProductAdapter productAdapter;
    private Product editingProduct = null;

    private Button btnManagePromotions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        etProductName = findViewById(R.id.etProductName);
        etProductType = findViewById(R.id.etProductType);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductQuantity = findViewById(R.id.etProductQuantity);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnViewOrders = findViewById(R.id.btnViewOrders);
        btnManagePromotions = findViewById(R.id.btnManagePromotions);
        rvProducts = findViewById(R.id.rvProducts);
        dbHelper = new DatabaseHelper(this); // Khởi tạo DatabaseHelper

        // Thiết lập RecyclerView -
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        loadProducts();

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etProductName.getText().toString().trim();
                String type = etProductType.getText().toString().trim();
                String priceStr = etProductPrice.getText().toString().trim();
                String quantityStr = etProductQuantity.getText().toString().trim();

                if (name.isEmpty() || type.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()) {
                    Toast.makeText(StaffActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int price;
                int quantity;
                try {
                    price = Integer.parseInt(priceStr);
                    quantity = Integer.parseInt(quantityStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(StaffActivity.this, "Giá và số lượng phải là số hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (editingProduct == null) {
                    // Thêm sản phẩm mới
                    Product product = new Product();
                    product.setName(name);
                    product.setType(type);
                    product.setPrice(price);
                    product.setQuantity(quantity);
                    dbHelper.addProduct(product);
                    Toast.makeText(StaffActivity.this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    // Sửa sản phẩm
                    editingProduct.setName(name);
                    editingProduct.setType(type);
                    editingProduct.setPrice(price);
                    editingProduct.setQuantity(quantity);
                    dbHelper.updateProduct(editingProduct);
                    Toast.makeText(StaffActivity.this, "Sửa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                    editingProduct = null;
                    btnAddProduct.setText("Thêm sản phẩm");
                }

                clearInputs();
                loadProducts();
            }
        });

        btnViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffActivity.this, OrderListActivity.class);
                startActivity(intent);
            }
        });

        btnManagePromotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffActivity.this, PromotionListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadProducts() {
        List<Product> productList = dbHelper.getAllProducts();
        productAdapter = new ProductAdapter(productList, false, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onAddToCartClick(Product product) {
                // Không dùng cho nhân viên
            }
            //Sửa và xóa sản phẩm
            @Override
            public void onEditClick(Product product) {
                editingProduct = product;
                etProductName.setText(product.getName());
                etProductType.setText(product.getType());
                etProductPrice.setText(String.valueOf(product.getPrice()));
                etProductQuantity.setText(String.valueOf(product.getQuantity()));
                btnAddProduct.setText("Cập nhật sản phẩm");
            }

            @Override
            public void onDeleteClick(Product product) {
                dbHelper.deleteProduct(product.getId());
                Toast.makeText(StaffActivity.this, "Xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                loadProducts();
            }
        });
        rvProducts.setAdapter(productAdapter);
    }

    private void clearInputs() {
        etProductName.setText("");
        etProductType.setText("");
        etProductPrice.setText("");
        etProductQuantity.setText("");
    }
}