package vn.edu.tlu.cse.tuongthiduyen.quanlycuahangdientu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnLogout;
    private DatabaseHelper dbHelper; // Thay ShoppingDAO bằng DatabaseHelper
    private String loggedInUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogout = findViewById(R.id.btnLogout);
        dbHelper = new DatabaseHelper(this); // Khởi tạo DatabaseHelper

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập email và mật khẩu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // dbHelper.login()
                String role = dbHelper.login(email, password);
                if (role != null) {
                    loggedInUserEmail = email;
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công! Vai trò: " + role, Toast.LENGTH_SHORT).show();
                    btnLogin.setVisibility(View.GONE);
                    btnLogout.setVisibility(View.VISIBLE);

                    Intent intent;
                    if (role.equals("Nhân viên")) {
                        intent = new Intent(LoginActivity.this, StaffActivity.class); //  StaffActivity
                    } else {
                        intent = new Intent(LoginActivity.this, CustomerActivity.class); // CustomerActivity
                    }
                    intent.putExtra("user_email", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggedInUserEmail = null;
                btnLogin.setVisibility(View.VISIBLE);
                btnLogout.setVisibility(View.GONE);
                etEmail.setText("");
                etPassword.setText("");
                Toast.makeText(LoginActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}