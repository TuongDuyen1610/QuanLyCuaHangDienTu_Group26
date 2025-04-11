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
    private TextView tvPromoCode, tvDiscountAmount, tvTotalAfterDiscount, tvTotal;
    private Button btnCheckout;
    private DatabaseHelper dbHelper; // Sử dụng DatabaseHelper
    private CartAdapter cartAdapter;
    private String userEmail;
    private List<CartItem> cartItems;

// gọi khi CartActivity khởi tạo.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rvCart = findViewById(R.id.rvCart);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        tvPromoCode = findViewById(R.id.tvPromoCode);
        tvDiscountAmount = findViewById(R.id.tvDiscountAmount);
        tvTotalAfterDiscount = findViewById(R.id.tvTotalAfterDiscount);

        dbHelper = new DatabaseHelper(this);

        //. Email lấy ds sp trong giỏ hàng.
        userEmail = getIntent().getStringExtra("user_email");

        // Thiết lập RecyclerView hiển thị dssp trong RecyclerView dạng dọc.
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        loadCart();     // hiển thị dssp trong giỏ hàng.



//        //pay->check cart
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItems.isEmpty()) {
                    Toast.makeText(CartActivity.this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int total = calculateTotal();  // Tính tổng tiền
                Promotion bestPromo = dbHelper.getBestPromotion(total);  // Khởi tạo hàm getBestPromotion() trong DatabaseHelper return bestPromo
                                                                        // Tìm mã khuyến mãi tốt nhất
                double discount = 0;
                String promoMessage = "";
                String promoCode = "Không có";

                if (bestPromo != null) {
                    discount = total * (bestPromo.getDiscountAmount() / 100.0); // Tính số tiền giảm , cách tinh là tổng tiền * khuyến mãi / 100
                    // Khởi tạo hàm getDiscountAmount() trong Promotion để lấy giá trị khuyến mãi
                    promoCode = bestPromo.getTitle();
                    promoMessage = "Đã áp dụng mã: " + promoCode + "\nGiảm: " + formatCurrency(discount);
                } else {
                    promoMessage = "Không có mã giảm giá nào phù hợp.";
                }

                double totalAfterDiscount = total - discount;

                Toast.makeText(CartActivity.this,
                        promoMessage + "\nTổng thanh toán: " + formatCurrency(totalAfterDiscount),
                        Toast.LENGTH_LONG).show();

                // ✅ Hiển thị các TextView lên màn hình -> MESSAGE KHUYẾN MÃI
                tvPromoCode.setVisibility(View.VISIBLE);
                tvPromoCode.setText("Mã giảm giá: " + promoCode);

                tvDiscountAmount.setVisibility(View.VISIBLE);
                tvDiscountAmount.setText("Bạn được giảm: " + formatCurrency(discount));

                tvTotalAfterDiscount.setVisibility(View.VISIBLE);
                tvTotalAfterDiscount.setText("Tổng sau giảm: " + formatCurrency(totalAfterDiscount));

                // ✅ Lưu đơn hàng vào database của bảng orders
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                dbHelper.createOrder(userEmail, currentDate, (int) totalAfterDiscount, "Đã thanh toán");

                // ✅ Xóa giỏ hàng
                dbHelper.clearCart(userEmail);
                loadCart();
            }
        });

    }
    //hiển thị giá đẹp
    private String formatCurrency(double amount) {
            return String.format("%,.0f VND", amount);
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