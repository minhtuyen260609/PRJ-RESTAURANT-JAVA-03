package org.example.model;

import org.example.constant.OrderStatus;

import java.sql.Timestamp;

public class Order {
    private int id;
    private Integer tableId;
    private Integer userId;
    private OrderStatus status;
    private Timestamp createdAt;

    public Order() {
    }

    public Order(int id, Integer tableId, Integer userId, OrderStatus status, Timestamp createdAt) {
        this.id = id;
        this.tableId = tableId;
        this.userId = userId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", tableId=" + tableId +
                ", userId=" + userId +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
