package vn.edu.tlu.cse.tuongthiduyen.quanlycuahangdientu;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder> {

    private ArrayList<Promotion> promotionList;
    private Context context;

    public PromotionAdapter(ArrayList<Promotion> promotionList, Context context) {
        this.promotionList = promotionList;
        this.context = context;
    }

    @NonNull
    @Override
    public PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_promotion, parent, false);
        return new PromotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionViewHolder holder, int position) {
        Promotion promo = promotionList.get(position);

        holder.tvTitle.setText(promo.getTitle());
        holder.tvMinOrder.setText("Đơn tối thiểu: " + promo.getMinOrderValue() + "đ");
        holder.tvDiscount.setText("Giảm: " + promo.getDiscountAmount() + "%");

        // Cập nhật contentDescription cho các phần tử UI để hỗ trợ accessibility
        holder.tvTitle.setContentDescription("Tiêu đề khuyến mãi: " + promo.getTitle());
        holder.tvMinOrder.setContentDescription("Giá trị đơn hàng tối thiểu: " + promo.getMinOrderValue() + "đ");
        holder.tvDiscount.setContentDescription("Giảm giá: " + promo.getDiscountAmount() + "%");

        holder.btnEdit.setContentDescription("Chỉnh sửa khuyến mãi");

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, PromotionFormActivity.class);
            intent.putExtra("edit", true);
            intent.putExtra("id", promo.getId());
            intent.putExtra("title", promo.getTitle());
            intent.putExtra("min_order", promo.getMinOrderValue());
            intent.putExtra("discount", promo.getDiscountAmount());
            context.startActivity(intent);
        });

        holder.btnDelete.setContentDescription("Xóa khuyến mãi");

        holder.btnDelete.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            int rowsDeleted = db.delete(
                    DatabaseHelper.TABLE_PROMOTIONS,
                    DatabaseHelper.COLUMN_ID + "=?",
                    new String[]{promo.getId()}
            );

            db.close();

            if (rowsDeleted > 0) {
                promotionList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, promotionList.size());
                Toast.makeText(context, "Đã xóa khuyến mãi!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return promotionList.size();
    }

    public static class PromotionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvMinOrder, tvDiscount;
        Button btnEdit, btnDelete;

        public PromotionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMinOrder = itemView.findViewById(R.id.tvMinOrder);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
