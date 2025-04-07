package vn.edu.tlu.cse.tuongthiduyen.quanlycuahangdientu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//hiển thị giỏ hàng, tính tổng tiền và xử lý thanh toán.
public class CartActivity extends AppCompatActivity {
    private RecyclerView rvCart;  //Hiển thị danh sách các sản phẩm trong giỏ hàng.
    private TextView tvTotal;
    private Button btnCheckout;
    private DatabaseHelper dbHelper; // Sử dụng DatabaseHelper
    private CartAdapter cartAdapter;
    private String userEmail;
    private List<CartItem> cartItems;
// gọi khi CartActivity khởi tạo.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart); //tk gdien tp trong activity_cart.xml.

        rvCart = findViewById(R.id.rvCart);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        dbHelper = new DatabaseHelper(this); // Khởi tạo DatabaseHelper

        //. Email lấy ds sp trong giỏ hàng.
        userEmail = getIntent().getStringExtra("user_email");

        // Thiết lập RecyclerView hiển thị dssp trong RecyclerView dạng dọc.
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        loadCart(); // hiển thị dssp trong giỏ hàng.

        //pay->check cart
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItems.isEmpty()) {
                    Toast.makeText(CartActivity.this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int total = calculateTotal();
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                dbHelper.createOrder(userEmail, currentDate, total, "Đã thanh toán");
                dbHelper.clearCart(userEmail);
                Toast.makeText(CartActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                loadCart();
            }
        });
    }

    private void loadCart() {
        cartItems = dbHelper.getCartItems(userEmail);
        cartAdapter = new CartAdapter(this, cartItems);
        rvCart.setAdapter(cartAdapter);

        int total = calculateTotal();
        tvTotal.setText("Tổng tiền: " + total + " VND");
    }
    //Duyệt qua danh sách cartItems
    private int calculateTotal() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
}