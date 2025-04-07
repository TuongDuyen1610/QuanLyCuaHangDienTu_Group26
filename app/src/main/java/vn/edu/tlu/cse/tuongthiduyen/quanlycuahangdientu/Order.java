package vn.edu.tlu.cse.tuongthiduyen.quanlycuahangdientu;

public class Order {
    private int id;
    private String userEmail;
    private String orderDate;
    private int total;
    private String status;

    public Order() {}

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}