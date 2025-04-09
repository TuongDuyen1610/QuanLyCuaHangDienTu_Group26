package vn.edu.tlu.cse.tuongthiduyen.quanlycuahangdientu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QLCHDT.db";
    private static final int DATABASE_VERSION = 1;

    // Bảng người dùng (Users)
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_FULL_NAME = "full_name";

    // Bảng sản phẩm (Products)
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_PRODUCT_ID = "id";
    private static final String COLUMN_PRODUCT_NAME = "name";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_QUANTITY = "quantity";

    // Bảng giỏ hàng (Cart)
    private static final String TABLE_CART = "carts";
    private static final String COLUMN_CART_ID = "id";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_PRODUCT_ID_FK = "product_id";
    private static final String COLUMN_QUANTITY_IN_CART = "quantity";

    // Bảng đơn hàng (Orders)
    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ORDER_ID = "id";
    private static final String COLUMN_ORDER_USER_EMAIL = "user_email";
    private static final String COLUMN_ORDER_DATE = "order_date";
    private static final String COLUMN_TOTAL = "total";
    private static final String COLUMN_STATUS = "status";

    // Bảng khuyến mãi (Promotions)
    public static final String TABLE_PROMOTIONS = "promotions";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_MIN_ORDER = "min_order";
    public static final String COLUMN_DISCOUNT = "discount";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE + " TEXT, " +
                COLUMN_FULL_NAME + " TEXT)";
        db.execSQL(createUsersTable);

        // Tạo bảng products
        String createProductsTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_NAME + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_PRICE + " INTEGER, " +
                COLUMN_QUANTITY + " INTEGER)";
        db.execSQL(createProductsTable);

        // Tạo bảng carts
        String createCartTable = "CREATE TABLE " + TABLE_CART + " (" +
                COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_EMAIL + " TEXT, " +
                COLUMN_PRODUCT_ID_FK + " INTEGER, " +
                COLUMN_QUANTITY_IN_CART + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_PRODUCT_ID_FK + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + "))";
        db.execSQL(createCartTable);

        // Tạo bảng orders
        String createOrdersTable = "CREATE TABLE " + TABLE_ORDERS + " (" +
                COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORDER_USER_EMAIL + " TEXT, " +
                COLUMN_ORDER_DATE + " TEXT, " +
                COLUMN_TOTAL + " INTEGER, " +
                COLUMN_STATUS + " TEXT)";
        db.execSQL(createOrdersTable);

        // Tạo bảng promotions
        String createTable = "CREATE TABLE " + TABLE_PROMOTIONS + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_MIN_ORDER + " REAL, " +
                COLUMN_DISCOUNT + " REAL)";
        db.execSQL(createTable);

        // Chèn dữ liệu mẫu cho bảng promotions
        insertSamplePromotions(db);

        // Thêm dữ liệu mẫu cho các bảng khác
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOTIONS);
        onCreate(db);
    }

    // Thêm dữ liệu mẫu cho các bảng khác
    private void insertSampleData(SQLiteDatabase db) {
        // Thêm người dùng mẫu
        ContentValues userValues = new ContentValues();
        userValues.put(COLUMN_EMAIL, "staff@store.com");
        userValues.put(COLUMN_PASSWORD, "123456");
        userValues.put(COLUMN_ROLE, "Staff");
        userValues.put(COLUMN_FULL_NAME, "Nguyễn Văn A");
        db.insert(TABLE_USERS, null, userValues);

        userValues.put(COLUMN_EMAIL, "user1@email.com");
        userValues.put(COLUMN_PASSWORD, "abc123");
        userValues.put(COLUMN_ROLE, "Customer");
        userValues.put(COLUMN_FULL_NAME, "Lê Thị Mai");
        db.insert(TABLE_USERS, null, userValues);

        userValues.put(COLUMN_EMAIL, "user2@email.com");
        userValues.put(COLUMN_PASSWORD, "123abc");
        userValues.put(COLUMN_ROLE, "Customer");
        userValues.put(COLUMN_FULL_NAME, "Trần Minh Quân");
        db.insert(TABLE_USERS, null, userValues);

        userValues.put(COLUMN_EMAIL, "user3@email.com");
        userValues.put(COLUMN_PASSWORD, "pass123");
        userValues.put(COLUMN_ROLE, "Customer");
        userValues.put(COLUMN_FULL_NAME, "Phạm Hồng Ngọc");
        db.insert(TABLE_USERS, null, userValues);

        userValues.put(COLUMN_EMAIL, "admin@store.com");
        userValues.put(COLUMN_PASSWORD, "admin123");
        userValues.put(COLUMN_ROLE, "Admin");
        userValues.put(COLUMN_FULL_NAME, "Đỗ Thanh Tùng");
        db.insert(TABLE_USERS, null, userValues);

        // Thêm 25 sản phẩm mẫu

        ContentValues productValues = new ContentValues();
        productValues.put(COLUMN_PRODUCT_NAME, "Samsung Galaxy S23 Ultra");
        productValues.put(COLUMN_TYPE, "Samsung");
        productValues.put(COLUMN_PRICE, 30900000);
        productValues.put(COLUMN_QUANTITY, 9);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "iPhone 14 Pro Max");
        productValues.put(COLUMN_TYPE, "APPLE");
        productValues.put(COLUMN_PRICE, 33900000);
        productValues.put(COLUMN_QUANTITY, 8);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "Dell Inspiron 15");
        productValues.put(COLUMN_TYPE, "Dell");
        productValues.put(COLUMN_PRICE, 18000000);
        productValues.put(COLUMN_QUANTITY, 10);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "MacBook Air M2");
        productValues.put(COLUMN_TYPE, "APPLE");
        productValues.put(COLUMN_PRICE, 28500000);
        productValues.put(COLUMN_QUANTITY, 5);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "Xiaomi Pad 5");
        productValues.put(COLUMN_TYPE, "Xiaomi");
        productValues.put(COLUMN_PRICE, 9500000);
        productValues.put(COLUMN_QUANTITY, 15);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "Sony WH-1000XM5");
        productValues.put(COLUMN_TYPE, "Sony");
        productValues.put(COLUMN_PRICE, 8990000);
        productValues.put(COLUMN_QUANTITY, 10);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "iPad Pro M2 11\"");
        productValues.put(COLUMN_TYPE, "APPLE");
        productValues.put(COLUMN_PRICE, 27900000);
        productValues.put(COLUMN_QUANTITY, 5);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "Mac Mini M2");
        productValues.put(COLUMN_TYPE, "APPLE");
        productValues.put(COLUMN_PRICE, 18900000);
        productValues.put(COLUMN_QUANTITY, 4);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "Asus ROG Phone 7");
        productValues.put(COLUMN_TYPE, "ASUS");
        productValues.put(COLUMN_PRICE, 21900000);
        productValues.put(COLUMN_QUANTITY, 6);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "MSI Gaming GF63");
        productValues.put(COLUMN_TYPE, "MSI");
        productValues.put(COLUMN_PRICE, 17500000);
        productValues.put(COLUMN_QUANTITY, 7);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "Acer Nitro 5");
        productValues.put(COLUMN_TYPE, "Acer");
        productValues.put(COLUMN_PRICE, 16500000);
        productValues.put(COLUMN_QUANTITY, 6);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "Logitech MX Master 3S");
        productValues.put(COLUMN_TYPE, "Logitech");
        productValues.put(COLUMN_PRICE, 2690000);
        productValues.put(COLUMN_QUANTITY, 20);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "Razer BlackWidow V4");
        productValues.put(COLUMN_TYPE, "Razer");
        productValues.put(COLUMN_PRICE, 3590000);
        productValues.put(COLUMN_QUANTITY, 10);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "Apple Watch Series 9");
        productValues.put(COLUMN_TYPE, "APPLE");
        productValues.put(COLUMN_PRICE, 11900000);
        productValues.put(COLUMN_QUANTITY, 6);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "Kindle Paperwhite Gen 11");
        productValues.put(COLUMN_TYPE, "Amazon");
        productValues.put(COLUMN_PRICE, 3790000);
        productValues.put(COLUMN_QUANTITY, 8);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "Canon EOS M50 Mark II");
        productValues.put(COLUMN_TYPE, "Canon");
        productValues.put(COLUMN_PRICE, 17400000);
        productValues.put(COLUMN_QUANTITY, 3);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "GoPro HERO12 Black");
        productValues.put(COLUMN_TYPE, "GoPro");
        productValues.put(COLUMN_PRICE, 10900000);
        productValues.put(COLUMN_QUANTITY, 5);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "DJI Osmo Mobile 6");
        productValues.put(COLUMN_TYPE, "DJI");
        productValues.put(COLUMN_PRICE, 3390000);
        productValues.put(COLUMN_QUANTITY, 10);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "Philips Hue Starter Kit");
        productValues.put(COLUMN_TYPE, "Philips");
        productValues.put(COLUMN_PRICE, 2990000);
        productValues.put(COLUMN_QUANTITY, 12);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "TP-Link Deco X60 (3-pack)");
        productValues.put(COLUMN_TYPE, "TP-Link");
        productValues.put(COLUMN_PRICE, 5790000);
        productValues.put(COLUMN_QUANTITY, 4);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "Samsung Galaxy Tab S9");
        productValues.put(COLUMN_TYPE, "Samsung");
        productValues.put(COLUMN_PRICE, 19900000);
        productValues.put(COLUMN_QUANTITY, 7);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "Beats Studio Buds+");
        productValues.put(COLUMN_TYPE, "Beats");
        productValues.put(COLUMN_PRICE, 4990000);
        productValues.put(COLUMN_QUANTITY, 11);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "HP Pavilion Gaming");
        productValues.put(COLUMN_TYPE, "HP");
        productValues.put(COLUMN_PRICE, 2000000);
        productValues.put(COLUMN_QUANTITY, 7);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "ASUS VivoBook X515");
        productValues.put(COLUMN_TYPE, "ASUS");
        productValues.put(COLUMN_PRICE, 14000000);
        productValues.put(COLUMN_QUANTITY, 12);
        db.insert(TABLE_PRODUCTS, null, productValues);

        productValues.put(COLUMN_PRODUCT_NAME, "Lenovo ThinkPad E14");
        productValues.put(COLUMN_TYPE, "Lenovo");
        productValues.put(COLUMN_PRICE, 16500000);
        productValues.put(COLUMN_QUANTITY, 6);
        db.insert(TABLE_PRODUCTS, null, productValues);

        // Thêm đơn hàng mẫu
        ContentValues orderValues = new ContentValues();
        orderValues.put(COLUMN_ORDER_USER_EMAIL, "user1@email.com");
        orderValues.put(COLUMN_ORDER_DATE, "2025-04-01");
        orderValues.put(COLUMN_TOTAL, 18000000);
        orderValues.put(COLUMN_STATUS, "Đã thanh toán");
        db.insert(TABLE_ORDERS, null, orderValues);

        orderValues.put(COLUMN_ORDER_USER_EMAIL, "user2@email.com");
        orderValues.put(COLUMN_ORDER_DATE, "2025-04-02");
        orderValues.put(COLUMN_TOTAL, 28000000);
        orderValues.put(COLUMN_STATUS, "Đang xử lý");
        db.insert(TABLE_ORDERS, null, orderValues);
    }

    // Thêm dữ liệu mẫu cho bảng promotions
    public void insertSamplePromotions(SQLiteDatabase db) {
        try {
            // Dữ liệu mẫu
            ContentValues values = new ContentValues();

            // Dữ liệu 1
            values.put(COLUMN_ID, "PROMO001");
            values.put(COLUMN_TITLE, "Khuyến mãi Mùa Hè");
            values.put(COLUMN_MIN_ORDER, 500000);
            values.put(COLUMN_DISCOUNT, 10.0); // Giảm 10%
            db.insert(TABLE_PROMOTIONS, null, values);

            // Dữ liệu 2
            values.clear();
            values.put(COLUMN_ID, "PROMO002");
            values.put(COLUMN_TITLE, "Khuyến mãi Sinh Nhật Cửa Hàng");
            values.put(COLUMN_MIN_ORDER, 300000);
            values.put(COLUMN_DISCOUNT, 18.0); // Giảm 18%
            db.insert(TABLE_PROMOTIONS, null, values);

            // Dữ liệu 3
            values.clear();
            values.put(COLUMN_ID, "PROMO002");
            values.put(COLUMN_TITLE, "Khuyến mãi Mừng Xuân Sang ");
            values.put(COLUMN_MIN_ORDER, 300000);
            values.put(COLUMN_DISCOUNT, 10.0); // Giảm 10%
            db.insert(TABLE_PROMOTIONS, null, values);

            // Dữ liệu 4
            values.clear();
            values.put(COLUMN_ID, "PROMO002");
            values.put(COLUMN_TITLE, "Khuyến mãi Ngày Phụ Nữ Việt Nam");
            values.put(COLUMN_MIN_ORDER, 300000);
            values.put(COLUMN_DISCOUNT, 15.0); // Giảm 15%
            db.insert(TABLE_PROMOTIONS, null, values);


            // Dữ liệu 5
            values.clear();
            values.put(COLUMN_ID, "PROMO003");
            values.put(COLUMN_TITLE, "Giảm Giá Cuối Năm");
            values.put(COLUMN_MIN_ORDER, 1000000);
            values.put(COLUMN_DISCOUNT, 20.0); // Giảm 20%
            db.insert(TABLE_PROMOTIONS, null, values);
        } catch (Exception e) {
            Log.e("insertSamplePromotions", "Error inserting sample promotions: " + e.getMessage());
        }
    }

    // Đăng nhập
    public String login(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ROLE + " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password});
        if (cursor.moveToFirst()) {
            int roleIndex = cursor.getColumnIndex(COLUMN_ROLE);
            String role = roleIndex >= 0 ? cursor.getString(roleIndex) : null;
            cursor.close();
            return role;
        }
        cursor.close();
        return null;
    }

    // Đăng ký người dùng mới
    public boolean register(String email, String password, String role, String fullName) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Kiểm tra xem email đã tồn tại chưa
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // Email đã tồn tại
        }
        cursor.close();

        // Thêm người dùng mới
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_FULL_NAME, fullName);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1; // Trả về true nếu thêm thành công
    }

    // Lấy danh sách sản phẩm
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                int idIndex = cursor.getColumnIndex(COLUMN_PRODUCT_ID);
                int nameIndex = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
                int typeIndex = cursor.getColumnIndex(COLUMN_TYPE);
                int priceIndex = cursor.getColumnIndex(COLUMN_PRICE);
                int quantityIndex = cursor.getColumnIndex(COLUMN_QUANTITY);

                if (idIndex >= 0) product.setId(cursor.getInt(idIndex));
                if (nameIndex >= 0) product.setName(cursor.getString(nameIndex));
                if (typeIndex >= 0) product.setType(cursor.getString(typeIndex));
                if (priceIndex >= 0) product.setPrice(cursor.getInt(priceIndex));
                if (quantityIndex >= 0) product.setQuantity(cursor.getInt(quantityIndex));

                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    // Thêm sản phẩm
    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_TYPE, product.getType());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        db.insert(TABLE_PRODUCTS, null, values);
    }

    // Sửa sản phẩm
    public void updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_TYPE, product.getType());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(product.getId())});
    }

    // Xóa sản phẩm
    public void deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)});
    }

    // Thêm sản phẩm vào giỏ hàng
    public void addToCart(String userEmail, int productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, userEmail);
        values.put(COLUMN_PRODUCT_ID_FK, productId);
        values.put(COLUMN_QUANTITY_IN_CART, quantity);
        db.insert(TABLE_CART, null, values);
    }

    // Lấy giỏ hàng của người dùng
    public List<CartItem> getCartItems(String userEmail) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT c." + COLUMN_CART_ID + ", c." + COLUMN_USER_EMAIL + ", c." + COLUMN_PRODUCT_ID_FK + ", c." + COLUMN_QUANTITY_IN_CART +
                ", p." + COLUMN_PRODUCT_NAME + ", p." + COLUMN_PRICE +
                " FROM " + TABLE_CART + " c JOIN " + TABLE_PRODUCTS + " p ON c." + COLUMN_PRODUCT_ID_FK + "=p." + COLUMN_PRODUCT_ID +
                " WHERE c." + COLUMN_USER_EMAIL + "=?", new String[]{userEmail});
        if (cursor.moveToFirst()) {
            do {
                CartItem item = new CartItem();
                int cartIdIndex = cursor.getColumnIndex(COLUMN_CART_ID);
                int userEmailIndex = cursor.getColumnIndex(COLUMN_USER_EMAIL);
                int productIdIndex = cursor.getColumnIndex(COLUMN_PRODUCT_ID_FK);
                int quantityIndex = cursor.getColumnIndex(COLUMN_QUANTITY_IN_CART);
                int productNameIndex = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
                int priceIndex = cursor.getColumnIndex(COLUMN_PRICE);

                if (cartIdIndex >= 0) item.setCartId(cursor.getInt(cartIdIndex));
                if (userEmailIndex >= 0) item.setUserEmail(cursor.getString(userEmailIndex));
                if (productIdIndex >= 0) item.setProductId(cursor.getInt(productIdIndex));
                if (quantityIndex >= 0) item.setQuantity(cursor.getInt(quantityIndex));
                if (productNameIndex >= 0) item.setProductName(cursor.getString(productNameIndex));
                if (priceIndex >= 0) item.setPrice(cursor.getInt(priceIndex));

                cartItems.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems;
    }

    // Xóa giỏ hàng sau khi thanh toán
    public void clearCart(String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_USER_EMAIL + "=?", new String[]{userEmail});
    }

    // Tạo đơn hàng
    public void createOrder(String userEmail, String date, int total, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_USER_EMAIL, userEmail);
        values.put(COLUMN_ORDER_DATE, date);
        values.put(COLUMN_TOTAL, total);
        values.put(COLUMN_STATUS, status);
        db.insert(TABLE_ORDERS, null, values);
    }

    // Lấy danh sách đơn hàng
    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ORDERS, null);
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                int idIndex = cursor.getColumnIndex(COLUMN_ORDER_ID);
                int userEmailIndex = cursor.getColumnIndex(COLUMN_ORDER_USER_EMAIL);
                int orderDateIndex = cursor.getColumnIndex(COLUMN_ORDER_DATE);
                int totalIndex = cursor.getColumnIndex(COLUMN_TOTAL);
                int statusIndex = cursor.getColumnIndex(COLUMN_STATUS);

                if (idIndex >= 0) order.setId(cursor.getInt(idIndex));
                if (userEmailIndex >= 0) order.setUserEmail(cursor.getString(userEmailIndex));
                if (orderDateIndex >= 0) order.setOrderDate(cursor.getString(orderDateIndex));
                if (totalIndex >= 0) order.setTotal(cursor.getInt(totalIndex));
                if (statusIndex >= 0) order.setStatus(cursor.getString(statusIndex));

                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderList;
    }

    public ArrayList<Promotion> getAllPromotions() {
        ArrayList<Promotion> promotions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM promotions", null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String title = cursor.getString(1);
                double minValue = cursor.getDouble(2);
                double discount = cursor.getDouble(3);
                promotions.add(new Promotion(id, title, minValue, discount));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return promotions;
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cart_table", null, null); // Xóa tất cả các bản ghi trong bảng giỏ hàng
        db.close();
    }
    public Promotion getBestPromotion(double total) {
        SQLiteDatabase db = this.getReadableDatabase();
        Promotion bestPromo = null;
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_PROMOTIONS + " WHERE " + COLUMN_MIN_ORDER + " <= ? ORDER BY " + COLUMN_DISCOUNT + " DESC LIMIT 1",
                new String[]{String.valueOf(total)}
        );

        if (cursor.moveToFirst()) {
            bestPromo = new Promotion(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MIN_ORDER)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT))
            );
        }

        cursor.close();
        db.close();
        return bestPromo;
    }


}