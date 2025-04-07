package vn.edu.tlu.cse.tuongthiduyen.quanlycuahangdientu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonSignUp;
    private LinearLayout loginLayout, signUpLayout;
    private EditText editTextSignUpEmail, editTextSignUpPassword, editTextFullName;
    private Spinner spinnerRole;
    private Button buttonRegister, buttonBackToLogin;
    public static String loggedInEmail = "";
    public static String userRole = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        // Khởi tạo các view cho đăng nhập
        loginLayout = findViewById(R.id.loginLayout);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        // Khởi tạo các view cho đăng ký
        signUpLayout = findViewById(R.id.signUpLayout);
        editTextSignUpEmail = findViewById(R.id.editTextSignUpEmail);
        editTextSignUpPassword = findViewById(R.id.editTextSignUpPassword);
        editTextFullName = findViewById(R.id.editTextFullName);
        spinnerRole = findViewById(R.id.spinnerRole);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonBackToLogin = findViewById(R.id.buttonBackToLogin);

        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Khách hàng", "Nhân viên"});
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(roleAdapter);

        // Ban đầu hiển thị form đăng nhập, ẩn form đăng ký
        loginLayout.setVisibility(View.VISIBLE);
        signUpLayout.setVisibility(View.GONE);

        // Xử lý nút "Sign Up" để hiển thị form đăng ký
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginLayout.setVisibility(View.GONE);
                signUpLayout.setVisibility(View.VISIBLE);
            }
        });

        // Xử lý nút "Back to Login" để quay lại form đăng nhập
        buttonBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
            }
        });

        // Xử lý nút "Register" để đăng ký tài khoản
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextSignUpEmail.getText().toString().trim();
                String password = editTextSignUpPassword.getText().toString().trim();
                String fullName = editTextFullName.getText().toString().trim();
                String role = spinnerRole.getSelectedItem().toString();

                if (email.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isRegistered = dbHelper.register(email, password, role, fullName);
                if (isRegistered) {
                    Toast.makeText(MainActivity.this, "Registration Success! Please login.", Toast.LENGTH_SHORT).show();
                    // Quay lại form đăng nhập
                    signUpLayout.setVisibility(View.GONE);
                    loginLayout.setVisibility(View.VISIBLE);
                    // Xóa các trường nhập
                    editTextSignUpEmail.setText("");
                    editTextSignUpPassword.setText("");
                    editTextFullName.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Registration Failed! Email already exists.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý nút "Login"
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                String role = dbHelper.login(email, password);
                if (role != null) {
                    loggedInEmail = email;
                    userRole = role;
                    Toast.makeText(MainActivity.this, "Login Success! Role: " + userRole, Toast.LENGTH_SHORT).show();

                    Intent intent;
                    if (userRole.equals("Nhân viên")) {
                        intent = new Intent(MainActivity.this, StaffActivity.class);
                        intent.putExtra("user_email", email);
                        startActivity(intent);

                        // Mở thêm chức năng khuyến mãi nếu là nhân viên
                        Intent promoIntent = new Intent(MainActivity.this, PromotionListActivity.class);
                        startActivity(promoIntent);
                    } else {
                        intent = new Intent(MainActivity.this, CustomerActivity.class);
                        intent.putExtra("user_email", email);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}