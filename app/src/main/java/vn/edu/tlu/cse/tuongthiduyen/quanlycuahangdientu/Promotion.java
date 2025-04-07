package vn.edu.tlu.cse.tuongthiduyen.quanlycuahangdientu;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Promotion {
    private String id;
    private String title;
    private double minOrderValue;
    private double discountAmount;

    public Promotion(String id, String title, double minOrderValue, double discountAmount) {
        this.id = id;
        this.title = title;
        this.minOrderValue = minOrderValue;
        this.discountAmount = discountAmount;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getMinOrderValue() { return minOrderValue; }
    public void setMinOrderValue(double minOrderValue) { this.minOrderValue = minOrderValue; }

    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }
}
