package vn.edu.tlu.cse.tuongthiduyen.quanlycuahangdientu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
//
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private boolean isCustomer; // True: khách hàng, False: nhân viên
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onAddToCartClick(Product product);
        void onEditClick(Product product);
        void onDeleteClick(Product product);
    }

    public ProductAdapter(List<Product> productList, boolean isCustomer, OnProductClickListener listener) {
        this.productList = productList;
        this.isCustomer = isCustomer;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_computer, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductType.setText("Hãng: " + product.getType());
        holder.tvProductPrice.setText("Giá bán: " + product.getPrice() + " VNĐ");
        holder.tvProductQuantity.setText("Số lượng tồn kho: " + product.getQuantity());

        if (isCustomer) {
            holder.btnAddToCart.setVisibility(View.VISIBLE);
            holder.btnEditProduct.setVisibility(View.GONE);
            holder.btnDeleteProduct.setVisibility(View.GONE);
            holder.btnAddToCart.setOnClickListener(v -> listener.onAddToCartClick(product));
        } else {
            holder.btnAddToCart.setVisibility(View.GONE);
            holder.btnEditProduct.setVisibility(View.VISIBLE);
            holder.btnDeleteProduct.setVisibility(View.VISIBLE);
            holder.btnEditProduct.setOnClickListener(v -> listener.onEditClick(product));
            holder.btnDeleteProduct.setOnClickListener(v -> listener.onDeleteClick(product));
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductType, tvProductPrice, tvProductQuantity;
        Button btnAddToCart, btnEditProduct, btnDeleteProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductType = itemView.findViewById(R.id.tvProductType);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnEditProduct = itemView.findViewById(R.id.btnEditProduct);
            btnDeleteProduct = itemView.findViewById(R.id.btnDeleteProduct);
        }
    }
}