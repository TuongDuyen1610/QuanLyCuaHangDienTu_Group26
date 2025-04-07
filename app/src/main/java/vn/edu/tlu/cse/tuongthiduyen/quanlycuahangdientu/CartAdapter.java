package vn.edu.tlu.cse.tuongthiduyen.quanlycuahangdientu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private Context context;

    // Constructor nhận vào Context để có thể sử dụng trong cả CartActivity và CheckoutActivity
    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.tvCartProductName.setText(item.getProductName());
        holder.tvCartQuantity.setText("Số lượng: " + item.getQuantity());
        holder.tvCartPrice.setText("Giá: " + (item.getPrice() * item.getQuantity()) + " VND");
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvCartProductName, tvCartQuantity, tvCartPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCartProductName = itemView.findViewById(R.id.tvCartProductName);
            tvCartQuantity = itemView.findViewById(R.id.tvCartQuantity);
            tvCartPrice = itemView.findViewById(R.id.tvCartPrice);
        }
    }
}