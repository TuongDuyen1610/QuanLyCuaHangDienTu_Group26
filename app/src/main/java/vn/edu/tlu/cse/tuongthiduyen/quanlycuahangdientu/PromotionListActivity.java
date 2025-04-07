package vn.edu.tlu.cse.tuongthiduyen.quanlycuahangdientu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PromotionListActivity extends AppCompatActivity {
    RecyclerView recyclerPromotions;
    PromotionAdapter adapter;
    ArrayList<Promotion> promotionList;
    DatabaseHelper dbHelper;
    Button btnAddPromotion;

    @Override
    protected void onResume() {
        super.onResume();
        loadPromotions();// Đảm bảo dữ liệu được tải lại khi Activity được tiếp tục
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_list);

        recyclerPromotions = findViewById(R.id.recyclerPromotions);
        btnAddPromotion = findViewById(R.id.btnAddPromotion);
        dbHelper = new DatabaseHelper(this);
        promotionList = new ArrayList<>();

        recyclerPromotions.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PromotionAdapter(promotionList, this);
        recyclerPromotions.setAdapter(adapter);

        btnAddPromotion.setOnClickListener(v -> {
            startActivity(new Intent(PromotionListActivity.this, PromotionFormActivity.class));
        });
    }

    private void loadPromotions() {
        promotionList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_PROMOTIONS, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Promotion promo = new Promotion(
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MIN_ORDER)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DISCOUNT))
            );
            promotionList.add(promo);
        }

        cursor.close(); // Đảm bảo đóng cursor sau khi sử dụng
        db.close(); // Đóng cơ sở dữ liệu

        adapter.notifyDataSetChanged(); // Cập nhật RecyclerView sau khi danh sách thay đổi
    }
}
