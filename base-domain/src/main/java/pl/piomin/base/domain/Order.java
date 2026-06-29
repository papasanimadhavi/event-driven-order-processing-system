package pl.piomin.base.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Order {
    private Long id;
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @Min(value = 1, message = "Product count must be greater than 0")
    private int productCount;

    @Min(value = 1, message = "Price must be greater than 0")
    private int price;
    private String status;
    private String source;

    public Order() {
    }

    public Order(Long id, Long customerId, Long productId, String status) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.status = status;
    }

    public Order(Long id, Long customerId, Long productId, int productCount, int price) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.productCount = productCount;
        this.price = price;
        this.status = "NEW";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", productId=" + productId +
                ", productCount=" + productCount +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
