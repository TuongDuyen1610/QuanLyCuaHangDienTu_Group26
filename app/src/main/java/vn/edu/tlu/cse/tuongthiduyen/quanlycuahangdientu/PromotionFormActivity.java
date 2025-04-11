package vn.edu.tlu.cse.tuongthiduyen.quanlycuahangdientu;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.UUID;

public class PromotionFormActivity extends AppCompatActivity {
    EditText etTitle, etMinOrder, etDiscount;
    Button btnSave;
    DatabaseHelper dbHelper;
    boolean isEdit = false;
    String promoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_form);

        etTitle = findViewById(R.id.etTitle);
        etMinOrder = findViewById(R.id.etMinOrder);
        etDiscount = findViewById(R.id.etDiscount);
        btnSave = findViewById(R.id.btnSave);
        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("edit")) {
            isEdit = true;
            promoId = intent.getStringExtra("id");
            etTitle.setText(intent.getStringExtra("title"));
            etMinOrder.setText(String.valueOf(intent.getDoubleExtra("min_order", 0)));
            etDiscount.setText(String.valueOf(intent.getDoubleExtra("discount", 0)));
        }

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            double minOrder = Double.parseDouble(etMinOrder.getText().toString());
            double discount = Double.parseDouble(etDiscount.getText().toString());

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_TITLE, title);
            values.put(DatabaseHelper.COLUMN_MIN_ORDER, minOrder);
            values.put(DatabaseHelper.COLUMN_DISCOUNT, discount);

            if (isEdit) {
                db.update(DatabaseHelper.TABLE_PROMOTIONS, values,
                        DatabaseHelper.COLUMN_ID + "=?", new String[]{promoId});
                Toast.makeText(this, "Đã cập nhật!", Toast.LENGTH_SHORT).show();
            } else {
                String id = UUID.randomUUID().toString();
                values.put(DatabaseHelper.COLUMN_ID, id);
                db.insert(DatabaseHelper.TABLE_PROMOTIONS, null, values);
                Toast.makeText(this, "Đã thêm khuyến mãi!", Toast.LENGTH_SHORT).show();
            }
            db.close();
            finish();
        });
    }
}
